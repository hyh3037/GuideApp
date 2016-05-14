package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.constans.It;
import com.jyyl.guideapp.ui.base.BaseActivity;

/**
 * @Fuction: 消息详情
 * @Author: Shang
 * @Date: 2016/5/13  17:17
 */
public class NoticeDetailsActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView mContentTv;
    private Context mContext;

    private String toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_notice_details);
        initToolBar();
    }

    @Override
    protected void initViews() {
        super.initViews();
        mContentTv = (TextView) findViewById(R.id.tv_notice_content);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        Intent intent = getIntent();
        toolbarTitle = intent.getStringExtra(It.BUNDLE_KEY_NOTICE_TITLE);
        if (!toolbarTitle.isEmpty()){
            toolbar.setTitle(toolbarTitle);
            mContentTv.setText(toolbarTitle);
        }
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

}
