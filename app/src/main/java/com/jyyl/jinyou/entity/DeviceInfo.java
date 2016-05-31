package com.jyyl.jinyou.entity;

import com.baidu.mapapi.model.LatLng;

/**
 * @Fuction: 绑定设备信息
 * @Author: Shang
 * @Date: 2016/4/28  13:46
 */
public class DeviceInfo {
    private int number;
    private String deviceId;
    private LatLng mLatLng; //设备地理位置
    private boolean state;
    private boolean isCheck;

    public DeviceInfo(int number, String deviceId) {
        this.number = number;
        this.deviceId = deviceId;
        this.state = false;
    }

    public DeviceInfo(String deviceId, LatLng latLng) {
        this.deviceId = deviceId;
        mLatLng = latLng;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
