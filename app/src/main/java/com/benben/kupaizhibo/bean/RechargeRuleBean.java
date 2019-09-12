package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/5/31 0031 17:19
 *
 * 充值规则
 */
public class RechargeRuleBean {

    /**
     * name : 60糖豆
     * id : 2
     * money : 0.01
     * add_money : 60.00
     * app_name :
     */
    private String name;
    private int id;
    private String money;
    private String add_money;
    private String app_name;

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAdd_money() {
        return add_money;
    }

    public void setAdd_money(String add_money) {
        this.add_money = add_money;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }
}
