package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/6/12 0012 8:43
 * 配置信息
 */
public class ConfigBean {

    /**
     *  {
     *      "app_version": "6",
     *      "app_download": "http://live.zzqcnz.com/data/",
     *      "apple_version": "1.0.1",
     *      "apple_download": "http://live.zzqcnz.com/data/"
     *  }
     */
    private String app_version;
    private String app_download;

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getApp_download() {
        return app_download;
    }

    public void setApp_download(String app_download) {
        this.app_download = app_download;
    }
}
