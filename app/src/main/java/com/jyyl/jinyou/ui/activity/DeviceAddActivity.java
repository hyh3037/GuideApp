package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.SPUtils;

/**
 * @Fuction: 添加设备
 * @Author: Shang
 * @Date: 2016/6/5 14:17
 */
public class DeviceAddActivity extends BaseActivity {

    private static String TAG = "DeviceAddActivity";

    private Toolbar toolbar;
    private TextView mToolbarRightTv;
    private String mTitle = "添加设备";
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
}
