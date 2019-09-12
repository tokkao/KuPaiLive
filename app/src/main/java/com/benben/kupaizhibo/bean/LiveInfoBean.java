package com.benben.kupaizhibo.bean;

import java.io.Serializable;

/**
 * 直播信息对象
 */
public class LiveInfoBean implements Serializable {

    /**
     * user_id : 1
     * stream : 1_1557457049
     * title : 星辰
     * nickname : 星辰
     * avatar : http://192.168.2.134:105/uploads/images/16461303439.jpg
     * thumb : http://192.168.2.134:105/uploads/images/16461303439.jpg
     * live_status : 1
     * sex : 2
     */
    private String user_id;
    private String stream;
    private String title;
    private String nickname;
    private String avatar;
    private String thumb;
    private String live_status;
    private String sex;
    private String number;
    private String passwd;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLive_status() {
        return live_status;
    }

    public void setLive_status(String live_status) {
        this.live_status = live_status;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
