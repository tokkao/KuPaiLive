package com.benben.kupaizhibo.bean;

import java.io.Serializable;

/**
 * socket处理对象
 * create by zjn on 2019/5/14 0014
 * email:168455992@qq.com
 */
public class SocketHandleBean implements Serializable {
    /**
     * uid : 37
     * roomnum : 37
     * stream : 37_1557797622
     * token : Z20190514093342708935
     */

    private int uid;
    private int roomnum;
    private String stream;
    private String token;
    private String nickname="zhangsan";

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(int roomnum) {
        this.roomnum = roomnum;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
