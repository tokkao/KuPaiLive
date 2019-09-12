package com.benben.kupaizhibo.utils;

import android.content.Context;

import com.benben.kupaizhibo.config.Constants;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

/**
 *
 * Author:zhn
 * Time:2019/5/24 0024 11:43
 *
 * 友盟相关常量 和 工具类
 *
 */
public class UMengHelper {
    //对应于服务后台的位置：应用管理 -> 应用信息 -> Appkey
    public static String APP_KEY="5ce6610b3fc195479c000f80";
    //渠道号
    public static String CHANNEL_ID="umeng";
    //设备类型
    public static int DEVICE_TYPE= UMConfigure.DEVICE_TYPE_PHONE;
    //对应于服务后台的位置：应用管理 -> 应用信息 -> Umeng Message Secret
    public static String MESSAGE_SECRET= "7dedaf30b53a85870cb2961426772d23";

    //友盟初始化
    public static void init(final Context context) {
        UMConfigure.init(context,
                UMengHelper.APP_KEY,
                UMengHelper.CHANNEL_ID,
                UMengHelper.DEVICE_TYPE,
                UMengHelper.MESSAGE_SECRET);

        UMConfigure.setLogEnabled(true);

        //友盟分享和三方登录
        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_SECRET);
        PlatformConfig.setQQZone("101735719", "6960023df695aba235d97bb25ac6d983");
        PlatformConfig.setSinaWeibo("11", "11", "http://sns.whalecloud.com");

//        //
//        UMShareAPI.init(context,UMengHelper.APP_KEY);
    }

}
