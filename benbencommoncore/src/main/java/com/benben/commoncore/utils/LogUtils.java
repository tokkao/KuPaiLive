package com.benben.commoncore.utils;

import android.util.Log;

/**
 * Description: log日志的
 *
 * @author zjn
 * Email：168455992@qq.com
 * @date 2019/1/15
 */
public class LogUtils {

    private static boolean isShowLog = true;

    /**
     * 常用的log打印控制类
     * @param tag
     * @param msg
     */
    public static void e(String tag,String msg){

        if(isShowLog) {
            Log.e(tag, msg+"");
        }
    }
}
