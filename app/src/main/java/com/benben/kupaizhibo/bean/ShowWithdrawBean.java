package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/5/31 0031 11:04
 */
public class ShowWithdrawBean {


    /**
     * {
     *         "user_money": "5000.00",//账户余额
     *         "total_revenue_money": "0.00",//总收入
     *         "user_bobi": "0.00"//糖豆
     *     }
     */

    private double user_money;
    private double total_revenue_money;
    private double user_bobi;

    public double getUser_money() {
        return user_money;
    }

    public void setUser_money(double user_money) {
        this.user_money = user_money;
    }

    public double getTotal_revenue_money() {
        return total_revenue_money;
    }

    public void setTotal_revenue_money(double total_revenue_money) {
        this.total_revenue_money = total_revenue_money;
    }

    public double getUser_bobi() {
        return user_bobi;
    }

    public void setUser_bobi(double user_bobi) {
        this.user_bobi = user_bobi;
    }
}
