package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.jyyl.jinyou.R;
import com.jyyl.jinyou.abardeen.ABaDingMethod;
import com.jyyl.jinyou.entity.DeviceInfo;
import com.jyyl.jinyou.entity.DeviceResult;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.ui.adapter.DeviceManageAdapter;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.T;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Fuction: 设备管理
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class DeviceManageActivity extends BaseActivity implements RefreshToolbarListener {
    private Toolbar toolbar;
    private ImageView mToolbarRightIv;
    private CheckBox mToolbarCheckBox;
    private TextView mToolbarRightTv;
    private String mTitle = "设备管理";
    private Context mContext;

    private Button mBinddingBtn;

    private ListView mListView;
    private DeviceManageAdapter mAdapter;

    private ArrayList<DeviceInfo> mDatas = new ArrayList<>();
    private boolean flage = false; //是否删除界面
    private boolean isChecked = false; //toolbar的checkbox选择状态
    private boolean isDelFinish = false; //false:取消 true:完成

    public static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_management);
        initToolBar();
        initListview();

        refreshDeviceDatas();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refreshDeviceDatas();
    }

    /**
     * 获取绑定的设备信息
     */
    private void refreshDeviceDatas() {
        HttpMethods.getInstance().getUserDevices()
                .subscribe(new BaseSubscriber<List<DeviceResult>>(mContext) {
                    @Override
                    public void onNext(List<DeviceResult> deviceResults) {
                        LogUtils.d("deviceResults" + deviceResults.toString());
                        mDatas.clear();
                        int number = 1;
                        for (DeviceResult deviceResult : deviceResults){
                            mDatas.add(new DeviceInfo(number, deviceResult));
                            number++;
                        }
                        refreshUI();
                    }
                });
    }

    private void initListview() {
        mListView = (ListView) findViewById(R.id.device_listview);
        mAdapter = new DeviceManageAdapter(mContext, mDatas, R.layout.item_device_listview);
        mAdapter.setFlag(flage);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(findViewById(R.id.device_empty_view));
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle(mTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        initToolbarAddview();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化toolbar中的其他控件
     */
    private void initToolbarAddview() {
        mToolbarRightIv = (ImageView) findViewById(R.id.toolbar_right_iv);
        mToolbarCheckBox = (CheckBox) findViewById(R.id.toolbar_checkbox);
        mToolbarRightTv = (TextView) findViewById(R.id.toolbar_right_tv);
//        mToolbarRightIv.setOnClickListener(this);
        mToolbarCheckBox.setOnClickListener(this);
        mToolbarRightTv.setOnClickListener(this);
        mToolbarRightIv.setImageResource(R.drawable.delete);
//        if (mDatas.size() > 0) {
//            mToolbarRightIv.setVisibility(View.VISIBLE);
//        } else {
//            mToolbarRightIv.setVisibility(View.GONE);
//        }
        mToolbarCheckBox.setVisibility(View.GONE);
        mToolbarRightTv.setVisibility(View.GONE);
    }

    @Override
    protected void initViews() {
        super.initViews();

        mBinddingBtn = (Button) findViewById(R.id.btn_binding_device);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBinddingBtn.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.btn_binding_device:
                openActivity(mContext, CaptureActivity.class);
                break;
            case R.id.toolbar_right_iv://进入批量删除模式
                flage = true;
                mAdapter.setFlag(true);
                toolbar.setTitle(null);
                toolbar.setNavigationIcon(null);
                mBinddingBtn.setVisibility(View.GONE);
                mToolbarRightIv.setVisibility(View.GONE);
                mToolbarCheckBox.setVisibility(View.VISIBLE);
                mToolbarRightTv.setVisibility(View.VISIBLE);
                refreshToolbar();
                break;

            case R.id.toolbar_checkbox:
                if (isChecked) {
                    //全不选
                    for (DeviceInfo deviceInfo : mDatas) {
                        deviceInfo.setCheck(false);
                    }
                    mToolbarCheckBox.setChecked(false);
                    isChecked = false;
                } else {
                    //全选
                    for (DeviceInfo deviceInfo : mDatas) {
                        deviceInfo.setCheck(true);
                    }
                    mToolbarCheckBox.setChecked(true);
                    isChecked = true;
                }
                refreshToolbar();
                break;

            case R.id.toolbar_right_tv: //完成（确认删除）
                if (isDelFinish) {
                    itemRemove(mDatas);
                    for (DeviceInfo DeviceInfo : mDatas) {
                        DeviceInfo.setCheck(false);
                    }
                }
                flage = false;
                mAdapter.setFlag(false);
                toolbar.setTitle(mTitle);
                toolbar.setNavigationIcon(R.drawable.ic_back);
                mBinddingBtn.setVisibility(View.VISIBLE);
                mToolbarCheckBox.setVisibility(View.GONE);
                mToolbarRightTv.setVisibility(View.GONE);
                refreshUI();
                break;
        }
    }

    /**
     * 删除选中设备
     * @param list 设备集合
     */
    public void itemRemove(ArrayList<DeviceInfo> list) {
        final Iterator<DeviceInfo> it = list.iterator();
        while (it.hasNext()) {
            DeviceInfo deviceInfo = it.next();
            if (deviceInfo.isCheck()) {
                final String bindingId = deviceInfo.getDeviceResult().getDeviceBindId();
                final String deviceId = deviceInfo.getDeviceResult().getDeviceIMEI();
                if (bindingId!=null){
                    Observable.create(new Observable.OnSubscribe<Boolean>() {
                        @Override
                        public void call(Subscriber<? super Boolean> subscriber) {
                            boolean isRemove = ABaDingMethod.getInstance()
                                    .deleteDevice(bindingId);
                            subscriber.onNext(isRemove);
                            subscriber.onCompleted();
                        }
                    })
                            .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                            .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                            .subscribe(new BaseSubscriber<Boolean>(mContext) {
                                @Override
                                public void onNext(Boolean b) {
                                    if (b){
                                        T.showShortToast(mContext, deviceId + "删除成功");
                                        it.remove();
                                    }else {
                                        T.showShortToast(mContext, deviceId + "删除失败");
                                    }
                                }
                            });
                }else {
                    it.remove();
                }

            }
        }
    }

    /**
     * 刷新 批量删除模式 toolbar
     */
    @Override
    public void refreshToolbar() {
        refresh();
        mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        int mCheckedNum = 0;
        for (DeviceInfo info : mDatas) {
            if (info.isCheck()) {
                mCheckedNum++;
            }
        }
        String checkNum = String.valueOf(mCheckedNum) + " 已选中";
        mToolbarCheckBox.setText(checkNum);

        if (flage && mCheckedNum > 0) {
            isDelFinish = true;
            mToolbarRightTv.setText("完成");
        } else {
            isDelFinish = false;
            mToolbarRightTv.setText("取消");
        }

        if (mCheckedNum == mDatas.size()) {
            mToolbarCheckBox.setChecked(true);
            isChecked = true;
        } else {
            mToolbarCheckBox.setChecked(false);
            isChecked = false;
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (mDatas.size() > 0) {
            mToolbarRightIv.setVisibility(View.GONE);
        } else {
            mToolbarRightIv.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

}
