package com.jyyl.jinyou.entity;

import java.io.Serializable;

/**
 * @Fuction:
 * @Author: Shang
 * @Date: 2016/6/1  15:40
 */
public class DeviceResult implements Serializable{

    private String deviceId;//设备id
    private String deviceType;//设备类型
    private String deviceIMEI;//设备IMEI
    private String deviceName;//设备名称
    private String ascriptionId;//归属人id，这里指的是导游的id
    private String bindState;//启用状态
    private String devicePhone;//绑定电话--腕表里的电话号是惟一的
    private String deviceNote;//备注
    private String deviceBindId;
    private String sosPhone;
    private String bindTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAscriptionId() {
        return ascriptionId;
    }

    public void setAscriptionId(String ascriptionId) {
        this.ascriptionId = ascriptionId;
    }

    public String getBindState() {
        return bindState;
    }

    public void setBindState(String bindState) {
        this.bindState = bindState;
    }

    public String getDevicePhone() {
        return devicePhone;
    }

    public void setDevicePhone(String devicePhone) {
        this.devicePhone = devicePhone;
    }

    public String getDeviceNote() {
        return deviceNote;
    }

    public void setDeviceNote(String deviceNote) {
        this.deviceNote = deviceNote;
    }

    public String getDeviceBindId() {
        return deviceBindId;
    }

    public void setDeviceBindId(String deviceBindId) {
        this.deviceBindId = deviceBindId;
    }

    public String getSosPhone() {
        return sosPhone;
    }

    public void setSosPhone(String sosPhone) {
        this.sosPhone = sosPhone;
    }

    public String getBindTime() {
        return bindTime;
    }

    public void setBindTime(String bindTime) {
        this.bindTime = bindTime;
    }
}
