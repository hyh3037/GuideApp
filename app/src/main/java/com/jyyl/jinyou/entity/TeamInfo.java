package com.jyyl.jinyou.entity;

/**
 * @Fuction: 团队信息
 * @Author: Shang
 * @Date: 2016/6/8  11:04
 */
public class TeamInfo {
    private String teamId;//团id
    private String teamName;//团名称
    private String teamNumbers;//团人数
    private String createTime;//创建时间
    private String createMemberId;//创建人id
    private String createMemberName;//创建人姓名
    private String teamDate;//团期
    private String deleteState;//删除状态
    private String industry;//行业标识

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamNumbers() {
        return teamNumbers;
    }

    public void setTeamNumbers(String teamNumbers) {
        this.teamNumbers = teamNumbers;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateMemberId() {
        return createMemberId;
    }

    public void setCreateMemberId(String createMemberId) {
        this.createMemberId = createMemberId;
    }

    public String getCreateMemberName() {
        return createMemberName;
    }

    public void setCreateMemberName(String createMemberName) {
        this.createMemberName = createMemberName;
    }

    public String getTeamDate() {
        return teamDate;
    }

    public void setTeamDate(String teamDate) {
        this.teamDate = teamDate;
    }

    public String getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(String deleteState) {
        this.deleteState = deleteState;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
