package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/5/24 0024 9:36
 * 消息列表实体类
 */
public class EasemobUserBean {

    private boolean isSystemMsg;//是否是系统消息
    private String eid;//环信账号
    private String eNickName;//环信用户昵称
    private String eAvatar;//环信用户头像
    private int eUnreadMsgCount;//未读消息数
    private String eLastMsg;//最新一条消息
    private String eLastMsgTime;//最新一条消息时间

    public boolean isSystemMsg() {
        return isSystemMsg;
    }

    public void setSystemMsg(boolean systemMsg) {
        isSystemMsg = systemMsg;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String geteNickName() {
        return eNickName;
    }

    public void seteNickName(String eNickName) {
        this.eNickName = eNickName;
    }

    public String geteAvatar() {
        return eAvatar;
    }

    public void seteAvatar(String eAvatar) {
        this.eAvatar = eAvatar;
    }

    public int geteUnreadMsgCount() {
        return eUnreadMsgCount;
    }

    public void seteUnreadMsgCount(int eUnreadMsgCount) {
        this.eUnreadMsgCount = eUnreadMsgCount;
    }

    public String geteLastMsg() {
        return eLastMsg;
    }

    public void seteLastMsg(String eLastMsg) {
        this.eLastMsg = eLastMsg;
    }

    public String geteLastMsgTime() {
        return eLastMsgTime;
    }

    public void seteLastMsgTime(String eLastMsgTime) {
        this.eLastMsgTime = eLastMsgTime;
    }
}
