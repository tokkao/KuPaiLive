package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-30.
 * Describe:未读消息bean
 */
public class UnReadMsgCountBean {
    private int count;

    public UnReadMsgCountBean() {
    }

    public UnReadMsgCountBean(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
