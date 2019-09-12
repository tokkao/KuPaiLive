package com.benben.kupaizhibo.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.config.Constants;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.mine.UserHomePageActivity;
import com.hyphenate.helper.HyphenateHelper;
import com.hyphenate.helper.ui.ChatActivity;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;


/**
 * 功能:直播间点击头像对话框
 * create by zjn on 2019/5/15 0015
 * email:168455992@qq.com
 */
public class UserInfoDialog {
    private static final String TAG = "UserInfoDialog";

    private static UserInfoDialog mInstance;


    private AlertDialog mAlertDialog;
    private Activity mActivity;

    private UserInfoDialog() {
    }

    public static UserInfoDialog getInstance() {
        if (mInstance == null) {
            mInstance = new UserInfoDialog();
        }
        return mInstance;
    }

    /**
     * 弹出dialog提醒（）
     *
     * @param context
     * @param mUserInfoBean
     */
    public void showUserInfoDialog(final Activity context, UserInfoBean mUserInfoBean,String streamCode) {
        this.mActivity = context;
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //设置dialog主题
            mAlertDialog = builder.create();
            View dialogAlertView = context.getLayoutInflater().inflate(R.layout.dialog_user_info, null);
            mAlertDialog.setView(dialogAlertView); // 设置view 和左上右下的边距
            Window window = mAlertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ViewHolder holder = new ViewHolder(dialogAlertView);
            holder.tvUserNickname = dialogAlertView.findViewById(R.id.tv_user_nickname);
            holder.tvGuanzhuNum = dialogAlertView.findViewById(R.id.tv_guanzhu_num);
            holder.tvFensiNum = dialogAlertView.findViewById(R.id.tv_fensi_num);
            holder.rlytChat = dialogAlertView.findViewById(R.id.rlyt_chat);
            holder.rlytZhuye = dialogAlertView.findViewById(R.id.rlyt_zhuye);
            holder.ivReport = dialogAlertView.findViewById(R.id.iv_report);

            holder.civUserAvatar = dialogAlertView.findViewById(R.id.civ_user_avatar);

            holder.tvUserAge = dialogAlertView.findViewById(R.id.tv_user_age);
            mAlertDialog.show();


            //头像
            ImageUtils.getPic(mUserInfoBean.getAvatar(), holder.civUserAvatar, context, R.mipmap.icon_default_avatar);
            //性别
            if (mUserInfoBean.getSex() == 1) {
                holder.ivUserSex.setImageResource(R.mipmap.icon_sex_man);
            } else {
                holder.ivUserSex.setImageResource(R.mipmap.icon_sex_woman);
            }
            //年龄
            if (mUserInfoBean.getBirthday() > 0) {
                Date birthday = new Date(mUserInfoBean.getBirthday() * 1000);
                holder.tvUserAge.setText(String.valueOf(DateUtils.YearsBetween(
                        birthday, new Date())));
            } else {
                holder.tvUserAge.setText("0");
            }

            holder.tvUserNickname.setText(mUserInfoBean.getNickname());
            holder.tvGuanzhuNum.setText(StringUtils.getWanStr(mUserInfoBean.getFollow()));
            holder.tvFensiNum.setText(StringUtils.getWanStr(mUserInfoBean.getFans()));

            holder.tvUserAddress.setText(mUserInfoBean.getAddress());
            holder.tvUserIdNum.setText(mActivity.getResources().getString(R.string.user_id,String.valueOf(mUserInfoBean.getId())));
            //举报
            holder.ivReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismissAlertDialog();
                    showReportDialog(mUserInfoBean.getId(),streamCode);
                }
            });
            //私信
            holder.rlytChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAlertDialog();
                    toServiceOnline("kpzb-" + mUserInfoBean.getId());
                }
            });
            //主页
            holder.rlytZhuye.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAlertDialog();
                    //跳转该用户主页
                    Intent intent = new Intent(context, UserHomePageActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_USER_ID, String.valueOf(mUserInfoBean.getId()));
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //举报弹窗
    private void showReportDialog(int user_id,String steamCode) {
        CommonReportPopupWindow commonReportPopupWindow = new CommonReportPopupWindow(mActivity,user_id);
        commonReportPopupWindow.showWindow(steamCode);
    }


    //私聊主播
    private void toServiceOnline(String userId) {
        BaseOkHttpClient.newBuilder()
                .addParam("eid", userId)
                .url(NetUrlUtils.GET_EASEMOB_USER)
                .post().json().build()
                .enqueue(mActivity, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        if (!TextUtils.isEmpty(json)) {
                            String userName = JSONUtils.getNoteJson(json, "nickname");
                            String avatar = JSONUtils.getNoteJson(json, "avatar");
                            if (TextUtils.isEmpty(userName)) {
                                userName = "";
                            }
                            if (TextUtils.isEmpty(avatar)) {
                                avatar = "";
                            }

                            HyphenateHelper.callChatIM(mActivity, ChatActivity.class, userId, userName, avatar,
                                    KuPaiLiveApplication.mPreferenceProvider.getPhoto());
                        }

                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                        ToastUtils.show(mActivity, msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                    }
                });
    }


    public void dismissAlertDialog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }


    public static
    class ViewHolder {
        public View rootView;
        public ImageView ivReport;
        public TextView tvUserNickname;
        public ImageView ivUserSex;
        public TextView tvUserAge;
        public TextView tvUserAddress;
        public TextView tvUserIdNum;
        public LinearLayout llytUserInfo;
        public TextView tvGuanzhuNum;
        public RelativeLayout rlytGuanhzu;
        public TextView tvFensiNum;
        public RelativeLayout rlytFensi;
        public LinearLayout llytGuanzhuInfo;
        public RelativeLayout rlytChat;
        public RelativeLayout rlytZhuye;
        public LinearLayout llytBottomBtn;
        public CircleImageView civUserAvatar;
        public RelativeLayout rlytPersonalAvatar;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.ivReport = (ImageView) rootView.findViewById(R.id.iv_report);
            this.tvUserNickname = (TextView) rootView.findViewById(R.id.tv_user_nickname);
            this.ivUserSex = (ImageView) rootView.findViewById(R.id.iv_user_sex);
            this.tvUserAge = (TextView) rootView.findViewById(R.id.tv_user_age);
            this.tvUserAddress = (TextView) rootView.findViewById(R.id.tv_user_address);
            this.tvUserIdNum = (TextView) rootView.findViewById(R.id.tv_user_id_num);
            this.llytUserInfo = (LinearLayout) rootView.findViewById(R.id.llyt_user_info);
            this.tvGuanzhuNum = (TextView) rootView.findViewById(R.id.tv_guanzhu_num);
            this.rlytGuanhzu = (RelativeLayout) rootView.findViewById(R.id.rlyt_guanhzu);
            this.tvFensiNum = (TextView) rootView.findViewById(R.id.tv_fensi_num);
            this.rlytFensi = (RelativeLayout) rootView.findViewById(R.id.rlyt_fensi);
            this.llytGuanzhuInfo = (LinearLayout) rootView.findViewById(R.id.llyt_guanzhu_info);
            this.rlytChat = (RelativeLayout) rootView.findViewById(R.id.rlyt_chat);
            this.rlytZhuye = (RelativeLayout) rootView.findViewById(R.id.rlyt_zhuye);
            this.llytBottomBtn = (LinearLayout) rootView.findViewById(R.id.llyt_bottom_btn);
            this.civUserAvatar = (CircleImageView) rootView.findViewById(R.id.civ_user_avatar);
            this.rlytPersonalAvatar = (RelativeLayout) rootView.findViewById(R.id.rlyt_personal_avatar);
        }

    }
}
