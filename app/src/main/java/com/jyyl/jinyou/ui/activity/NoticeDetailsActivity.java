package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.It;
import com.jyyl.jinyou.ui.base.BaseActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * @Fuction: 消息详情
 * @Author: Shang
 * @Date: 2016/5/13  17:17
 */
public class NoticeDetailsActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView mContentTv;
    private String title, content;
    private Context mContext;

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
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            int startIntent = bundle.getInt(It.START_INTENT_WITH);
            if ( It.ACTIVITY_NOTICE == startIntent){
                title = bundle.getString(It.BUNDLE_KEY_NOTICE_TITLE);
                content = bundle.getString(It.BUNDLE_KEY_NOTICE_CONTENT);
            }else if (It.RECEIVER_JPUSH_NOTICE == startIntent){
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content = bundle.getString(JPushInterface.EXTRA_ALERT);
            }
            mContentTv.setText(content);

        }
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
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
