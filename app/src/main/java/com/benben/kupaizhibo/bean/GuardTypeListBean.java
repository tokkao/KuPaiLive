package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 守护坐席bean
 */
public class GuardTypeListBean {

    private boolean isSelect;
    private String guard_name;
    private String guard_price;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getGuard_name() {
        return guard_name;
    }

    public void setGuard_name(String guard_name) {
        this.guard_name = guard_name;
    }

    public String getGuard_price() {
        return guard_price;
    }

    public void setGuard_price(String guard_price) {
        this.guard_price = guard_price;
    }
}
