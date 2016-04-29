package com.jyyl.guideapp.bean;

/**
 * @Fuction: 游客信息
 * @Author: Shang
 * @Date: 2016/4/28  14:40
 */
public class MemberInfo {
    private String bindingDeciveId; //绑定的设备ID
    private String name;
    private String idNumber;
    private String photoUrl;
    private String contactTelNumber;

    public MemberInfo(String name) {
        this.name = name;
    }

    public String getBindingDeciveId() {
        return bindingDeciveId;
    }

    public void setBindingDeciveId(String bindingDeciveId) {
        this.bindingDeciveId = bindingDeciveId;
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
}
