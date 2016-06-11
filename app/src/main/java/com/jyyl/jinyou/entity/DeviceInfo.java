package com.jyyl.jinyou.entity;

/**
 * @Fuction: 绑定设备信息
 * @Author: Shang
 * @Date: 2016/4/28  13:46
 */
public class DeviceInfo {
    private int number;
    private DeviceInfoResult mDeviceInfoResult;
    private boolean isCheck;

    public DeviceInfo(int number, DeviceInfoResult deviceInfoResult) {
        this.number = number;
        this.mDeviceInfoResult = deviceInfoResult;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public DeviceInfoResult getDeviceInfoResult() {
        return mDeviceInfoResult;
    }

    public void setDeviceInfoResult(DeviceInfoResult deviceInfoResult) {
        this.mDeviceInfoResult = deviceInfoResult;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
