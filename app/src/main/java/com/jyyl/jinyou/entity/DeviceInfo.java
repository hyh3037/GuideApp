package com.jyyl.jinyou.entity;

/**
 * @Fuction: 绑定设备信息
 * @Author: Shang
 * @Date: 2016/4/28  13:46
 */
public class DeviceInfo {
    private int number;
    private DeviceResult deviceResult;
    private boolean isCheck;

    public DeviceInfo(int number, DeviceResult deviceResult) {
        this.number = number;
        this.deviceResult = deviceResult;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public DeviceResult getDeviceResult() {
        return deviceResult;
    }

    public void setDeviceResult(DeviceResult deviceResult) {
        this.deviceResult = deviceResult;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
