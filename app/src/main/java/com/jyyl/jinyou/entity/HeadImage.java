package com.jyyl.jinyou.entity;

/**
 * @Fuction: 服务器返回的图片信息
 * @Author: Shang
 * @Date: 2016/6/10  14:37
 */
public class HeadImage {

    private String memberId;
    private String headAddress;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getHeadAddrdss() {
        return headAddress;
    }

    public void setHeadAddrdss(String headAddrdss) {
        this.headAddress = headAddrdss;
    }
}
