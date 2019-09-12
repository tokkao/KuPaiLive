package com.benben.kupaizhibo.bean.socket;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 功能:长连接的消息体对象
 * create by zjn on 2019/5/15 0015
 * email:168455992@qq.com
 */
public class SocketResponseBodyBean implements Serializable {
    //消息说明
    @JSONField(name = "_method_")
    private String _method_;
    //Action和msgtype  为消息 进行了进一步的二级分拣
    private String action;
    private String msgtype;
    //聊天内容
    private String ct;
    private String uid;
    private String uname;
    private String level;
    private String user_level;
    private String timestamp;
    //选填 1 为点亮
    private String heart;
    private String uhead;
    private String ct2;

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getCt2() {
        return ct2;
    }

    public void setCt2(String ct2) {
        this.ct2 = ct2;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUhead() {
        return uhead;
    }

    public void setUhead(String uhead) {
        this.uhead = uhead;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }

    public String get_method_() {
        return _method_;
    }

    public void set_method_(String _method_) {
        this._method_ = _method_;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    @Override
    public String toString() {
        return "SocketResponseBodyBean{" +
                "_method_='" + _method_ + '\'' +
                ", action='" + action + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", ct='" + ct + '\'' +
                ", uid='" + uid + '\'' +
                ", uname='" + uname + '\'' +
                ", level='" + level + '\'' +
                ", heart='" + heart + '\'' +
                ", uhead='" + uhead + '\'' +
                '}';
    }
}
