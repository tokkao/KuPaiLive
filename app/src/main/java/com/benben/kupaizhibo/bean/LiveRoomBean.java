package com.benben.kupaizhibo.bean;

import java.io.Serializable;

/**
 * 功能:直播间信息对象
 * create by zjn on 2019/5/14 0014
 * email:168455992@qq.com
 */
public class LiveRoomBean implements Serializable {
    /**
     * view_num : 0
     * live_type : 1
     * nickname : 人生若只如初见
     * avatar : http://live.zzqcnz.com/uploads/user_avatar/20190525/14503144697.jpg
     * cdn_name : cloud
     * earnings : 0
     * link_mic : 0
     * app_name : live
     * extra :
     * thumb : http://live.zzqcnz.com/uploads/images/20190528/102038259995.jpg
     * socket_handle : {"roomnum":32,"token":"Z20190528102038618861","stream":"32_1559009648","uid":32}
     * title : 123
     * user_type :
     * stream : 32_1559009648
     * socket_url : http://122.114.74.51:20000
     * rtmp : rtmp://livepush.yuanjiwei.cn/live/32_1559009648?txSecret=27432521e548f3dcb398876fd9f8f3ef&txTime=5CEDEAF0
     * activity_id : 0
     * votes_total : 0.00
     * user_id : 32
     * follow_num : 2
     */

    private int view_num;
    private int live_type;
    private String nickname;
    private String avatar;
    private String cdn_name;
    private int earnings;
    private int link_mic;
    private String app_name;
    private String extra;
    private String thumb;
    private SocketHandleBean socket_handle;
    private String title;
    private String user_type;
    private String stream;
    private String socket_url;
    private String rtmp;
    private int activity_id;
    private String votes_total;
    private int user_id;
    private int follow_num;

    //补充字段？
    private String u_type;
    private String city;
    private String is_follow;
    private String nums;
    private String pull_mic;
    private PullStreamAddressBean pull;
    private String active_id;



    public int getView_num() {
        return view_num;
    }

    public void setView_num(int view_num) {
        this.view_num = view_num;
    }

    public int getLive_type() {
        return live_type;
    }

    public void setLive_type(int live_type) {
        this.live_type = live_type;
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

    public String getCdn_name() {
        return cdn_name;
    }

    public void setCdn_name(String cdn_name) {
        this.cdn_name = cdn_name;
    }

    public int getEarnings() {
        return earnings;
    }

    public void setEarnings(int earnings) {
        this.earnings = earnings;
    }

    public int getLink_mic() {
        return link_mic;
    }

    public void setLink_mic(int link_mic) {
        this.link_mic = link_mic;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public SocketHandleBean getSocket_handle() {
        return socket_handle;
    }

    public void setSocket_handle(SocketHandleBean socket_handle) {
        this.socket_handle = socket_handle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getSocket_url() {
        return socket_url;
    }

    public void setSocket_url(String socket_url) {
        this.socket_url = socket_url;
    }

    public String getRtmp() {
        return rtmp;
    }

    public void setRtmp(String rtmp) {
        this.rtmp = rtmp;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getVotes_total() {
        return votes_total;
    }

    public void setVotes_total(String votes_total) {
        this.votes_total = votes_total;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public String getU_type() {
        return u_type;
    }

    public void setU_type(String u_type) {
        this.u_type = u_type;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getPull_mic() {
        return pull_mic;
    }

    public void setPull_mic(String pull_mic) {
        this.pull_mic = pull_mic;
    }

    public PullStreamAddressBean getPull() {
        return pull;
    }

    public void setPull(PullStreamAddressBean pull) {
        this.pull = pull;
    }

    public String getActive_id() {
        return active_id;
    }

    public void setActive_id(String active_id) {
        this.active_id = active_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
