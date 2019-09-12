package com.benben.kupaizhibo.config;

import android.os.Environment;

/**
 * SD卡存储路径
 * created by zjn on 2018/9/26
 * email 168455992@qq.com
 */
public class SystemDir {

    /** SD卡根目录 */
    public static final String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**不随程序卸载的文件夹 */
    public static final String DIR_LONG_CACHE = ROOTPATH + "/com.benben.kupaizhibo";

    /** 更新文件 */
    public static final String DIR_UPDATE_APK = DIR_LONG_CACHE+"/update_file";
    /** 错误日志 */
    public static final String DIR_ERROR_MSG = DIR_LONG_CACHE+"/error_file";
    /** 缓存图片 */
    public static final String DIR_IMAGE = DIR_LONG_CACHE+"/image_file";
    /** 礼物缓存文件 */
    public static final String DIR_GIFT_FILE = DIR_LONG_CACHE+"/gift_image_file";

}
