package com.benben.kupaizhibo.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benben.kupaizhibo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:zhn
 * Time:2019/6/11 17:30
 * <p>
 * 这是一个仿淘宝头条的竖直广告滚动布局。
 * 实现以下几个内容
 * 1、数据实现动态加载，组件默认不显示，当有数据加载时显示
 * 2、子View的加载自动执行动画
 * 3、isLoop为false时：所有子View播放一遍后隐藏组件，并且移除所有的子View
 * 4、isLoop为true时：一直循环播放
 * <p>
 * PS:参考了ViewFlipper和ViewAnimator的源码
 */
public class RollAdsLayout extends FrameLayout {

    //默认间隔的时间
    private static final int DEFAULT_INTERVAL = 5000;

    private int mFlipInterval = DEFAULT_INTERVAL;

    //是否循环播放
    private boolean isLoop = true;

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    //当前显示的View的index
    public int mWhichChild = -1;
    //子View执行的动画
    public Animation mInAnimation;
    public Animation mOutAnimation;

    private boolean mRunning = false;
    private boolean mStarted = false;
    private boolean mVisible = false;
    private boolean mUserPresent = true;

    private List<String> mDatas;

    public RollAdsLayout(@NonNull Context context) {
        this(context, null);
    }

    public RollAdsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RollAdsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化动画
        mInAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_marquee_in);
        mOutAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_marquee_out);
        mDatas = new ArrayList<>();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mUserPresent = false;
                updateRunning(true);
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mUserPresent = true;
                updateRunning(false);
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Listen for broadcasts related to user-presence
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        // OK, this is gross but needed. This class is supported by the
        // remote views machanism and as a part of that the remote views
        // can be inflated by a context for another user without the app
        // having interact users permission - just for loading resources.
        // For exmaple, when adding widgets from a user profile to the
        // home screen. Therefore, we register the receiver as the current
        // user not the one the context is for.
        getContext().registerReceiver(mReceiver, filter, null, getHandler());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;

        getContext().unregisterReceiver(mReceiver);
        updateRunning(true);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning(false);
    }

    /**
     * 设置间隔时间
     *
     * @param milliseconds
     */
    public void setFlipInterval(int milliseconds) {
        mFlipInterval = milliseconds;
    }


    public void addAds(List<String> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        if (!mStarted) {
            mStarted = true;
            mDatas.clear();
            mDatas.addAll(datas);
            startFlipping();
        } else {
            mDatas.addAll(datas);
        }
    }

    public void addOneAds(String ads) {
        if (TextUtils.isEmpty(ads)) {
            return;
        }
        if (!mStarted) {
            mStarted = true;
            mDatas.clear();
            mDatas.add(ads);
            startFlipping();
        } else {
            mDatas.add(ads);
        }
    }

    public void refreshAds(List<String> datas) {
        if (datas == null || datas.size() == 0) {
            setVisibility(View.GONE);
            return;
        }
        mDatas.clear();
        mDatas.addAll(datas);
        if (!mStarted) {
            mStarted = true;
            startFlipping();
        }
    }

    private void startFlipping() {
        updateRunning(true);
        //初始化mWhichChild
        mWhichChild = -1;
        //显示布局
        setVisibility(View.VISIBLE);
    }

    private void stopFlipping() {
        mStarted = false;
        updateRunning(true);
        //隐藏布局
        setVisibility(View.GONE);
        //移除所有组件
        removeAllViews();
    }

    private void updateRunning(boolean flipNow) {
        boolean running = mVisible && mStarted && mUserPresent;
        if (running != mRunning) {
            if (running) {
                post(mFlipRunnable);
            } else {
                //停止执行
                removeCallbacks(mFlipRunnable);
            }
            mRunning = running;
        }
    }

    private void showNext() {
        int index = mWhichChild + 1;
        if (index >= mDatas.size()) {
            stopFlipping();
        } else {
            View view = getView(mDatas.get(index));
            addView(view);
            setDisplayedChild(mWhichChild + 1);
        }
    }

    private void showLoopNext() {
        int index = mWhichChild + 1;
        if (index >= mDatas.size()) {
            mWhichChild = -1;
            index = mWhichChild + 1;
        }
        if (index == getChildCount()) {
            View view = getView(mDatas.get(index));
            addView(view);
        }
        setDisplayedChild(mWhichChild + 1);
    }

    private View getView(String data) {
        TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_roll_ads, null);
        view.setText(data);
        return view;
    }

    public void setDisplayedChild(int whichChild) {
        mWhichChild = whichChild;
        boolean hasFocus = getFocusedChild() != null;
        // This will clear old focus if we had it
        showOnly(mWhichChild);
        if (hasFocus) {
            // Try to retake focus if we had it
            requestFocus(FOCUS_FORWARD);
        }
    }

    void showOnly(int childIndex) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (i == childIndex) {
                if (mInAnimation != null) {
                    child.startAnimation(mInAnimation);
                }
                child.setVisibility(View.VISIBLE);
            } else {
                if (mOutAnimation != null && child.getVisibility() == View.VISIBLE) {
                    child.startAnimation(mOutAnimation);
                } else if (child.getAnimation() == mInAnimation)
                    child.clearAnimation();
                child.setVisibility(View.GONE);
            }
        }
    }


    private final Runnable mFlipRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRunning) {
                if (isLoop) {
                    showLoopNext();
                } else {
                    showNext();
                }
                postDelayed(mFlipRunnable, mFlipInterval);
            }
        }
    };

}
