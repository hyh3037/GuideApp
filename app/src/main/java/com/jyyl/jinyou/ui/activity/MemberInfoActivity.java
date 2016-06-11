package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.entity.MemberInfoResult;
import com.jyyl.jinyou.ui.base.BaseActivity;

/**
 * @Fuction: 游客详情
 * @Author: Shang
 * @Date: 2016/4/28  15:17
 */
public class MemberInfoActivity extends BaseActivity{
    private Toolbar toolbar;
    private Context mContext;

    private MemberInfoResult mMemberInfo;

    //    private View mBloodPressure;
//    private View mBloodsugar;
//    private View mHeartRate;
//    private View mStepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_member_info);
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("游客信息");
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

    @Override
    protected void initViews() {
        super.initViews();
        mMemberInfo = (MemberInfoResult) getIntent().getSerializableExtra("memberInfo");

        TextView nameTv = (TextView) findViewById(R.id.tv_member_name);
        TextView linkmanNameTv = (TextView) findViewById(R.id.tv_linkman_name);
        TextView linkmanPhoneTv = (TextView) findViewById(R.id.tv_linkman_phone);
        TextView deviceImeiTv = (TextView) findViewById(R.id.tv_binding_device_imei);
        TextView devicePhoneTv = (TextView) findViewById(R.id.tv_binding_device_phone);

        nameTv.setText(getString(R.string.member_info_name,
                mMemberInfo.getTouristName()));
        linkmanNameTv.setText(getString(R.string.member_info_linkman_name,
                mMemberInfo.getTouristContentName()));
        linkmanPhoneTv.setText(getString(R.string.member_info_linkman_phone,
                mMemberInfo.getTouristContentPhone()));
        deviceImeiTv.setText(getString(R.string.member_info_device_imei,
                mMemberInfo.getDeviceId()));
        devicePhoneTv.setText(getString(R.string.member_info_device_phone,
                mMemberInfo.getDevicePhone()));
        //        mBloodPressure = findViewById(R.id.blood_pressure);
//        mBloodsugar = findViewById(R.id.blood_sugar);
//        mHeartRate = findViewById(R.id.heart_rate);
//        mStepCounter = findViewById(R.id.step_counter);
    }

    @Override
    protected void initListener() {
        super.initListener();
//        mBloodPressure.setOnClickListener(this);
//        mBloodsugar.setOnClickListener(this);
//        mHeartRate.setOnClickListener(this);
//        mStepCounter.setOnClickListener(this);
    }

//    @Override
//    protected void onViewClick(View v) {
//        super.onViewClick(v);
//        switch (v.getId()){
//            case R.id.blood_pressure:
//                T.showShortToast(mContext,"血压");
//                break;
//            case R.id.blood_sugar:
//                T.showShortToast(mContext,"血糖");
//                break;
//            case R.id.heart_rate:
//                T.showShortToast(mContext,"心率");
//                break;
//            case R.id.step_counter:
//                T.showShortToast(mContext,"步数");
//                break;
//        }
//    }
}
