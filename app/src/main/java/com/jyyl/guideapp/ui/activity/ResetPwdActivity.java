package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.constans.It;
import com.jyyl.guideapp.http.BaseSubscriber;
import com.jyyl.guideapp.http.HttpMethods;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.utils.LogUtils;
import com.jyyl.guideapp.utils.RegexUtils;
import com.jyyl.guideapp.utils.T;


/**
 * @author ShangBB
 * @version V1.0
 * @Package com.xaqy.loanmanage.ui.activity
 * @Description: (忘记密码 密码重置)
 * @date 2016/2/16 0016 上午 11:11
 */
public class ResetPwdActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;

    private EditText mAccountEt;
    private EditText mSecurityCodeEt;
    private TextView mSendCodeTv;
    private EditText mNewPwdEt;
    private EditText mRePwdEt;
    private Button mResetPwdBtn;

    private String account, securityCode, newPwd, rePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        //        AndroidBug5497Workaround.assistActivity(this);
        initToolBar();
        mContext = this;
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("手机验证");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPwdActivity.this.finish();
            }
        });
    }

    @Override
    protected void initViews() {
        super.initViews();
        mAccountEt = (EditText) findViewById(R.id.et_phone);
        mSecurityCodeEt = (EditText) findViewById(R.id.et_security_code);
        mSendCodeTv = (TextView) findViewById(R.id.send_code);
        mNewPwdEt = (EditText) findViewById(R.id.et_new_pwd);
        mRePwdEt = (EditText) findViewById(R.id.et_re_pwd);
        mResetPwdBtn = (Button) findViewById(R.id.btn_reset_pwd);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSendCodeTv.setOnClickListener(this);
        mResetPwdBtn.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.send_code://获取验证码
                account = mAccountEt.getText().toString().trim();
                if (account.isEmpty()) {
                    T.showShortToast(mContext, getString(R.string.toast_phone_not_null));
                } else if (RegexUtils.checkMobile(account)) {
                    getSecurityCode();
                } else {
                    T.showShortToast(mContext, getString(R.string.toast_phone_format_error));
                }

                break;

            case R.id.btn_reset_pwd:
                account = mAccountEt.getText().toString().trim();
                securityCode = mSecurityCodeEt.getText().toString().trim();
                newPwd = mNewPwdEt.getText().toString().trim();
                rePwd = mRePwdEt.getText().toString().trim();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(securityCode)
                        || TextUtils.isEmpty(newPwd)
                        || TextUtils.isEmpty(rePwd)) {
                    T.showShortToast(this, getString(R.string.toast_submit_information_not_null));
                } else if (!RegexUtils.checkPassword(newPwd)){
                    T.showShortToast(this, getString(R.string.toast_password_format_error));
                } else if (!newPwd.equals(rePwd)) {
                    T.showShortToast(this, getString(R.string.toast_two_password_not_consistent));
                } else if (newPwd.equals(rePwd)) {
                    resetPassword();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 重置密码
     */
    private void resetPassword() {
        HttpMethods.getInstance().registerAccount(account, newPwd, securityCode)
                .subscribe(new BaseSubscriber<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        // 密码重置成功跳转到登录
                        Bundle bundle = new Bundle();
                        bundle.putInt(It.START_INTENT_WITH, It.ACTIVITY_RESET_PASSWORD);
                        bundle.putString(It.BUNDLE_KEY_LOGIN_ACCOUNT, account);
                        bundle.putString(It.BUNDLE_KEY_LOGIN_PASSWOED, newPwd);
                        openActivity(mContext, LoginActivity.class, bundle);
                        T.showShortToast(mContext, getString(R.string.toast_reset_success));
                        finish();
                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getSecurityCode() {
        HttpMethods.getInstance().getSecurityCode(account)
                .subscribe(new BaseSubscriber<String>(mContext) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        TimeCount timeCount = new TimeCount(30000, 1000);
                        timeCount.start();
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.d(s);
                        T.showShortToast(mContext,getString(R.string.toast_verification_code_has_been_sent));
                    }
                });
    }

    //倒计时
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mSendCodeTv.setText("获取验证码");
            mSendCodeTv.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mSendCodeTv.setClickable(false);//防止重复点击
            mSendCodeTv.setText(millisUntilFinished / 1000 + "s");
        }
    }
}
