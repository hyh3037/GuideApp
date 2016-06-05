package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.abading.ABaDingMethod;
import com.jyyl.jinyou.constans.It;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Fuction: 新增设备
 * @Author: Shang
 * @Date: 2016/6/5 14:17
 */
public class DeviceAddActivity extends BaseActivity {

    private static String TAG = "DeviceAddActivity";

    private Toolbar toolbar;
    private TextView mToolbarRightTv;
    private String mTitle = "新增设备";
    private Context mContext;

    private EditText mDeviceNickname;
    private EditText mDeviceImei;
    private EditText mDeviceSosNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add);
        initToolBar();
    }

    @Override
    protected void initViews() {
        super.initViews();

        mDeviceNickname = (EditText) findViewById(R.id.et_device_name);
        mDeviceImei = (EditText) findViewById(R.id.et_device_imei);
        mDeviceSosNo = (EditText) findViewById(R.id.et_device_sos_no);

        mDeviceNickname.setText("腕表");

        String sosNo = (String) SPUtils.get(mContext, Sp.SP_KEY_LAST_LOGIN_ACCOUNT,
                getResources().getString(R.string.device_sos_no_hint));
        mDeviceSosNo.setText(sosNo);

        Bundle bundle = getIntent().getExtras();
        int intentCode = bundle.getInt(It.BUNDLE_KEY_INTENT_CODE);
        if ( 0 == intentCode ){
            mDeviceImei.setText(getResources().getString(R.string.device_imei_hint));
        }else if ( 1 == intentCode ){
            String bindingValidationCode = bundle.getString("result");
            LogUtils.d(bindingValidationCode);
            addDeviceByScan(bindingValidationCode);
        }
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle(mTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        mToolbarRightTv = (TextView) findViewById(R.id.toolbar_right_tv);
        mToolbarRightTv.setText("保存");
        mToolbarRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 提交设备信息,设置sos
            }
        });
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
     * 扫描添加设备
     * @param bindingValidationCode
     *         扫描成功后的字符串后面的查询字段
     */
    private void addDeviceByScan(final String bindingValidationCode) {
        Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                JSONObject jsonObject = ABaDingMethod.getInstance()
                        .bindingDeviceByScan(bindingValidationCode);
                subscriber.onNext(jsonObject);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new BaseSubscriber<JSONObject>(mContext) {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject != null) {

                            try {
                                String deviceId = (String) jsonObject.get("imei");
                                String bindingId = (String) jsonObject.get("id");
                                LogUtils.d(TAG, "IMEI==>>"+deviceId);
                                mDeviceImei.setText(deviceId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }
}
