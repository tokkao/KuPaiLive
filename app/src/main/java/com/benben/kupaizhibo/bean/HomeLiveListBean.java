package com.benben.kupaizhibo.bean;

import java.io.Serializable;

/**
 * Created by: wanghk 2019-07-02.
 * Describe: 首页直播列表
 */
public class HomeLiveListBean implements Serializable {

    private String room_cover;
    private String room_title;
    private String anchor_name;
    private String anchor_avatar;
    private String viewer_numbers;
    private String room_id;

    public String getRoom_cover() {
        return room_cover;
    }

    public void setRoom_cover(String room_cover) {
        this.room_cover = room_cover;
    }

    public String getRoom_title() {
        return room_title;
    }

    public void setRoom_title(String room_title) {
        this.room_title = room_title;
    }

    public String getAnchor_name() {
        return anchor_name;
    }

    public void setAnchor_name(String anchor_name) {
        this.anchor_name = anchor_name;
    }

    public String getAnchor_avatar() {
        return anchor_avatar;
    }

    public void setAnchor_avatar(String anchor_avatar) {
        this.anchor_avatar = anchor_avatar;
    }

    public String getViewer_numbers() {
        return viewer_numbers;
    }

    public void setViewer_numbers(String viewer_numbers) {
        this.viewer_numbers = viewer_numbers;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
