package com.jyyl.guideapp.bean;

/**
 * @Fuction: 通知消息
 * @Author: Shang
 * @Date: 2016/4/28  10:48
 */
public class NoticeInfo {

    private String message;
    private boolean isCheck;

    public NoticeInfo(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
