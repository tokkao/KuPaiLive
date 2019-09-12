package com.benben.kupaizhibo.bean.socket;

/**
 * 透传消息包
 * create by zjn on 2019/5/16 0016
 * email:168455992@qq.com
 */
public class CTMessageBean {
    private String id;
    private String user_nickname;
    private String avatar;
    private String gift_id;
    private String gift_name;
    private String number;
    private String gift_thumb;
    private String red_envelopes_id;
    private String money;
    private String msg;
    private String user_id;
    private String to_user_id;
    private String type;
    private String stream;
    private String order_no;
    private String total_fee;
    private String zip_name;
    private String zip_addr;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getZip_name() {
        return zip_name;
    }

    public void setZip_name(String zip_name) {
        this.zip_name = zip_name;
    }

    public String getZip_addr() {
        return zip_addr;
    }

    public void setZip_addr(String zip_addr) {
        this.zip_addr = zip_addr;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRed_envelopes_id() {
        return red_envelopes_id;
    }

    public void setRed_envelopes_id(String red_envelopes_id) {
        this.red_envelopes_id = red_envelopes_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGift_thumb() {
        return gift_thumb;
    }

    public void setGift_thumb(String gift_thumb) {
        this.gift_thumb = gift_thumb;
    }

    @Override
    public String toString() {
        return "CTMessageBean{" +
                "id='" + id + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gift_id='" + gift_id + '\'' +
                ", gift_name='" + gift_name + '\'' +
                ", number='" + number + '\'' +
                ", gift_thumb='" + gift_thumb + '\'' +
                ", red_envelopes_id='" + red_envelopes_id + '\'' +
                ", money='" + money + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
