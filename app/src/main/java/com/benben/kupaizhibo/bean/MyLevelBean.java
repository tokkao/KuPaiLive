package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-27.
 * Describe:我的等级
 */
public class MyLevelBean {

    /**
     * percentage : 3
     * count_exp : 10
     * now_exp : 0.00
     * user_level : 1
     * default_exp : 0
     */

    private int percentage;
    private int count_exp;
    private String now_exp;
    private int user_level;
    private int anchor_level;
    private int default_exp;

    public int getAnchor_level() {
        return anchor_level;
    }

    public void setAnchor_level(int anchor_level) {
        this.anchor_level = anchor_level;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getCount_exp() {
        return count_exp;
    }

    public void setCount_exp(int count_exp) {
        this.count_exp = count_exp;
    }

    public String getNow_exp() {
        return now_exp;
    }

    public void setNow_exp(String now_exp) {
        this.now_exp = now_exp;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getDefault_exp() {
        return default_exp;
    }

    public void setDefault_exp(int default_exp) {
        this.default_exp = default_exp;
    }
}
