package com.benben.kupaizhibo.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StatusBarUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.MainActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.AdvertBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;

import java.io.IOException;

import okhttp3.Call;

/**
 * 闪屏页面
 */
public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private ImageView mIvSplash;
    private TextView tvSkip;
    private RelativeLayout rlytRootView;
    private int mNavigationBarHeight;
    private CountDownTimer mCountDownTimer;
    private AdvertBean mAdvertBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        mIvSplash = findViewById(R.id.iv_splash);
        tvSkip = findViewById(R.id.tv_skip);
        rlytRootView = findViewById(R.id.rlyt_root_view);

        startCountDown(3 * 1000);
        setMarginNavigationBar();
       // getAdvertImageList();

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainPager();
            }
        });

        mIvSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAdvertBean != null) {

                    if (mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                        mCountDownTimer = null;
                    }

                    Intent mainIntent = new Intent(mContext, MainActivity.class);
                    startActivity(mainIntent);
                    NormalWebViewActivity.startWithData(mContext, mAdvertBean.getUrl(), mAdvertBean.getTitle(), true, false);
                    finish();
                }
            }
        });
    }

    private void showAdvertImage(String img_path) {
        tvSkip.setVisibility(View.VISIBLE);
        //倒计时的时间 3s
        if (!StringUtils.isEmpty(img_path)) {
            //显示广告图片
            ImageUtils.getPic(img_path, mIvSplash, mContext, R.mipmap.splash_pic);
        }
    }

    //获取广告图片地址
    private void getAdvertImageList() {
        BaseOkHttpClient.newBuilder()
                .addParam("machine", 1)//机型参数：1:安卓；2:苹果
//                .url(NetUrlUtils.GET_ADVERT_LIST)
                .url(NetUrlUtils.BASEURL)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        LogUtils.e(TAG, "onSuccess: 获取开启app时图片广告----" + result);
                        mAdvertBean = JSONUtils.jsonString2Bean(result, AdvertBean.class);
                        if (mAdvertBean != null) {
                            showAdvertImage(mAdvertBean.getImage());
                        } else {
                            showAdvertImage("");
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        //showAdvertImage("");
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        // showAdvertImage("");
                    }
                });

    }

    //倒计时
    private void startCountDown(long time_length) {

        mCountDownTimer = new CountDownTimer(time_length, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvSkip.setText(getResources().getString(R.string.skip) + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                toMainPager();
            }
        };
        mCountDownTimer.start();
    }

    //跳转首页
    private void  toMainPager() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        mNavigationBarHeight = StatusBarUtils.getNavigationBarHeight(mContext);

    }

    //计算虚拟按键的高度 setmargin
    private void setMarginNavigationBar() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlytRootView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, mNavigationBarHeight);
        rlytRootView.setLayoutParams(layoutParams);
    }
}
