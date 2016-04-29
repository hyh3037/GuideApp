package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.biz.ReturnMessage;
import com.jyyl.guideapp.biz.UserBiz;
import com.jyyl.guideapp.constans.It;
import com.jyyl.guideapp.receive.SMSBroadcastReceiver;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.utils.T;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * @author ShangBB
 * @Description: (注册界面)
 * @date 2016/1/20 0020 下午 1:31
 */
public class RegisterActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText mAccEt;
    private EditText mPwdEt;
    private EditText mSecurityCodeEt; //验证码
    private TextView mSendCodeTv;
    private EditText mRePwdEt;
    private Button mRegisterBtn;

    private Context mContext;

    private String account, securityCode, password, rePwd;
    private SMSBroadcastReceiver mSmsBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar();
        mContext = this;
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("注册");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }

    @Override
    protected void initViews() {
        super.initViews();
        mAccEt = (EditText) findViewById(R.id.et_acc);
        mSecurityCodeEt = (EditText) findViewById(R.id.et_security_code);
        mSendCodeTv = (TextView) findViewById(R.id.send_code);
        mPwdEt = (EditText) findViewById(R.id.et_pwd);
        mRePwdEt = (EditText) findViewById(R.id.et_repwd);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSendCodeTv.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.send_code://获取验证码
                TimeCount timeCount = new TimeCount(30000, 1000);
                timeCount.start();
                // TODO 请求接口发送验证码
                T.showShortToast(this, "验证码已发送");
                break;
            case R.id.btn_register://注册成功后跳转到登陆界面
                account = mAccEt.getText().toString().trim();
                securityCode = mSecurityCodeEt.getText().toString().trim();
                password = mPwdEt.getText().toString().trim();
                rePwd = mRePwdEt.getText().toString().trim();
                register();
                break;
        }
    }

    private void register() {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(securityCode)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(rePwd)) {
            T.showShortToast(this, "注册信息不能为空");
        }else if (!password.equals(rePwd)){
            T.showShortToast(this, "两次输入的密码不一致");
        }else {
            Observable
                    .create(new Observable.OnSubscribe<ReturnMessage>() {
                        @Override
                        public void call(Subscriber<? super ReturnMessage> subscriber) {
                            ReturnMessage returnMessage = UserBiz.getInstance()
                                    .userRegister(account, password, securityCode);
                            subscriber.onNext(returnMessage);
                            subscriber.onCompleted();
                        }
                    })
                    .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                    .subscribe(new Action1<ReturnMessage>() {
                        @Override
                        public void call(ReturnMessage returnMessage) {
                            if (returnMessage.isSuccess()) {
                                // 注册成功跳转到登录
                                Bundle bundle = new Bundle();
                                bundle.putInt(It.START_ACTIVITY_WITH, It.ACTION_REGISTER);
                                bundle.putString(It.BUNDLE_KEY_LOGIN_ACCOUNT, account);
                                bundle.putString(It.BUNDLE_KEY_LOGIN_PASSWOED, password);
                                openActivity(mContext, LoginActivity.class, bundle);
                                T.showShortToast(mContext, "注册成功");
                                finish();
                            } else {
                                T.showShortToast(mContext, returnMessage.getResultMessage());
                            }
                        }
                    });
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

    /**
     * ================================接收短信验证==>>开始=================================
     */
    @Override
    protected void onStart() {
        super.onStart();
        // 生成广播处理
        mSmsBroadcastReceiver = new SMSBroadcastReceiver();

        // 实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        // 注册广播
        this.registerReceiver(mSmsBroadcastReceiver, intentFilter);

        mSmsBroadcastReceiver
                .setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
                    @Override
                    public void onReceived(String message) {

                        mSecurityCodeEt.setText(message);

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销短信监听广播
        this.unregisterReceiver(mSmsBroadcastReceiver);
    }

    /**
     * ==============================接收短信验证==>>结束================================
     */

}
