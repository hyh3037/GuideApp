package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.ui.base.BaseActivity;
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

    private EditText mPhoneEt;
    private EditText mSecurityCodeEt;
    private TextView mSendCodeTv;
    private Button nNextBtn;
    private LinearLayout resetItem1;

    private EditText mNewPwdEt;
    private EditText mRePwdEt;
    private Button mResetPwdBtn;
    private LinearLayout resetItem2;

    private String phone, securityCode, newPwd, rePwd;

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
        mPhoneEt = (EditText) findViewById(R.id.et_phone);
        mSecurityCodeEt = (EditText) findViewById(R.id.et_security_code);
        mSendCodeTv = (TextView) findViewById(R.id.send_code);
        nNextBtn = (Button) findViewById(R.id.btn_next);
        resetItem1 = (LinearLayout) findViewById(R.id.reset_item_1);

        mNewPwdEt = (EditText) findViewById(R.id.et_new_pwd);
        mRePwdEt = (EditText) findViewById(R.id.et_re_pwd);
        mResetPwdBtn = (Button) findViewById(R.id.btn_reset_pwd);
        resetItem2 = (LinearLayout) findViewById(R.id.reset_item_2);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSendCodeTv.setOnClickListener(this);
        nNextBtn.setOnClickListener(this);
        mResetPwdBtn.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.send_code://获取验证码
                phone = mPhoneEt.getText().toString().trim();
                if (RegexUtils.checkMobile(phone)) {
                    TimeCount timeCount = new TimeCount(30000, 1000);
                    timeCount.start();
                    T.showShortToast(mContext, "验证码已发送");
                } else {
                    T.showShortToast(mContext, "输入的手机号码有误");
                }

                break;
            case R.id.btn_next://下一步

                //判断验证码是否正确
                securityCode = mSecurityCodeEt.getText().toString().trim();
                if ("1111".equals(securityCode)) {
                    resetItem1.setVisibility(View.GONE);
                    resetItem2.setVisibility(View.VISIBLE);
                    toolbar.setTitle("密码重置");
                } else {
                    T.showShortToast(mContext, "验证码错误");
                }
                break;

            case R.id.btn_reset_pwd:
                newPwd = mNewPwdEt.getText().toString().trim();
                rePwd = mRePwdEt.getText().toString().trim();
                if (newPwd.equals(rePwd)) {
                    /**
                     * 请求服务端重置密码
                     */
                    T.showShortToast(mContext, "密码已修改");
                    openActivity(mContext, LoginActivity.class);
                    finish();
                } else {
                    T.showShortToast(mContext, "两次输入的密码不一致");
                }
                break;
        }
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
