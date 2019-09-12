package com.benben.kupaizhibo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.benben.commoncore.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author:zhn
 * Time:2019/5/20 0020 16:37
 * <p>
 * 获取验证码按钮
 */
public class VerifyCodeButton extends AppCompatButton {
    private static final String TAG = "VerifyCodeButton";

    /**
     * 倒计时时长，默认倒计时时间60秒；
     */
    private long length = 60 * 1000;
    /**
     * 开始执行计时的类，可以在每秒实行间隔任务
     */
    private Timer timer;
    /**
     * 每秒时间到了之后所执行的任务
     */
    private TimerTask timerTask;
    /**
     * 在点击按钮之前按钮所显示的文字，默认是获取验证码
     */
    private String beforeText = "获取验证码";
    /**
     * 在开始倒计时之后那个秒数数字之后所要显示的字，默认是秒
     */
    private String timeUnit = "秒";
    /**
     * 在倒计时结束后按钮所显示的文字，默认是获取验证码
     */
    private String afterText = "重新获取";


    public VerifyCodeButton(Context context) {
        this(context, null);
    }

    public VerifyCodeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //初始化组件
    private void initView() {
        this.setText(beforeText);
    }

    //开始倒计时
    public void startTimer() {
        initTimer();
        this.setText(length / 1000 + afterText);
        this.setEnabled(false);
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 初始化时间
     */
    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                 LogUtils.e(TAG, "mHandler = " +mHandler);
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(1);
                    LogUtils.e(TAG, "mHandler sendEmptyMessage ");

                }
            }
        };
    }

    /**
     * 更新显示的文本
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VerifyCodeButton.this.setText(length / 1000 + timeUnit);
            length -= 1000;
            if (length < 0) {
                VerifyCodeButton.this.setEnabled(true);
                VerifyCodeButton.this.setText(afterText);
                clearTimer();
                length = 60 * 1000;
            }
        }
    };


 /*   private MyHandler handler = new MyHandler(this);
    static class MyHandler extends Handler {
        WeakReference weakReference;

        public MyHandler(VerifyCodeButton verifyCodeButton) {
            weakReference = new WeakReference(verifyCodeButton);
        }

        @Override
        public void handleMessage(Message msg) {

        }

    }*/
    /**
     * 清除倒计时
     */
    private void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 记得一定要在activity或者fragment消亡的时候清除倒计时，
     * 因为如果倒计时没有完的话子线程还在跑，
     * 这样的话就会引起内存溢出
     */
    @Override
    protected void onDetachedFromWindow() {
        clearTimer();
        if (mHandler != null) {

            mHandler = null;
        }
        super.onDetachedFromWindow();
    }

}
