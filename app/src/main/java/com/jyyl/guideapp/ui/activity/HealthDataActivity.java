package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.utils.T;

/**
 * @Fuction: 体征数据
 * @Author: Shang
 * @Date: 2016/4/28  15:17
 */
public class HealthDataActivity extends BaseActivity{
    private Toolbar toolbar;
    private Context mContext;

    private View mBloodPressure;
    private View mBloodsugar;
    private View mHeartRate;
    private View mStepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_data);
        mContext = this;
        initToolBar();
    }
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        Intent intent = getIntent();
        String toolBarTitle = intent.getStringExtra("name");
        toolbar.setTitle(toolBarTitle);
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
        mBloodPressure = findViewById(R.id.blood_pressure);
        mBloodsugar = findViewById(R.id.blood_sugar);
        mHeartRate = findViewById(R.id.heart_rate);
        mStepCounter = findViewById(R.id.step_counter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBloodPressure.setOnClickListener(this);
        mBloodsugar.setOnClickListener(this);
        mHeartRate.setOnClickListener(this);
        mStepCounter.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()){
            case R.id.blood_pressure:
                T.showShortToast(mContext,"血压");
                break;
            case R.id.blood_sugar:
                T.showShortToast(mContext,"血糖");
                break;
            case R.id.heart_rate:
                T.showShortToast(mContext,"心率");
                break;
            case R.id.step_counter:
                T.showShortToast(mContext,"步数");
                break;
        }
    }
}
