package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: bean
 */
public class UserHomePageAnchorListBean {

    private int anchor_id;
    private String name;
    private String avatar;

    private String signature;
    private int follow_status;
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public int getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(int follow_status) {
        this.follow_status = follow_status;
    }

    public int getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(int anchor_id) {
        this.anchor_id = anchor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
