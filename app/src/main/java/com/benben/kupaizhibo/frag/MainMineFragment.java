package com.benben.kupaizhibo.frag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.FastBlurUtils;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.RxBus;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.LazyBaseFragments;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.AuthStatusBean;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.mine.AnchorAuthenticationActivity;
import com.benben.kupaizhibo.ui.mine.FollowFansListActivity;
import com.benben.kupaizhibo.ui.mine.MyLevelActivity;
import com.benben.kupaizhibo.ui.mine.MyRewardActivity;
import com.benben.kupaizhibo.ui.mine.MySharingActivity;
import com.benben.kupaizhibo.ui.mine.ParentalControlActivity;
import com.benben.kupaizhibo.ui.mine.PersonDataActivity;
import com.benben.kupaizhibo.ui.mine.RechargeDiamondActivity;
import com.benben.kupaizhibo.ui.mine.SettingActivity;
import com.benben.kupaizhibo.utils.LoginCheckUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

import static com.benben.kupaizhibo.config.Constants.RXBUS_KEY_REFRESH_MINEFRAGMENT;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 我的fragment
 */
public class MainMineFragment extends LazyBaseFragments {

    private static final String TAG = "MainMineFragment";
    @BindView(R.id.iv_top_bg)
    ImageView ivTopBg;
    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_age)
    TextView tvUserAge;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_id_num)
    TextView tvUserIdNum;
    @BindView(R.id.llyt_user_info)
    LinearLayout llytUserInfo;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.llyt_follow)
    LinearLayout llytFollow;
    @BindView(R.id.tv_fans_num)
    TextView tvFansNum;
    @BindView(R.id.llyt_fans)
    LinearLayout llytFans;
    @BindView(R.id.tv_diamond_num)
    TextView tvDiamondNum;
    @BindView(R.id.llyt_recharge)
    LinearLayout llytRecharge;
    @BindView(R.id.tv_personal_info)
    TextView tvPersonalInfo;
    @BindView(R.id.tv_my_level)
    TextView tvMyLevel;
    @BindView(R.id.tv_my_reward)
    TextView tvMyReward;
    @BindView(R.id.tv_my_sharing)
    TextView tvMySharing;
    @BindView(R.id.tv_apply_anchor)
    TextView tvApplyAnchor;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.tv_parental_control_mode)
    TextView tvParentalControlMode;
    @BindView(R.id.iv_user_sex)
    ImageView ivUserSex;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private UserInfoBean mUserInfo;

    public static MainMineFragment getInstance() {
        MainMineFragment sf = new MainMineFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_mine, null);
        return mRootView;
    }

    @Override
    public void initView() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getUserInfo();
            }
        });
        //刷新个人中心
        RxBus.getInstance().toObservable(String.class)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.e(TAG,s);
                        if (RXBUS_KEY_REFRESH_MINEFRAGMENT.equals(s)) {
                            getUserInfo();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void initData() {

    }


    //获取用户详情
    private void getUserInfo() {

        BaseOkHttpClient.newBuilder()
                .addParam("uid", KuPaiLiveApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        mUserInfo = JSONUtils.jsonString2Bean(json, UserInfoBean.class);
                        refreshUI();
                        refreshLayout.finishRefresh(true);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                        refreshLayout.finishRefresh(true);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                        refreshLayout.finishRefresh(true);
                    }
                });
    }

    private void refreshUI() {
        LoginCheckUtils.updateUserInfo(mUserInfo);
        ImageUtils.getPic(mUserInfo.getAvatar(),civUserAvatar,mContext,R.mipmap.icon_default_avatar);
        ivUserSex.setImageResource(mUserInfo.getSex() == 1 ? R.mipmap.icon_sex_man : R.mipmap.icon_sex_woman);
        //年龄根据生日计算
        if (mUserInfo.getBirthday() > 0) {
            Date birthday = new Date(mUserInfo.getBirthday() * 1000);
            tvUserAge.setText(String.valueOf(DateUtils.YearsBetween(
                    birthday, new Date())));
        } else {
            tvUserAge.setText("0");
        }
        //地址
        tvUserAddress.setText(mUserInfo.getAddress());
        //id
        tvUserIdNum.setText(getString(R.string.user_id, String.valueOf(mUserInfo.getId())));
        //背景  高斯模糊
        loadBackgroundImage();
        /*Glide.with(mContext) //不起作用
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.mipmap.img_user_center_top_bg).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.mipmap.img_user_center_top_bg).transform(new BlurTransformation(mContext, 14, 3)))
                .asBitmap()
                .load(mUserInfo.getBackground())
                .into(ivTopBg);*/
//        ImageUtils.getPic(mUserInfo.getBackground(), ivTopBg, mContext, R.mipmap.img_user_center_top_bg);
        //昵称
        tvUserName.setText(mUserInfo.getNickname());
        //粉丝数量
        tvFansNum.setText(StringUtils.getWanStr(mUserInfo.getFans()));
        //关注数量
        tvFollowNum.setText(StringUtils.getWanStr(mUserInfo.getFollow()));
        //钻石余额
        tvDiamondNum.setText(getString(R.string.my_diamond, StringUtils.getWanStr(mUserInfo.getUser_bobi())));
    }

    //加载背景图
    private void loadBackgroundImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap userBackground = ImageUtils.getBitMBitmap(mUserInfo.getBackground());
                if (userBackground != null) {
                    Bitmap blurPic = FastBlurUtils.toBlur(userBackground, 3);
                    if (ivTopBg != null && blurPic != null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                ivTopBg.setImageBitmap(blurPic);
                            }
                        });
                    }
                }
            }
        }).start();
    }


    @OnClick({R.id.llyt_follow, R.id.llyt_fans, R.id.tv_user_name,R.id.llyt_recharge, R.id.tv_personal_info, R.id.tv_my_level, R.id.tv_my_reward, R.id.tv_my_sharing, R.id.tv_apply_anchor, R.id.tv_setting, R.id.tv_parental_control_mode})
    public void onViewClicked(View view) {
        Intent followFansIntent = new Intent(mContext, FollowFansListActivity.class);

        switch (view.getId()) {
            case R.id.tv_user_name://名字
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                break;
            case R.id.llyt_follow://关注人数
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                followFansIntent.putExtra("type", 2);
                startActivityForResult(followFansIntent, 101);
                break;
            case R.id.llyt_fans://粉丝人数
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                followFansIntent.putExtra("type", 3);
                startActivityForResult(followFansIntent, 101);
                break;
            case R.id.llyt_recharge://去充值
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                Intent rechargeIntent = new Intent(mContext, RechargeDiamondActivity.class);
                startActivityForResult(rechargeIntent, 102);
                break;
            case R.id.tv_personal_info://个人资料
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                Intent personalIntent = new Intent(mContext, PersonDataActivity.class);
                startActivityForResult(personalIntent, 102);
                break;
            case R.id.tv_my_level://我的等级
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                Intent levelIntent = new Intent(mContext, MyLevelActivity.class);
                startActivity(levelIntent);
                break;
            case R.id.tv_my_reward://我的打赏
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                Intent rewardIntent = new Intent(mContext, MyRewardActivity.class);
                startActivity(rewardIntent);
                break;
            case R.id.tv_my_sharing://我的分享
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                Intent sharingIntent = new Intent(mContext, MySharingActivity.class);
                startActivity(sharingIntent);
                break;
            case R.id.tv_apply_anchor://申请主播
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                getAuthStatus();
                break;
            case R.id.tv_setting://设置
                Intent settingIntent = new Intent(mContext, SettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.tv_parental_control_mode://家长控制模式
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                Intent controlIntent = new Intent(mContext, ParentalControlActivity.class);
                startActivity(controlIntent);
                break;
        }
    }

    //获取认证状态
    private void getAuthStatus() {

        BaseOkHttpClient.newBuilder()
                .addParam("type", 1)//0=>实名认证 1=>主播认证
                .url(NetUrlUtils.AUTHENTICATION_STATUS)
                .json().post().build()
                .enqueue(mContext, new BaseCallBack<String>() {

                    @Override
                    public void onSuccess(String json, String msg) {
                        LogUtils.e(TAG, "获取认证状态----onSuccess:" + json);
                        if (StringUtils.isEmpty(json)) {
                            ToastUtils.show(mContext,mContext.getResources().getString(R.string.server_exception));
                            return;
                        }
                       /* UserInfoBean mUserInfo = JSONUtils.jsonString2Bean(json, UserInfoBean.class);
                        if (mUserInfo.getUser_type() == 1) {
                            startActivity(new Intent(mContext, CreateLiveActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else {
                            showAuthDialog();
                        }*/
                        AuthStatusBean lstAuthStatus = JSONUtils.jsonString2Bean(json,
                                AuthStatusBean.class);

                        //-1失败，0待审核，1成功
                        startActivity(new Intent(mContext, AnchorAuthenticationActivity.class).putExtra("auth_info", lstAuthStatus));
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, "获取认证状态----onError:" + msg);
                        startActivity(new Intent(mContext, AnchorAuthenticationActivity.class));

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, "获取认证状态----onFailure:" + e.getMessage());
                    }
                });
    }

    @Override
    protected void loadData() {
        super.loadData();
        getUserInfo();
    }
}
