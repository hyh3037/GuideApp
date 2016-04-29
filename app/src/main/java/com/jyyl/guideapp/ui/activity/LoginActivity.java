package com.jyyl.guideapp.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.biz.ReturnMessage;
import com.jyyl.guideapp.biz.UserBiz;
import com.jyyl.guideapp.constans.It;
import com.jyyl.guideapp.constans.Sp;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.utils.LogUtils;
import com.jyyl.guideapp.utils.MD5Utils;
import com.jyyl.guideapp.utils.SPUtils;
import com.jyyl.guideapp.utils.T;
import com.jyyl.guideapp.widget.CleanEditText;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author ShangBB
 * @Description: (登陆界面)
 * @date 2016/1/20 0020 下午 1:30
 */
public class LoginActivity extends BaseActivity {
    private CleanEditText mAccEt;
    private EditText mPwdEt;
    private Button mLoginBtn;
    private TextView mForgetPwdTv;
    private TextView mGoRegisterTv;
    private Context mContext;

    private String account, password;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();
        mContext = this;
        //禁用SwipeBackLayout
        setSwipeBackEnable(false);
    }

    private void initToolBar() {
        //        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        TextView textView = (TextView) findViewById(R.id.toolbar_center_title);
        textView.setText("登录");
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setVisibility(View.VISIBLE);
        //        setSupportActionBar(toolbar);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mAccEt = (CleanEditText) findViewById(R.id.et_login_acc);
        mPwdEt = (EditText) findViewById(R.id.et_login_pwd);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mForgetPwdTv = (TextView) findViewById(R.id.login_forget_pwd);
        mGoRegisterTv = (TextView) findViewById(R.id.login_go_register);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLoginBtn.setOnClickListener(this);
        mForgetPwdTv.setOnClickListener(this);
        mGoRegisterTv.setOnClickListener(this);
    }

    /**
     * @Title: initLoginAccount
     * @Description: (默认填写最后一次登录账号和密码)
     */
    private void initLoginAccount() {
        String lastAcc = (String) SPUtils.get(this, Sp.SP_KEY_LAST_LOGIN_ACCOUNT, null);
        String lastPwd = (String) SPUtils.get(this, Sp.SP_KEY_LAST_LOGIN_PASSWORD, null);
        if (lastAcc != null && lastPwd != null) {
            mAccEt.setText(lastAcc);
            mPwdEt.setText(lastPwd);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        LogUtils.i("===>> onNewIntent");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String intentAcc = bundle.getString(It.BUNDLE_KEY_LOGIN_ACCOUNT);
            String intentPwd = bundle.getString(It.BUNDLE_KEY_LOGIN_PASSWOED);
            if (intentAcc != null && intentPwd != null) {
                mAccEt.setText(intentAcc);
                mPwdEt.setText(intentPwd);
            }
        }
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                account = mAccEt.getText().toString().trim();
                password = MD5Utils.toMd5(mPwdEt.getText().toString().trim());
                login();
                break;
            case R.id.login_forget_pwd:
                openActivity(this, ResetPwdActivity.class);
                break;
            case R.id.login_go_register:
                openActivity(this, RegisterActivity.class);
                break;
        }
    }

    private void login() {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            T.showShortToast(mContext, "手机号/密码不能为空");
        } else {
            mDialog = createLoadingDialog(mContext);
            mDialog.show();

            Observable.create(new Observable.OnSubscribe<ReturnMessage>() {
                @Override
                public void call(Subscriber<? super ReturnMessage>
                                         subscriber) {
                    ReturnMessage returnMessage = UserBiz.getInstance()
                            .userLogin(account, password);
                    subscriber.onNext(returnMessage);
                    subscriber.onCompleted();
                }
            })
                    .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber的回调发生在主线程
                    .subscribe(new Observer<ReturnMessage>() {
                        @Override
                        public void onCompleted() {
                            mDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(ReturnMessage returnMessage) {

                            if (returnMessage.isSuccess()) {
                                // 登录成功
                                openActivity(mContext, MainActivity.class);
                                //保存账号信息到SP
                                SPUtils.put(mContext, Sp.SP_KEY_LAST_LOGIN_ACCOUNT,
                                        account);
                                SPUtils.put(mContext, Sp
                                        .SP_KEY_LAST_LOGIN_PASSWORD, password);
                                SPUtils.put(mContext, Sp.SP_KEY_LOGIN_STATE, true);
                                finish();
                            } else {
                                mAccEt.setText("");
                                mPwdEt.setText("");
                                T.showShortToast(mContext, returnMessage
                                        .getResultMessage());
                            }
                        }
                    });
        }
    }

    /**
     * ========================= 退出程序 ===========================
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 将系统当前的时间赋值给exitTime
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_app_exit);
    }


}
