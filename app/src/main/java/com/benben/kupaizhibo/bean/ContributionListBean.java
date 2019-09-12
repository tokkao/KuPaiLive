package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 贡献榜bean
 */
public class ContributionListBean {

    private int ranking;
    private String user_name;
    private String user_avatar;
    private int  contribution_value;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public int getContribution_value() {
        return contribution_value;
    }

    public void setContribution_value(int contribution_value) {
        this.contribution_value = contribution_value;
    }
}
