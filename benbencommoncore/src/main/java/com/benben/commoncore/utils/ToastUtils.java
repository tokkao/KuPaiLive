package com.benben.commoncore.utils;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/23.
 *
 * 自定义吐司 布局
 *
 * 静态toast 只创建一个toast实例 可以实时显示弹出的内容
 */

public class ToastUtils {
    private static Toast textToast;

    /**
     * 纯文字
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
        if( context == null){
            return ;
        }
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    /**
     * 纯文字
     *
     * @param context
     * @param resId
     */
    public static void show(Context context, int resId) {
        if( context == null){
            return ;
        }
        Toast.makeText(context,resId,Toast.LENGTH_SHORT).show();
    }
}
