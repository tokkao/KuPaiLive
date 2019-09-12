package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/7/9 0009 20:00
 * 关播统计信息
 */
public class StopLiveResultBean {


    /**
     * stream : 63_1562673541
     * start_time : 1562673541
     * end_time : 1562673590
     * nickname : 用户27976
     * sex : 1
     * avatar : http://live.zzqcnz.com/static/admin/images/default_avatar.jpg
     * thumb : http://live.zzqcnz.com/static/admin/images/default_avatar.jpg
     * total_fee : 2.00
     * nums : 1
     * title : 嘤嘤嘤
     * times : 00:49
     *
     * stream  直播码
     * start_time  直播开始时间
     * end_time  直播结束时间
     * nickname 主播昵称
     * sex 主播性别
     * avatar  主播头像
     * thumb直播封面
     * total_fee  总收益
     * nums 总人数
     * title  直播主题
     * times  直播有效时长
     */

    private String stream;
    private int start_time;
    private int end_time;
    private String nickname;
    private int sex;
    private String avatar;
    private String thumb;
    private String total_fee;
    private int nums;
    private String title;
    private String times;
    private int fans_number;

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getFans_number() {
        return fans_number;
    }

    public void setFans_number(int fans_number) {
        this.fans_number = fans_number;
    }
}
