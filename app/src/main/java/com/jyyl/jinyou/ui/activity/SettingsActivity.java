package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.service.update.UpdateManager;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.SPUtils;

/**
 * @Fuction: 设置界面
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class SettingsActivity extends BaseActivity {
    private Toolbar toolbar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        initToolBar();
    }
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("系统设置");
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
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()){
            case R.id.tv_check_updates:
                //检查版本更新
                new UpdateManager(this);
                break;

            case R.id.tv_exit_login:
                openActivity(mContext, LoginActivity.class);
                SPUtils.put(mContext, Sp.SP_KEY_LOGIN_STATE, false);
                finish();
                break;
        }
    }
}
