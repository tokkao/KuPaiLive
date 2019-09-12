package com.benben.kupaizhibo.bean.socket;

/**
 * Create by wanghk on 2019-05-21.
 * Describe: 赠送礼物的bean
 */
public class SendGiftBean {

    /**
     *{
     *     "user_bobi":"105230.09",
     *     "consumption":"1.00",
     *     "gift_token":"GIFT_7cf978123379aa0246145fa2244fa3ef",
     *     "level":1
     * }
     */

    private String user_bobi;
    private String consumption;
    private String gift_token;
    private int level;

    public String getUser_bobi() {
        return user_bobi;
    }

    public void setUser_bobi(String user_bobi) {
        this.user_bobi = user_bobi;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getGift_token() {
        return gift_token;
    }

    public void setGift_token(String gift_token) {
        this.gift_token = gift_token;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
