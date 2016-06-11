package com.jyyl.jinyou.entity;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * @Fuction: 游客信息
 * @Author: Shang
 * @Date: 2016/4/28  14:40
 */
public class MemberInfo implements Serializable {
    private MemberInfoResult mMemberInfoResult;
    private LatLng latLng;

    public MemberInfo(MemberInfoResult memberInfoResult) {
        this.mMemberInfoResult = memberInfoResult;
    }

    public MemberInfoResult getMemberInfoResult() {
        return mMemberInfoResult;
    }

    public void setMemberInfoResult(MemberInfoResult memberInfoResult) {
        this.mMemberInfoResult = memberInfoResult;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
