package com.jyyl.guideapp.bean;

/**
 * @Fuction: 绑定设备信息
 * @Author: Shang
 * @Date: 2016/4/28  13:46
 */
public class DeviceInfo {
    private int number;
    private String deviceId;

    public DeviceInfo(int number, String deviceId) {
        this.number = number;
        this.deviceId = deviceId;
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
}
