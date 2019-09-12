package com.benben.kupaizhibo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.bean.UserLoginSuccessBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.mine.LoginActivity;
import com.hyphenate.chat.EMClient;
import com.kongzue.dialog.v3.MessageDialog;

import java.io.IOException;

import okhttp3.Call;

//import com.hyphenate.chat.EMClient;

/**
 * 用户是否登录检测工具类
 * Created by Administrator on 2017/11/28.
 */
public class LoginCheckUtils {
    private static final String TAG = "LoginCheckUtils";

    //验证是否登录的异步回调
    public interface CheckCallBack {
        /**
         * 检查结果
         *
         * @param flag 是否登录
         *             true：已登录；false：未登录
         */
        void onCheckResult(boolean flag);
    }

    /**
     * 检查用户是否登录
     *
     * @param context
     * @return
     */
    public static boolean checkUserIsLogin(Context context) {
        String uid = KuPaiLiveApplication.mPreferenceProvider.getUId();
        String token = KuPaiLiveApplication.mPreferenceProvider.getToken();
        if (!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(token)) {
            return true;
        }
        return false;
    }

    /**
     * 检查用户是否登录
     *
     * @param activity
     * @param callBack
     * @return
     */
    public static void checkUserIsLogin(Activity activity, CheckCallBack callBack) {
        String uid = KuPaiLiveApplication.mPreferenceProvider.getUId();
        String token = KuPaiLiveApplication.mPreferenceProvider.getToken();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token)) {
            //清除用户信息
            clearUserInfo();
            //验证结果回调
            callBack.onCheckResult(false);
            //显示验证弹窗
            showLoginDialog(activity, false);
            return;
        }
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.VERIFY_TOKEN)
                .json()
                .post().build().enqueue(activity, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                String user = JSONUtils.getNoteJson(result, "user");
                //更新当前的登录用户信息
                if (!StringUtils.isEmpty(user)) {
                    UserInfoBean userBean = JSONUtils.jsonString2Bean(user, UserInfoBean.class);
                    if (userBean != null) {
                        LoginCheckUtils.updateUserInfo(userBean);
                    }
                }
                callBack.onCheckResult(true);
            }

            @Override
            public void onError(int code, String msg) {
                //清除用户信息
                clearUserInfo();
                //验证结果回调
                callBack.onCheckResult(false);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //清除用户信息
                clearUserInfo();
                //验证结果回调
                callBack.onCheckResult(false);
                //显示验证弹窗
                showLoginDialog(activity, false);
            }
        });
    }


    /**
     * 提示用户登录对话框
     *
     * @param context         //上下文
     * @param isClearUserInfo //是否清除用户信息
     */
    public static void showLoginDialog(Activity context, boolean isClearUserInfo) {

        MessageDialog.show((AppCompatActivity) context, context.getResources().getString(R.string.cozy_tips), context.getResources().getString(R.string.please_login_first), context.getResources().getString(R.string.positive), context.getResources().getString(R.string.negative))
                .setOnOkButtonClickListener((baseDialog, v) -> {
                    if (isClearUserInfo) LoginCheckUtils.clearUserInfo();
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return false;
                }).show();

    }


    //检查是否登录
    public static boolean checkLoginShowDialog(Activity mContext) {
        if (!LoginCheckUtils.checkUserIsLogin(mContext)) {
            LoginCheckUtils.showLoginDialog(mContext, false);
            return false;

        }
        return true;
    }

    /**
     * 更新用户信息
     *
     * @param infoBean 用户信息
     */
    public static void updateUserInfo(UserInfoBean infoBean) {
        KuPaiLiveApplication.mPreferenceProvider.setPhoto(infoBean.getAvatar());
        KuPaiLiveApplication.mPreferenceProvider.setUserLevel(String.valueOf(infoBean.getUser_level()));
        KuPaiLiveApplication.mPreferenceProvider.setMobile(infoBean.getMobile());
//        KuPaiLiveApplication.mPreferenceProvider.setUserName(infoBean.getNickname());
//        KuPaiLiveApplication.mPreferenceProvider.setSex(infoBean.getSex());
//        KuPaiLiveApplication.mPreferenceProvider.setBoBi(String.valueOf(infoBean.getUser_bobi()));
//        KuPaiLiveApplication.mPreferenceProvider.setUserType(infoBean.getUser_type());
    }

    public static void saveLoginInfo(UserLoginSuccessBean data, String mobile) {
        KuPaiLiveApplication.mPreferenceProvider.setToken(data.getAccess_token());
        KuPaiLiveApplication.mPreferenceProvider.setUId(String.valueOf(data.getId()));
        KuPaiLiveApplication.mPreferenceProvider.setUserName(data.getNickname());
        KuPaiLiveApplication.mPreferenceProvider.setUserType(data.getUser_type());
        KuPaiLiveApplication.mPreferenceProvider.setMobile(mobile);
        KuPaiLiveApplication.mPreferenceProvider.setPhoto(data.getAvatar());
        KuPaiLiveApplication.mPreferenceProvider.setEasemob(data.getEasemob());
        KuPaiLiveApplication.mPreferenceProvider.setUserLevel(String.valueOf(data.getUser_level()));

    }

    public static void clearUserInfo() {
        KuPaiLiveApplication.mPreferenceProvider.setToken("");
        KuPaiLiveApplication.mPreferenceProvider.setUId("");
        KuPaiLiveApplication.mPreferenceProvider.setUserName("");
        KuPaiLiveApplication.mPreferenceProvider.setUserType(0);
        KuPaiLiveApplication.mPreferenceProvider.setMobile("");
        KuPaiLiveApplication.mPreferenceProvider.setPhoto("");
        KuPaiLiveApplication.mPreferenceProvider.setEasemob("");
        KuPaiLiveApplication.mPreferenceProvider.setSex(0);
        KuPaiLiveApplication.mPreferenceProvider.setBoBi("0");
        KuPaiLiveApplication.mPreferenceProvider.setUserLevel("1");

        //退出环信登录
        EMClient.getInstance().logout(false);
    }
}
