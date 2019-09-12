package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/5/15 0015 14:33
 * 钱包明细
 */
public class WalletDetailBean {

    //before_money 【变动前金额。类型：decimal(10,2)】
    //change_money 【操作金额。类型：decimal(10,2)】
    //after_money 【变动后金额。类型：decimal(10,2)】
    //change_type 【操作类型 1 充值 2打赏消费3红包收入4发红包退还。类型：int(11) unsigned】
    //remark 【备注。类型：varchar(500)】
    //create_time 【变动时间。类型：int(11) unsigned】
    private double before_bobi;
    private double change_bobi;
    private double after_bobi;
    private String change_type;
    private String remark;
    private String create_time;

    public double getBefore_bobi() {
        return before_bobi;
    }

    public void setBefore_bobi(double before_bobi) {
        this.before_bobi = before_bobi;
    }

    public double getChange_bobi() {
        return change_bobi;
    }

    public void setChange_bobi(double change_bobi) {
        this.change_bobi = change_bobi;
    }

    public double getAfter_bobi() {
        return after_bobi;
    }

    public void setAfter_bobi(double after_bobi) {
        this.after_bobi = after_bobi;
    }

    public String getChange_type() {
        return change_type;
    }

    public void setChange_type(String change_type) {
        this.change_type = change_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
