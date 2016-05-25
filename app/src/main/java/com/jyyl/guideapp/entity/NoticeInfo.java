package com.jyyl.guideapp.entity;

/**
 * @Fuction: 通知消息
 * @Author: Shang
 * @Date: 2016/4/28  10:48
 */
public class NoticeInfo {

    private String title;
    private String content;
    private String time;
    private boolean isCheck;

    public NoticeInfo(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.isCheck = false;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
