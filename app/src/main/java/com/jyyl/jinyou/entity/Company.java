package com.jyyl.jinyou.entity;

/**
 * @Fuction: 公司信息
 * @Author: Shang
 * @Date: 2016/6/13  11:16
 */
public class Company {
    private String companyId;//公司id
    private String companyName;//公司名称
    private String companyAddress;//公司地址
    private String companyPhone;//公司电话
    private String companyTD;//二维码
    private String companySign;//标识
    private String companyNote;//备注

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyTD() {
        return companyTD;
    }

    public void setCompanyTD(String companyTD) {
        this.companyTD = companyTD;
    }

    public String getCompanySign() {
        return companySign;
    }

    public void setCompanySign(String companySign) {
        this.companySign = companySign;
    }

    public String getCompanyNote() {
        return companyNote;
    }

    public void setCompanyNote(String companyNote) {
        this.companyNote = companyNote;
    }
}
