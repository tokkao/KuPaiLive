package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/6/1 0001 14:43
 *
 * 首页-搜索-按字段搜索结果
 *
 */
public class SearchRankingBean {

    /**
     * user_id : 3
     * stream : 3_1557105130
     * nickname : 星辰2
     * avatar : http://192.168.2.134:105/uploads/images/20190508/93442d6ec08e5dba4557acf0934fb0ee.jpg
     * thumb : 626
     * title : 星辰2
     * money : 0
     * live_status : 1
     * count_order : 0
     */

    private int user_id;
    private String stream;
    private String nickname;
    private String avatar;
    private String thumb;
    private String title;
    private int money;
    private int live_status;
    private int count_order;

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getLive_status() {
        return live_status;
    }

    public void setLive_status(int live_status) {
        this.live_status = live_status;
    }

    public int getCount_order() {
        return count_order;
    }

    public void setCount_order(int count_order) {
        this.count_order = count_order;
    }

    public LiveInfoBean convertLiveInfo() {
        LiveInfoBean bean=new LiveInfoBean();
        bean.setAvatar(avatar);
        bean.setNickname(nickname);
        bean.setStream(stream);
        bean.setThumb(thumb);
        bean.setTitle(title);
        bean.setUser_id(user_id+"");
        bean.setLive_status(live_status+"");
//        bean.setSex();
        return bean;
    }
}
