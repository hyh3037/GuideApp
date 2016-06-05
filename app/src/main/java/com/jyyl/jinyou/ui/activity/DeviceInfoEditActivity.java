package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.ui.base.BaseActivity;

/**
 * @Fuction: 修改设备信息
 * @Author: Shang
 * @Date: 2016/6/5 14:17
 */
public class DeviceInfoEditActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView mToolbarRightTv;
    private String mTitle = "修改设备信息";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info_edit);
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle(mTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        mToolbarRightTv = (TextView) findViewById(R.id.toolbar_right_tv);
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
        switch (v.getId()){
            case R.id.toolbar_right_tv:
                openActivity(mContext, DeviceInfoActivity.class);
                break;
            default:
                break;
        }
    }
}
