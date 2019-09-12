package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-20.
 * Describe:我的分享
 */
public class MyShareListBean {


    /**
     * time : 2019-07-20 16:07:20
     * id : 2
     * uid : 33
     * type_name : 微信好友
     * type : 1
     */

    private String time;
    private int id;
    private int uid;
    private String type_name;
    private int type;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
