package com.benben.kupaizhibo.bean.socket;

/**
 * Create by wanghk on 2019-05-22.
 * Describe: 创建红包
 */
public class SendRedPacketTokenBean {

    /**
     * after_money : 100.00
     * red_envelopes_token : RE_9fee7ec863a38556092d98929a138532
     */

    private String after_money;
    private String red_envelopes_token;

    public String getAfter_money() {
        return after_money;
    }

    public void setAfter_money(String after_money) {
        this.after_money = after_money;
    }

    public String getRed_envelopes_token() {
        return red_envelopes_token;
    }

    public void setRed_envelopes_token(String red_envelopes_token) {
        this.red_envelopes_token = red_envelopes_token;
    }
}
