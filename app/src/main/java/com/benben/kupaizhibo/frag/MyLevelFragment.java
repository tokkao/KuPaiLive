package com.benben.kupaizhibo.frag;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.LazyBaseFragments;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.MyLevelBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 我的等级fragment
 */
public class MyLevelFragment extends LazyBaseFragments {

    private static final String TAG = "MyLevelFragment";
    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    @BindView(R.id.iv_level)
    ImageView ivLevel;
    @BindView(R.id.tv_defeated)
    TextView tvDefeated;
    @BindView(R.id.tv_level_description)
    TextView tvLevelDescription;
    @BindView(R.id.pb_exp)
    ProgressBar pbExp;
    @BindView(R.id.tv_min_exp)
    TextView tvMinExp;
    @BindView(R.id.tv_current_exp)
    TextView tvCurrentExp;
    @BindView(R.id.tv_max_exp)
    TextView tvMaxExp;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    //0 用户等级 1 主播等级
    private int mLevelType;

    public static MyLevelFragment getInstance() {
        MyLevelFragment sf = new MyLevelFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_my_level, null);
        return mRootView;
    }

    @Override
    public void initView() {
        mLevelType = getArguments().getInt("type", 0);
        tvLevelDescription.setText(mLevelType == 0 ? R.string.vip_privilege : R.string.level_rule);
        ImageUtils.getPic(KuPaiLiveApplication.mPreferenceProvider.getPhoto(), civUserAvatar, mContext, R.mipmap.icon_default_avatar);
    }

    @Override
    public void initData() {

        if (mLevelType == 0) {
            //用户等级
            getUserLevel();
        } else {
            //主播等级
            getAnchorLevel();
        }

    }

    private void getAnchorLevel() {
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", KuPaiLiveApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_ANCHOR_LEVEL)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "主播等级----onSuccess:" + json);
                MyLevelBean myLevelBean = JSONUtils.jsonString2Bean(json, MyLevelBean.class);
                tvDefeated.setText(getString(R.string.defeated_users, String.valueOf(myLevelBean.getPercentage())));
                tvMinExp.setText(String.valueOf(myLevelBean.getDefault_exp()));
                tvMaxExp.setText(String.valueOf(myLevelBean.getCount_exp()));
                tvCurrentExp.setText(myLevelBean.getNow_exp());
                pbExp.setMax(myLevelBean.getCount_exp());
                pbExp.setProgress((int) Float.parseFloat(myLevelBean.getNow_exp()));
                tvLevel.setText(String.valueOf(myLevelBean.getAnchor_level()));
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "主播等级----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "主播等级----onFailure:" + e.getMessage());
            }
        });


    }

    private void getUserLevel() {
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", KuPaiLiveApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_USER_LEVEL)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "用户等级----onSuccess:" + json);
                MyLevelBean myLevelBean = JSONUtils.jsonString2Bean(json, MyLevelBean.class);
                tvDefeated.setText(getString(R.string.defeated_users, String.valueOf(myLevelBean.getPercentage())));
                tvMinExp.setText(String.valueOf(myLevelBean.getDefault_exp()));
                tvMaxExp.setText(String.valueOf(myLevelBean.getCount_exp()));
                tvCurrentExp.setText(myLevelBean.getNow_exp());
                pbExp.setMax(myLevelBean.getCount_exp());
                pbExp.setProgress((int) Float.parseFloat(myLevelBean.getNow_exp()));
                tvLevel.setText(String.valueOf(myLevelBean.getUser_level()));
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "用户等级----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "用户等级----onFailure:" + e.getMessage());
            }
        });

    }


}
