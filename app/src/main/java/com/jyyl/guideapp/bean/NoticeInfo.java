package com.jyyl.guideapp.bean;

/**
 * @Fuction: 通知消息
 * @Author: Shang
 * @Date: 2016/4/28  10:48
 */
public class NoticeInfo {

    public NoticeInfo(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
