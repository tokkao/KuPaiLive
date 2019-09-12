package com.benben.kupaizhibo.bean;

/**
 * 功能:推流地址信息
 * create by zjn on 2019/5/14 0014
 * email:168455992@qq.com
 */
public class PullStreamAddressBean {

    /**
     * cdn : cloud
     * app_name : live
     * rtmp : rtmp://liveplay.yuanjiwei.cn/live/2_1557105130?txSecret=7fa3bb7e7f699c9fc4dc414b7cd982dd&txTime=5CDBE9B2
     * flv : http://liveplay.yuanjiwei.cn/live/2_1557105130.flv?txSecret=7fa3bb7e7f699c9fc4dc414b7cd982dd&txTime=5CDBE9B2
     * m3u8 : http://liveplay.yuanjiwei.cn/live/2_1557105130.m3u8?txSecret=7fa3bb7e7f699c9fc4dc414b7cd982dd&txTime=5CDBE9B2
     */

    private String cdn;
    private String app_name;
    private String rtmp;
    private String flv;
    private String m3u8;

    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getRtmp() {
        return rtmp;
    }

    public void setRtmp(String rtmp) {
        this.rtmp = rtmp;
    }

    public String getFlv() {
        return flv;
    }

    public void setFlv(String flv) {
        this.flv = flv;
    }

    public String getM3u8() {
        return m3u8;
    }

    public void setM3u8(String m3u8) {
        this.m3u8 = m3u8;
    }
}
