package com.jyyl.guideapp.entity;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * @Fuction: 游客信息
 * @Author: Shang
 * @Date: 2016/4/28  14:40
 */
public class MemberInfo implements Serializable {
    private String name;
    private String idNumber;
    private String photoUrl;
    private String contactTelNumber;
    private String bindingDeciveId; //绑定的设备ID
    private LatLng latLng;

    public MemberInfo(String name) {
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getContactTelNumber() {
        return contactTelNumber;
    }

    public void setContactTelNumber(String contactTelNumber) {
        this.contactTelNumber = contactTelNumber;
    }

    public String getBindingDeciveId() {
        return bindingDeciveId;
    }

    public void setBindingDeciveId(String bindingDeciveId) {
        this.bindingDeciveId = bindingDeciveId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

}
