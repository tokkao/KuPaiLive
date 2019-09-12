package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/6/11 0011 15:35
 * 收益明细
 */
public class WalletEarningBean {

    /**
     * aid : 52155
     * uid : 40
     * before_money : 10.90
     * change_money : 0.15
     * after_money : 11.05
     * change_type : 7
     * create_time : 2019-06-05 16:54:57
     * change_type_name : "礼物分成"
     * order_no : GF20190605165457813403
     * create_time_text : 1970-01-01 08:33:39
     */

    private int aid;
    private int uid;
    private double before_money;
    private double change_money;
    private double after_money;
    private int change_type;
    private String change_type_name;
    private String create_time;
    private String order_no;
    private String create_time_text;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public double getBefore_money() {
        return before_money;
    }

    public void setBefore_money(double before_money) {
        this.before_money = before_money;
    }

    public double getChange_money() {
        return change_money;
    }

    public void setChange_money(double change_money) {
        this.change_money = change_money;
    }

    public double getAfter_money() {
        return after_money;
    }

    public void setAfter_money(double after_money) {
        this.after_money = after_money;
    }

    public int getChange_type() {
        return change_type;
    }

    public void setChange_type(int change_type) {
        this.change_type = change_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getChange_type_name() {
        return change_type_name;
    }

    public void setChange_type_name(String change_type_name) {
        this.change_type_name = change_type_name;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCreate_time_text() {
        return create_time_text;
    }

    public void setCreate_time_text(String create_time_text) {
        this.create_time_text = create_time_text;
    }
}
