package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.abardeen.AbardeenMethod;
import com.jyyl.jinyou.constans.It;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.entity.DeviceInfoResult;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.SPUtils;
import com.jyyl.jinyou.utils.T;

import org.json.JSONObject;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Fuction: 编辑设备信息
 * @Author: Shang
 * @Date: 2016/6/5 14:17
 */
public class DeviceInfoEditActivity extends BaseActivity {

    private static String TAG = "DeviceInfoEditActivity";

    private Toolbar toolbar;
    private TextView mToolbarRightTv;
    private String mTitle = "编辑设备信息";
    private Context mContext;

    private EditText mNameEt;
    private EditText mImeiEt;
    private EditText mSosEt;
    private EditText mPhoneEt;

    private String bindingId, deviceName, deviceImei, deviceSos, devicePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info_edit);

        initToolBar();
    }

    @Override
    protected void initViews() {
        super.initViews();

        mPhoneEt = (EditText) findViewById(R.id.et_device_edit_call_no);
        mSosEt = (EditText) findViewById(R.id.et_device_edit_sos_no);
        mImeiEt = (EditText) findViewById(R.id.tv_device_edit_imei);
        mNameEt = (EditText) findViewById(R.id.et_device_edit_name);

        deviceSos = (String) SPUtils.get(mContext, Sp.SP_KEY_LAST_LOGIN_ACCOUNT, "110");
        mSosEt.setText(deviceSos);

        Bundle bundle = getIntent().getExtras();
        int startActivity = bundle.getInt(It.START_INTENT_WITH);
        if (startActivity == It.ACTIVITY_CAPTURE) {
            bindingId = (String) bundle.get("bindingId");
            deviceImei = (String) bundle.get("deviceImei");
            mImeiEt.setText(deviceImei);
            mImeiEt.setKeyListener(null);

            HttpMethods.getInstance().updateDevice(deviceImei, bindingId, null, null)
                    .subscribe(new BaseSubscriber<List<DeviceInfoResult>>() {
                        @Override
                        public void onNext(List<DeviceInfoResult> resultList) {
                            DeviceInfoResult deviceInfoResult = resultList.get(0);
                            if (deviceInfoResult != null) {
                                LogUtils.d(TAG, "deviceInfoResult==>>" + deviceInfoResult
                                        .toString());
                                devicePhone = deviceInfoResult.getDevicePhone();
                                mPhoneEt.setText(devicePhone);
                                mPhoneEt.setKeyListener(null);
                            }
                        }
                    });

        } else if (startActivity == It.ACTIVITY_DEVICE_INFO) {
            DeviceInfoResult deviceInfo = (DeviceInfoResult) bundle.getSerializable("deviceInfo");
            if (deviceInfo != null) {
                bindingId = deviceInfo.getDeviceBindId();
                deviceName = deviceInfo.getDeviceName();
                deviceImei = deviceInfo.getDeviceIMEI();
                devicePhone = deviceInfo.getDevicePhone();
                mNameEt.setText(deviceName);
                mImeiEt.setText(deviceImei);
                mImeiEt.setKeyListener(null);
                mPhoneEt.setText(devicePhone);
                mPhoneEt.setKeyListener(null);
            }
        }
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle(mTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        mToolbarRightTv = (TextView) findViewById(R.id.toolbar_right_tv);
        mToolbarRightTv.setVisibility(View.VISIBLE);
        mToolbarRightTv.setText("保存");
        mToolbarRightTv.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.toolbar_right_tv:

                deviceName = mNameEt.getText().toString();
                deviceSos = mSosEt.getText().toString();


                //设置sos
                Observable.create(new Observable.OnSubscribe<JSONObject>() {
                    @Override
                    public void call(Subscriber<? super JSONObject> subscriber) {
                        JSONObject jsonObject = AbardeenMethod.getInstance()
                                .setSosNumber(deviceImei, deviceSos);
                        subscriber.onNext(jsonObject);
                        subscriber.onCompleted();
                    }
                })
                        .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                        .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                        .subscribe(new BaseSubscriber<JSONObject>() {
                            @Override
                            public void onNext(JSONObject jsonObject) {
                                if (jsonObject != null) {
                                    LogUtils.d(TAG, "设置sos成功");
                                    finish();
                                }
                            }
                        });

                HttpMethods.getInstance().updateDevice(deviceImei, bindingId, deviceName, deviceSos)
                        .subscribe(new BaseSubscriber<List<DeviceInfoResult>>() {
                            @Override
                            public void onNext(List<DeviceInfoResult> resultList) {
                                DeviceInfoResult deviceInfoResult = resultList.get(0);
                                if (deviceInfoResult != null) {
                                    LogUtils.d(TAG, "保存deviceResult==>>" + deviceInfoResult
                                            .toString());
                                    T.showShortToast(mContext, "设备信息修改成功");
                                    openActivity(mContext, DeviceManageActivity.class);
                                } else {
                                    T.showShortToast(mContext, "设备信息修改失败");
                                }
                            }
                        });

                break;
            default:
                break;
        }
    }


}
