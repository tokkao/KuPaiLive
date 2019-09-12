package com.benben.kupaizhibo.bean.socket;

/**
 * Create by wanghk on 2019-05-22.
 * Describe:抢红包bean
 */
public class GrabRedPacketBean {

    /**
     * is_end : 0
     * money : 2.80
     * after_money : 7394.21
     */

    private int is_end;
    private String money;
    private String after_money;

    public int getIs_end() {
        return is_end;
    }

    public void setIs_end(int is_end) {
        this.is_end = is_end;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAfter_money() {
        return after_money;
    }

    public void setAfter_money(String after_money) {
        this.after_money = after_money;
    }
}
