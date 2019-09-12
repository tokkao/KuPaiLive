package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-01.
 * Describe:登录结果bean
 */
public class UserLoginSuccessBean {

    /**
     * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJMd3dhblBIUCIsImlhdCI6MTU1NzQ2MDgxOSwibmJmIjoxNTU3NDYwODE5LCJzY29wZXMiOiJyb2xlX2FjY2VzcyIsImV4cCI6MTU1ODA2NTYxOSwicGFyYW1zIjp7ImlkIjozMiwibmlja25hbWUiOiJcdTc1MjhcdTYyMzcyMjQ3NCIsInN0YXR1cyI6MSwiYXZhdGFyIjoiIiwic2V4IjowLCJ1c2VyX3R5cGUiOjAsInVzZXJfbGV2ZWwiOjAsIm1vYmlsZSI6IjE1NTAxMjk2ODA4IiwidXVpZCI6IjE5YzE2MmFjODhhMzZiNTFjODQxMGU3MjRhMzViMDQ0In19.hh5aTrUMZVhCFoYQ9bROSTikqK-N2hDlVmiCbKa7WTU
     * refresh_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJMd3dhblBIUCIsImlhdCI6MTU1NzQ2MDgxOSwibmJmIjoxNTU3NDYwODE5LCJzY29wZXMiOiJyb2xlX3JlZnJlc2giLCJleHAiOjE1NjAwNTI4MTksInBhcmFtcyI6eyJpZCI6MzIsIm5pY2tuYW1lIjoiXHU3NTI4XHU2MjM3MjI0NzQiLCJzdGF0dXMiOjEsImF2YXRhciI6IiIsInNleCI6MCwidXNlcl90eXBlIjowLCJ1c2VyX2xldmVsIjowLCJtb2JpbGUiOiIxNTUwMTI5NjgwOCIsInV1aWQiOiIxOWMxNjJhYzg4YTM2YjUxYzg0MTBlNzI0YTM1YjA0NCJ9fQ.TBb2wYWUcN7Y9jDQYDaKAT-h91FAj3L33mkp4yTFmc0
     * token_type : bearer
     * id : 32
     * nickname : 用户22474
     * avatar :
     * user_type : 0
     * user_level : 0
     * mobile : 155****6808
     * easemob : c18f1cfe581845a9f6c995784d4d4700
     */
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int id;
    private String nickname;
    private String avatar;
    //0:普通 1：主播 4：机构
    private int user_type;
    private int user_level;
    private String mobile;
    private String easemob;//环信密码


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEasemob() {
        return easemob;
    }

    public void setEasemob(String easemob) {
        this.easemob = easemob;
    }
}
