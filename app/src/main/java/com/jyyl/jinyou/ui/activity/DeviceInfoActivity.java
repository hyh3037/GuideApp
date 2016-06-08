package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.abardeen.ABaDingMethod;
import com.jyyl.jinyou.constans.It;
import com.jyyl.jinyou.entity.DeviceResult;
import com.jyyl.jinyou.http.ApiException;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.http.HttpResult;
import com.jyyl.jinyou.http.ResultStatus;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.T;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Fuction: 设备详情
 * @Author: Shang
 * @Date: 2016/6/5 14:17
 */
public class DeviceInfoActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView mToolbarRightTv;
    private String mTitle = "设备详情";
    private Context mContext;
    private TextView mDeviceName;
    private TextView mDeviceImei;
    private TextView mDeviceSos;
    private TextView mDevicePhone;
    private TextView mDeviceBindingTime;
    private TextView mDeviceUseState;
    private Button mDeleteButton;

    private DeviceResult deviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        initToolBar();
    }

    @Override
    protected void initViews() {
        super.initViews();

        this.mDeviceUseState = (TextView) findViewById(R.id.tv_device_info_use_state);
        this.mDeviceBindingTime = (TextView) findViewById(R.id.tv_device_info_binding_time);
        this.mDevicePhone = (TextView) findViewById(R.id.tv_device_info_phone);
        this.mDeviceSos = (TextView) findViewById(R.id.tv_device_info_sos);
        this.mDeviceImei = (TextView) findViewById(R.id.tv_device_info_imei);
        this.mDeviceName = (TextView) findViewById(R.id.tv_device_info_name);

        mDeleteButton = (Button) findViewById(R.id.btn_delete_device);
        mDeleteButton.setOnClickListener(this);

        Intent intent = getIntent();
        deviceInfo = (DeviceResult) intent.getSerializableExtra("deviceInfo");

        mDeviceName.setText(getString(R.string.device_info_name,deviceInfo.getDeviceName()));
        mDeviceImei.setText(getString(R.string.device_info_imei,deviceInfo.getDeviceIMEI()));
        mDeviceSos.setText(getString(R.string.device_info_sos,deviceInfo.getSosPhone()));
        mDevicePhone.setText(getString(R.string.device_info_phone,deviceInfo.getDevicePhone()));
        mDeviceBindingTime.setText(getString(R.string.device_info_binding_time,deviceInfo.getBindTime()));
        String bindState;
        if ("0".equals(deviceInfo.getBindState())){
            bindState = "未启用";
        }else {
            bindState = "使用中";
        }
        mDeviceUseState.setText(getString(R.string.device_info_use_state,bindState));
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle(mTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        mToolbarRightTv = (TextView) findViewById(R.id.toolbar_right_tv);
        mToolbarRightTv.setVisibility(View.VISIBLE);
        mToolbarRightTv.setText("编辑");
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
        switch (v.getId()){
            case R.id.toolbar_right_tv:
                Bundle bundle = new Bundle();
                bundle.putInt(It.START_INTENT_WITH, It.ACTIVITY_DEVICE_INFO);
                bundle.putSerializable("deviceInfo", deviceInfo);
                openActivity(mContext, DeviceInfoEditActivity.class, bundle);
                break;
            case R.id.btn_delete_device:
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        String bindingId = deviceInfo.getDeviceBindId();
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
                                    T.showShortToast(mContext,"删除成功");
                                    deleteDevice();
                                }else {
                                    T.showShortToast(mContext,"删除失败");
                                }
                            }
                        });
                break;
            default:
                break;
        }
    }

    private void deleteDevice() {
        String imei = deviceInfo.getDeviceIMEI();
        HttpMethods.getInstance().deleteDevice(imei)
                .subscribe(new BaseSubscriber<HttpResult>(mContext) {

                    @Override
                    public void onNext(HttpResult httpResult) {
                        if ((ResultStatus.HTTP_SUCCESS).equals(httpResult.getStatus())) {
                            openActivity(mContext, DeviceManageActivity.class);
                            LogUtils.d("后台删除成功");
                        } else {
                            throw new ApiException(httpResult.getDescritpion());
                        }

                    }
                });
    }
}
