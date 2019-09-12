package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 充值钻石数量列表bean
 */
public class RechargeNumbersListBean {

    private int money;
    private int diamond_num;
    private int  id;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getDiamond_num() {
        return diamond_num;
    }

    public void setDiamond_num(int diamond_num) {
        this.diamond_num = diamond_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
