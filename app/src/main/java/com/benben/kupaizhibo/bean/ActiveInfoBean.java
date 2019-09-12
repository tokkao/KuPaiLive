package com.benben.kupaizhibo.bean;

/**
 * 活动信息类
 */
public class ActiveInfoBean {

    /**
     * id : 1
     * picture : 10
     * title : 活动名称1
     * synopsis : 活动简介
     */

    private int id;
    private String picture;
    private String title;
    private String synopsis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
