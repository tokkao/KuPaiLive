package com.benben.commoncore.utils;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.benben.commoncore.R;
import com.kongzue.dialog.v3.WaitDialog;


/**
 * 通用进度，对话框封装工具类
 * create by zjn on 2019/5/15 0015
 * email:168455992@qq.com
 */
public class StyledDialogUtils {

    private static final String TAG = "StyledDialogUtils";
    private boolean isShowing = false;
    private static StyledDialogUtils mInstance;

    public static StyledDialogUtils getInstance() {
        if (mInstance == null) {
            mInstance = new StyledDialogUtils();
        }
        return mInstance;
    }

    private StyledDialogUtils() {

    }

    /*开启进度条*/
    public void loading(final Activity activity) {
        /*if (isShowing) {
            return;
        }*/
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WaitDialog.show((AppCompatActivity) activity, activity.getResources().getString(R.string.loading)).setCancelable(true);

            }
        });
        //isShowing = true;
    }

    /*关闭进度条*/
    public void dismissLoading() {

        WaitDialog.dismiss();

    }
}
