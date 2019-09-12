package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/5/22 0022 19:05
 *
 * 我的钱包-获取糖豆，总收益
 */
public class WalletMoneyBean {

    /**
     * user_money : 36.45
     * user_bobi : 9907190.00
     * votes_total : 36.45
     * user_integral : 0
     * count_integral : 0
     * total_consumption_money : 0.00
     * total_revenue_money : 0.00
     * total_consumption_bobi : 102810.00
     */
    private double user_money;//会员余额
    private double user_bobi;//剩余糖豆
    private double votes_total;//打赏总收入
    private int user_integral;//用户积分
    private int count_integral;//总积分
    private double total_consumption_money;//总消费金额
    private double total_revenue_money;//总收益金额
    private double total_consumption_bobi;//总消费糖豆

    public double getUser_money() {
        return user_money;
    }

    public void setUser_money(double user_money) {
        this.user_money = user_money;
    }

    public double getUser_bobi() {
        return user_bobi;
    }

    public void setUser_bobi(double user_bobi) {
        this.user_bobi = user_bobi;
    }

    public double getVotes_total() {
        return votes_total;
    }

    public void setVotes_total(double votes_total) {
        this.votes_total = votes_total;
    }

    public int getUser_integral() {
        return user_integral;
    }

    public void setUser_integral(int user_integral) {
        this.user_integral = user_integral;
    }

    public int getCount_integral() {
        return count_integral;
    }

    public void setCount_integral(int count_integral) {
        this.count_integral = count_integral;
    }

    public double getTotal_consumption_money() {
        return total_consumption_money;
    }

    public void setTotal_consumption_money(double total_consumption_money) {
        this.total_consumption_money = total_consumption_money;
    }

    public double getTotal_revenue_money() {
        return total_revenue_money;
    }

    public void setTotal_revenue_money(double total_revenue_money) {
        this.total_revenue_money = total_revenue_money;
    }

    public double getTotal_consumption_bobi() {
        return total_consumption_bobi;
    }

    public void setTotal_consumption_bobi(double total_consumption_bobi) {
        this.total_consumption_bobi = total_consumption_bobi;
    }
}
