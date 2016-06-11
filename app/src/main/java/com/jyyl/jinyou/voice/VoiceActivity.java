package com.jyyl.jinyou.voice;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jyyl.jinyou.R;
import com.jyyl.jinyou.abardeen.AbardeenMethod;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.entity.GuideInfoResult;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.FileUtils;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.SPUtils;
import com.jyyl.jinyou.utils.TimeUtils;
import com.nickming.view.AudioRecordButton;
import com.nickming.view.AudioRecordButton.AudioFinishRecorderListener;
import com.nickming.view.Recorder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VoiceActivity extends BaseActivity {

    private Toolbar toolbar;
    private Context mContext;

    private AudioRecordButton button;

    private ListView mlistview;
    private ArrayAdapter<Recorder> mAdapter;
    private View viewanim;
    private List<Recorder> mDatas = new ArrayList<>();

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_voice);

        initviews();
        initToolBar();
        initListview();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("发送消息");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initviews() {
        loadingDialog = createLoadingDialog(this);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> imeiList = bundle.getStringArrayList("imeiList");

        button = (AudioRecordButton) findViewById(R.id.recordButton);
        button.setAudioFinishRecorderListener(new AudioFinishRecorderListener() {

            @Override
            public void onFinished(final float seconds, final String filePath) {
                Recorder recorder = new Recorder(seconds, filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                mlistview.setSelection(mDatas.size() - 1);

                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String message = "";
                        File file = new File(filePath);
                        byte[] fileByte = new byte[0];
                        try {
                            fileByte = FileUtils.getByte(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String dataId = TimeUtils.getTimestamp();
                        ArrayList<String> targets = new ArrayList<>();
                        targets.add("T" + "867293023112009");
                        int length = 0;
                        if (fileByte != null) {
                            length = fileByte.length;
                        }
                        int parts = 1;
                        int playLength = (int) seconds + 1;
                        String type = "DF";
                        JSONObject requestResult = AbardeenMethod.getInstance()
                                .requestTransferVoice(dataId,targets,length,parts,playLength,type);
                        try {
                            String code = (String) requestResult.get("code");
                            if ("0".equals(code)){
                                AbardeenMethod.getInstance().startTransferVoice(dataId, fileByte);
                                boolean b = AbardeenMethod.getInstance().endTransferVoice(dataId);
                                if (b){
                                    message = "语音发送成功";
                                }else {
                                    message = "语音发送失败";
                                }
                            }else {
                                message = "语音发送失败";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext(message);
                        subscriber.onCompleted();
                    }
                })
                        .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                        .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                        .subscribe(new BaseSubscriber<String>() {
                            @Override
                            public void onNext(String s) {
                                LogUtils.d(s);
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                loadingDialog.show();
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                loadingDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                loadingDialog.dismiss();
                            }
                        });
            }
        });
    }

    private void initListview() {
        String guideInfoString = (String) SPUtils.get(mContext, Sp.SP_KEY_USER_OBJECT, "-1");
        GuideInfoResult guideInfoResult = new Gson()
                .fromJson(guideInfoString, GuideInfoResult.class);
        String photoUrl = guideInfoResult.getHeadAddrdss();
        mlistview = (ListView) findViewById(R.id.listview);
        mAdapter = new RecorderAdapter(this, mDatas, photoUrl);
        mlistview.setAdapter(mAdapter);

        mlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // 播放动画
                if (viewanim != null) {//让第二个播放的时候第一个停止播放
                    viewanim.setBackgroundResource(R.drawable.adj);
                    viewanim = null;
                }
                viewanim = view.findViewById(R.id.id_recorder_anim);
                viewanim.setBackgroundResource(R.drawable.play);
                AnimationDrawable drawable = (AnimationDrawable) viewanim
                        .getBackground();
                drawable.start();

                // 播放音频
                MediaManager.playSound(mDatas.get(position).getFilePathString(),
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                viewanim.setBackgroundResource(R.drawable.adj);
                            }
                        });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

	/*class Recorder {
        float time;
		String filePathString;

		public Recorder(float time, String filePathString) {
			super();
			this.time = time;
			this.filePathString = filePathString;
		}

		public float getTime() {
			return time;
		}

		public void setTime(float time) {
			this.time = time;
		}

		public String getFilePathString() {
			return filePathString;
		}

		public void setFilePathString(String filePathString) {
			this.filePathString = filePathString;
		}

	}*/

}
