package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-26.
 * Describe:
 */
public class SearchLiveListBean {


    /**
     * user_id : 42
     * stream : 42_1559553991
     * nickname : c
     * avatar : http://live.zzqcnz.com/uploads/images/20190530/114152395494.jpg
     * title : 2580
     * thumb : 1167
     * money : 123
     * nums : 0
     */

    private int user_id;
    private String stream;
    private String nickname;
    private String avatar;
    private String title;
    private String thumb;
    private int money;
    private int nums;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public LiveInfoBean convertLiveInfo() {
        LiveInfoBean bean=new LiveInfoBean();
        bean.setAvatar(avatar);
        bean.setNickname(nickname);
        bean.setStream(stream);
        bean.setThumb(thumb);
        bean.setTitle(title);
        bean.setUser_id(user_id+"");
        bean.setLive_status("");
//        bean.setSex();
        return bean;
    }
}
