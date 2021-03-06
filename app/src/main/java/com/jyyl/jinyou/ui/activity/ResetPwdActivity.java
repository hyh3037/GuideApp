package com.jyyl.jinyou.ui.activity;

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

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.It;
import com.jyyl.jinyou.http.ApiException;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.http.HttpResult;
import com.jyyl.jinyou.http.ResultStatus;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.RegexUtils;
import com.jyyl.jinyou.utils.T;


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

    private String account, securityCode, newPassword, reNewPassword;

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
        toolbar.setTitle("重置密码");
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
                newPassword = mNewPwdEt.getText().toString().trim();
                reNewPassword = mRePwdEt.getText().toString().trim();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(securityCode)
                        || TextUtils.isEmpty(newPassword)
                        || TextUtils.isEmpty(reNewPassword)) {
                    T.showShortToast(this, getString(R.string.toast_submit_information_not_null));
                } else if (!RegexUtils.checkPassword(newPassword)) {
                    T.showShortToast(this, getString(R.string.toast_password_format_error));
                } else if (!newPassword.equals(reNewPassword)) {
                    T.showShortToast(this, getString(R.string.toast_two_password_not_consistent));
                } else if (newPassword.equals(reNewPassword)) {
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
        HttpMethods.getInstance().resetAccount(account, newPassword, securityCode)
                .subscribe(new BaseSubscriber<HttpResult>() {
                    @Override
                    public void onNext(HttpResult httpResult) {
                        if ((ResultStatus.HTTP_SUCCESS).equals(httpResult.getStatus())) {

                            Bundle bundle = new Bundle();
                            bundle.putInt(It.START_INTENT_WITH, It.ACTIVITY_REGISTER);
                            bundle.putString(It.BUNDLE_KEY_LOGIN_ACCOUNT, account);
                            bundle.putString(It.BUNDLE_KEY_LOGIN_PASSWOED, newPassword);
                            openActivity(mContext, LoginActivity.class, bundle);
                            T.showShortToast(mContext, getString(R.string.toast_reset_success));
                            finish();
                        } else {
                            throw new ApiException(httpResult.getDescritpion());
                        }
                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getSecurityCode() {
        HttpMethods.getInstance().getSecurityCode(account, "1")
                .subscribe(new BaseSubscriber<HttpResult>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        if ((ResultStatus.HTTP_SUCCESS).equals(httpResult.getStatus())) {
                            T.showShortToast(mContext, getString(R.string
                                    .toast_verification_code_has_been_sent));
                            TimeCount timeCount = new TimeCount(30000, 1000);
                            timeCount.start();
                            LogUtils.d("验证码发送成功");
                        } else {
                            throw new ApiException(httpResult.getDescritpion());
                        }

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
