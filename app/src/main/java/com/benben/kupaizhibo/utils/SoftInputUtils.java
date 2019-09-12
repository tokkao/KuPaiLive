package com.benben.kupaizhibo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

/**
 * Created by Administrator on 2019/4/7.
 * 软键盘的显示和隐藏工具类
 */

public class SoftInputUtils {

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     * <p>
     * viewList 中需要放的是当前界面所有触发软键盘弹出的控件。
     * 比如一个登陆界面， 有一个账号输入框和一个密码输入框，
     * 需要隐藏键盘的时候， 就将两个输入框对象放在 viewList 中，
     * 作为参数传到 hideSoftKeyboard 方法中即可。
     */
    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //显示软键盘
    public static boolean showSystemKeyBord(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 如果键盘弹起则隐藏
     *
     * @param token
     */
    public static void hideKeyboard(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
