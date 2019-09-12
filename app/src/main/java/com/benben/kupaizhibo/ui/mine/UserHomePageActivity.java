package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.ActivityManagerUtils;
import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.UserHomePageAnchorListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.LiveInfoBean;
import com.benben.kupaizhibo.bean.MyFansFollowListBean;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.live.LookLiveActivity;
import com.hyphenate.helper.HyphenateHelper;
import com.hyphenate.helper.StatusBarUtils;
import com.hyphenate.helper.ui.ChatActivity;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-11.
 * Describe:用户个人主页
 */
public class UserHomePageActivity extends BaseActivity {

    private static final String TAG = "UserHomePageActivity";
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.tv_fans_num)
    TextView tvFansNum;
    @BindView(R.id.iv_user_sex)
    ImageView ivUserSex;
    @BindView(R.id.tv_user_age)
    TextView tvUserAge;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_id_num)
    TextView tvUserIdNum;
    @BindView(R.id.rlv_anchor_list)
    RecyclerView rlvAnchorList;
    @BindView(R.id.llyt_nothing_left)
    LinearLayout llytNothingLeft;
    @BindView(R.id.tv_notice_content)
    TextView tvNoticeContent;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.rlyt_look_live)
    RelativeLayout rlytLookLive;
    //用户主页列表
    private String userId;
    private UserInfoBean mUserInfo;
    private LiveInfoBean mLiveInfoBean;
    private UserHomePageAnchorListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_home_page;
    }

    @Override
    protected void initData() {
        userId = getIntent().getStringExtra("user_id");
        LogUtils.e(TAG, "user_id = " + userId);

        rlvAnchorList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new UserHomePageAnchorListAdapter(mContext);
        rlvAnchorList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<MyFansFollowListBean.DataBean>() {
            @Override
            public void onItemClick(View view, int position, MyFansFollowListBean.DataBean model) {
                Intent intent = new Intent(mContext, UserHomePageActivity.class);
                LogUtils.e(TAG, "model.getId() = " + model.getUid());
                intent.putExtra("user_id", String.valueOf(model.getUid()));
                startActivityForResult(intent, 101);
            }

            @Override
            public void onItemLongClick(View view, int position, MyFansFollowListBean.DataBean model) {

            }
        });
        getUserInfo();
        getIsLive();
        getOtherFollowFansList();
    }

    private void getIsLive() {
        BaseOkHttpClient.newBuilder()
                .addParam("userid", userId)
                .url(NetUrlUtils.GET_IS_LIVE)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "是否正在直播----onSuccess:" + json);
                mLiveInfoBean = JSONUtils.jsonString2Bean(json, LiveInfoBean.class);
                if (mLiveInfoBean == null) return;
                rlytLookLive.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "是否正在直播----onError:" + msg);
                // ToastUtils.show(mContext, msg);
                rlytLookLive.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "是否正在直播----onFailure:" + e.getMessage());
            }
        });


    }

    //获取用户详情
    private void getUserInfo() {
        BaseOkHttpClient.newBuilder()
                .addParam("uid", userId)
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        LogUtils.e(TAG, "用户主页-获取用户信息----onSuccess:" + json);
                        mUserInfo = JSONUtils.jsonString2Bean(json, UserInfoBean.class);
                        refreshUI();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, "用户主页-获取用户信息----onError:" + msg);

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, "用户主页-获取用户信息----onFailure:" + e.getMessage());

                    }
                });
    }

    private void refreshUI() {
        LogUtils.e(TAG, "*****************mUserInfo.toString()= " + mUserInfo.toString());
        ImageUtils.getPic(mUserInfo.getAvatar(), civUserAvatar, mContext, R.mipmap.icon_default_avatar);
        centerTitle.setText(mUserInfo.getNickname());
        rightTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(mUserInfo.getIs_follow() == 0 ? R.mipmap.icon_home_unfollow : R.mipmap.icon_home_follow), null, null, null);

        tvFansNum.setText(String.valueOf(mUserInfo.getFans()));
        tvFollowNum.setText(String.valueOf(mUserInfo.getFollow()));
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
        //公告
        if (StringUtils.isEmpty(mUserInfo.getNotice())) {
            llytNothingLeft.setVisibility(View.VISIBLE);
            tvNoticeContent.setVisibility(View.GONE);
        } else {
            llytNothingLeft.setVisibility(View.GONE);
            tvNoticeContent.setVisibility(View.VISIBLE);
            tvNoticeContent.setText(mUserInfo.getNotice());
        }


    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }


    @OnClick({R.id.llyt_follow, R.id.llyt_fans, R.id.rlyt_private_chat, R.id.rlyt_look_live, R.id.tv_follow_anchor, R.id.rl_back, R.id.right_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back: //返回
                finish();
                break;
            case R.id.right_title://关注/取消关注
                updateFollow();
                break;
            case R.id.llyt_follow://关注列表
            case R.id.tv_follow_anchor:
                Intent followIntent = new Intent(mContext, FollowFansListActivity.class);
                followIntent.putExtra("type", 0);
                followIntent.putExtra("other_user_id", userId);
                startActivityForResult(followIntent, 101);
                break;
            case R.id.llyt_fans://粉丝列表
                Intent fansIntent = new Intent(mContext, FollowFansListActivity.class);
                fansIntent.putExtra("type", 1);
                fansIntent.putExtra("other_user_id", userId);
                startActivityForResult(fansIntent, 101);
                break;
            case R.id.rlyt_private_chat://私聊ta
                toServiceOnline("kpzb-"+mUserInfo.getId());
                break;
            case R.id.rlyt_look_live://进入直播间
                ActivityManagerUtils.remove(LookLiveActivity.class);
                LookLiveActivity.startLiveWithData(mContext,mLiveInfoBean);
                finish();
                break;
           /* case R.id.tv_follow_anchor://关注列表
                Intent followAnchorIntent = new Intent(mContext, FollowFansListActivity.class);
                followAnchorIntent.putExtra("type", 0);
                startActivityForResult(followAnchorIntent, 101);
                break;*/
        }
    }

    //私聊主播
    private void toServiceOnline(String userId) {
        BaseOkHttpClient.newBuilder()
                .addParam("eid", userId)
                .url(NetUrlUtils.GET_EASEMOB_USER)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        LogUtils.e(TAG, "获取他人环信信息----onSuccess:" + json);
                        if (!TextUtils.isEmpty(json)) {
                            String userName = JSONUtils.getNoteJson(json, "nickname");
                            String avatar = JSONUtils.getNoteJson(json, "avatar");
                            if (TextUtils.isEmpty(userName)) {
                                userName = "";
                            }
                            if (TextUtils.isEmpty(avatar)) {
                                avatar = "";
                            }

                            HyphenateHelper.callChatIM(mContext, ChatActivity.class,userId, userName, avatar,
                                    KuPaiLiveApplication.mPreferenceProvider.getPhoto());
                        }

                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, "获取他人环信信息----onError:" + msg);

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, "获取他人环信信息----onFailure:" + e.getMessage());

                    }
                });
    }

    private void updateFollow() {
        BaseOkHttpClient.newBuilder()
                .addParam("anchor_id", userId)
                .url(NetUrlUtils.FOLLOW)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                toast(msg);
               // FollowResultBean result = JSONUtils.jsonString2Bean(json, FollowResultBean.class);
                // rightTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(result.getFollow() != 0 ? R.mipmap.icon_home_follow : R.mipmap.icon_home_unfollow), null, null, null);
                getUserInfo();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });
    }


    private void getOtherFollowFansList() {
        BaseOkHttpClient.newBuilder()
                .addParam("page", 1)
                .addParam("uid", userId)
                .addParam("type", 1) // 1关注 2粉丝
                .url(NetUrlUtils.GET_OTHER_FANS_FOLLOW)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "他人的关注列表----onSuccess:" + json);
                MyFansFollowListBean myFansFollowListBean = JSONUtils.jsonString2Bean(json, MyFansFollowListBean.class);
                List<MyFansFollowListBean.DataBean> mFansFollowList = myFansFollowListBean.getData();
                mAdapter.refreshList(mFansFollowList);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "他人的关注列表----onError:" + msg);
                ToastUtils.show(mContext, msg);

            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "他人的关注列表----onFailure:" + e.getMessage());

            }
        });


    }

}
