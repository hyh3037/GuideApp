package com.jyyl.jinyou.entity;

/**
 * @Fuction:  服务器返回的游客信息
 * @Author: Shang
 * @Date: 2016/6/8  17:09
 */
public class MemberInfoResult {
    private String touristId;//游客id
    private String touristName;//游客名称
    private String touristPhone;//游客电话
    private String touristIdCard;//游客身份证
    private String touristContentName;//紧急联系人姓名
    private String touristContentPhone;//紧急联系人电话
    private String headAddress;//头像id
    private String touristNote;//备注
    private String industry;//行业标识
    private String teamId;//团id
    private String deviceId;//设备id
    private String devicePhone;//设备电话
    private String bindStartTime;//绑定开始时间
    private String bindEndTime;//绑定结束时间
    private String deleteSate;//删除状态
    private String teamName;//团名
    private String teamDate;//团期

    public String getTouristId() {
        return touristId;
    }

    public void setTouristId(String touristId) {
        this.touristId = touristId;
    }

    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public String getTouristPhone() {
        return touristPhone;
    }

    public void setTouristPhone(String touristPhone) {
        this.touristPhone = touristPhone;
    }

    public String getTouristIdCard() {
        return touristIdCard;
    }

    public void setTouristIdCard(String touristIdCard) {
        this.touristIdCard = touristIdCard;
    }

    public String getTouristContentName() {
        return touristContentName;
    }

    public void setTouristContentName(String touristContentName) {
        this.touristContentName = touristContentName;
    }

    public String getTouristContentPhone() {
        return touristContentPhone;
    }

    public void setTouristContentPhone(String touristContentPhone) {
        this.touristContentPhone = touristContentPhone;
    }

    public String getHeadAddress() {
        return headAddress;
    }

    public void setHeadAddress(String headAddress) {
        this.headAddress = headAddress;
    }

    public String getTouristNote() {
        return touristNote;
    }

    public void setTouristNote(String touristNote) {
        this.touristNote = touristNote;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevicePhone() {
        return devicePhone;
    }

    public void setDevicePhone(String devicePhone) {
        this.devicePhone = devicePhone;
    }

    public String getBindStartTime() {
        return bindStartTime;
    }

    public void setBindStartTime(String bindStartTime) {
        this.bindStartTime = bindStartTime;
    }

    public String getBindEndTime() {
        return bindEndTime;
    }

    public void setBindEndTime(String bindEndTime) {
        this.bindEndTime = bindEndTime;
    }

    public String getDeleteSate() {
        return deleteSate;
    }

    public void setDeleteSate(String deleteSate) {
        this.deleteSate = deleteSate;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDate() {
        return teamDate;
    }

    public void setTeamDate(String teamDate) {
        this.teamDate = teamDate;
    }
}
