package com.jyyl.guideapp.entity;

/**
 * @Fuction: 更新版本信息
 * @Author: Shang
 * @Date: 2016/5/26  11:59
 */
public class VersionInfo {
    private String checkGuid;       //版本更新主键
    private String versionValue;    //版本号
    private String updatetime;      //版本发布时间
    private String versioninfo;     //版本信息
    private String versionUrl;      //版本更新地址
    private String versiontype;     //版本类型
    private String enterpriseCode;  //企业代号
    private String versionNote;     //备注（版本类型说明）

    public String getCheckGuid() {
        return checkGuid;
    }

    public void setCheckGuid(String checkGuid) {
        this.checkGuid = checkGuid;
    }

    public String getVersionValue() {
        return versionValue;
    }

    public void setVersionValue(String versionValue) {
        this.versionValue = versionValue;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getVersioninfo() {
        return versioninfo;
    }

    public void setVersioninfo(String versioninfo) {
        this.versioninfo = versioninfo;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getVersiontype() {
        return versiontype;
    }

    public void setVersiontype(String versiontype) {
        this.versiontype = versiontype;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getVersionNote() {
        return versionNote;
    }

    public void setVersionNote(String versionNote) {
        this.versionNote = versionNote;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "checkGuid='" + checkGuid + '\'' +
                ", versionValue='" + versionValue + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", versioninfo='" + versioninfo + '\'' +
                ", versionUrl='" + versionUrl + '\'' +
                ", versiontype='" + versiontype + '\'' +
                ", enterpriseCode='" + enterpriseCode + '\'' +
                ", versionNote='" + versionNote + '\'' +
                '}';
    }
}
