package com.jyyl.guideapp.biz;


import com.jyyl.guideapp.utils.LogUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 用户业务逻辑管理类
 * @author ShangBB
 * @date 2015-11-13 下午5:52:11
 */
public class UserBiz {
    private static final String TAG = "UserBiz";

    private static UserBiz mInstance;
    private static Lock mLock = new ReentrantLock();

    public static synchronized UserBiz getInstance() {

        mLock.lock();
        try {
            if (mInstance == null) {
                mInstance = new UserBiz();
            }
        } finally {
            mLock.unlock();
        }
        return mInstance;
    }

    /**
     * 用户登录
     * @param account 账号/手机号
     * @param password 密码
     * @return ReturnMessage 返回类型
     */
    public ReturnMessage userLogin(String account, String password) {
        ReturnMessage returnMessage = new ReturnMessage();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if ("15268990185".equals(account)) {
            returnMessage.setResultFailure();
            returnMessage.setResultMessage("手机号或密码输入错误");
            LogUtils.i(TAG, "登录失败");
            return returnMessage;
        } else {
            returnMessage.setResultOk();
            LogUtils.i(TAG, "登录成功");
            return returnMessage;
        }
    }

    /**
     * 新用户注册
     * @param account 账号（手机号）
     * @param securityCode 验证码
     * @param password 密码
     * @return
     */
    public ReturnMessage userRegister(String account, String securityCode,
                                      String password) {
        ReturnMessage returnMessage = new ReturnMessage();
        if ("15268990185".equals(account)) {
            returnMessage.setResultFailure();
            returnMessage.setResultMessage("账号已存在");
            LogUtils.i(TAG, "注册失败");
            return returnMessage;
        }
        returnMessage.setResultOk();
        LogUtils.i(TAG, "注册成功");
        return returnMessage;
    }
}
