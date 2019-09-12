package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 守护坐席bean
 */
public class GuardSeatListBean {


    /**
     * sign : Z20190724142410378396
     * id : 33
     * userType : 0
     * avatar : http://bjzb.hncjne.com/uploads/images/20190722/182441704212.jpg
     * user_nickname : 我裂开了
     * guard_type : 1
     */

    private String sign;
    private int id;
    private int userType;
    private String avatar;
    private String user_nickname;
    private String guard_type;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getGuard_type() {
        return guard_type;
    }

    public void setGuard_type(String guard_type) {
        this.guard_type = guard_type;
    }
}
