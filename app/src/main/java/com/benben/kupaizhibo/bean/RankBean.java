package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/5/14 0014 15:20
 */
public class RankBean {

    /**
     * user_id : 23
     * nickname : 特普朗莫
     * sex : 1
     * avatar : http://192.168.2.134:105/uploads/images/093642327837.jpg
     * level : 1
     * level_anchor : 8
     * user_level : 0
     * score : 6853
     */
    private int user_id;//会员ID
    private String nickname;//会员昵称
    private int sex;
    private String avatar;//会员头像
    private int level;//会员消费等级
    private int level_anchor;//会员的收入等级
    private int user_level;//会员的VIP值
    private int score;//会员的贡献

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel_anchor() {
        return level_anchor;
    }

    public void setLevel_anchor(int level_anchor) {
        this.level_anchor = level_anchor;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
