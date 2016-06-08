package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.AppUtils;
import com.jyyl.jinyou.utils.SPUtils;

/**
 * @author ShangBB
 * @Description: (欢迎界面,初始化)
 * @date 2016/1/20 0020 下午 2:22
 */
public class SplashActivity extends BaseActivity {
    private Context mContext;
    private boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        isStartGuideActivity();
        setContentView(R.layout.activity_splash);
        //禁用SwipeBackLayout
        setSwipeBackEnable(false);
        initApp();
    }

    /**根据版本是否变更，决定是否启动引导页面*/
    private void isStartGuideActivity() {
        int version = AppUtils.getVersionCode(this);
        int lastVersion = (int) SPUtils.get(this, Sp.SP_KEY_LAST_VERSION, -1);
        boolean isFirst=false;
        if ( version != lastVersion){
            isFirst = true;
            SPUtils.put(this, Sp.SP_KEY_LAST_VERSION, version);
        }
        if (isFirst){
            openActivity(this,GuideActivity.class);
            finish();
        }
    }

    /**初始化应用程序*/
    private void initApp(){

        //登录状态
        isLogin = (boolean) SPUtils.get(this,Sp.SP_KEY_LOGIN_STATE,false);

        //设置初始化界面的图片
//        ImageView imageView = (ImageView) findViewById(R.id.iv_splash);
//        imageView.setImageResource(R.drawable.splash_bg);

        //设定时间之后跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mContext == null){
                    return;
                }
                if (isLogin) {
                    openActivity(mContext, MainActivity.class);
                } else {
                    openActivity(mContext, LoginActivity.class);
                }
                finish();
            }
        }, 2000);

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_alpha_out);
        mContext=null;
    }
}
