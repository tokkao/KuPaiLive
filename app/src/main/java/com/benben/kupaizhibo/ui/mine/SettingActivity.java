package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.benben.commoncore.utils.ActivityManagerUtils;
import com.benben.commoncore.utils.AppUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ThreadPoolUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.MainActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.ConfigBean;
import com.benben.kupaizhibo.bean.OnLineCustomerServiceBean;
import com.benben.kupaizhibo.config.SystemDir;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.utils.LoginCheckUtils;
import com.hyphenate.helper.HyphenateHelper;
import com.hyphenate.helper.StatusBarUtils;
import com.hyphenate.helper.ui.ChatActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjn.updateapputils.util.CheckVersionRunnable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 设置
 */
public class SettingActivity extends BaseActivity {

    private static final String TAG = "SettingActivity";
    @BindView(R.id.tv_account_safe)
    TextView tvAccountSafe;
    @BindView(R.id.view_divider)
    View viewDivider;
    @BindView(R.id.tv_feed_back)
    TextView tvFeedBack;
    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;
    @BindView(R.id.tv_service_custom)
    TextView tvServiceCustom;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.setting));
        tvVersion.setText(getString(R.string.version_num, String.valueOf(AppUtils.getVersionCode(mContext))));
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }


    @OnClick({R.id.tv_account_safe, R.id.btn_log_out, R.id.tv_feed_back, R.id.tv_about_us, R.id.tv_service_custom, R.id.llyt_app_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_account_safe://账户安全
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                Intent pwdIntent = new Intent(mContext, ForgotPasswordActivity.class);
                pwdIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pwdIntent.putExtra("type", 1);
                startActivity(pwdIntent);
                break;
            case R.id.tv_feed_back://意见反馈
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                Intent feedIntent = new Intent(mContext, SetNoticeContentActivity.class);
                feedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                feedIntent.putExtra("type", 1);
                startActivity(feedIntent);
                break;
            case R.id.tv_about_us://关于我们
                startActivity(new Intent(mContext,AboutUsActivity.class));
                break;
            case R.id.tv_service_custom://在线客服
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                getCustomService();
                break;
            case R.id.llyt_app_version://app版本
                updateVersion();
                break;
            case R.id.btn_log_out://退出登录
                if(!LoginCheckUtils.checkLoginShowDialog(mContext))return;
                doLoginOut();
                break;
        }
    }

    //获取客服列表
    private void getCustomService() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_CUSTOM_SERVICE)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取客服列表----onSuccess:" + json);
                if(StringUtils.isEmpty(json)) {
                    toast(mContext.getResources().getString(R.string.server_exception));
                    return;
                }
                List<OnLineCustomerServiceBean> list = JSONUtils.jsonString2Beans(json, OnLineCustomerServiceBean.class);
                if(list != null && !list.isEmpty()) {
                    Random random = new Random();
                    HyphenateHelper.callChatIM(mContext, ChatActivity.class, "kpzb-"+list.get(random.nextInt(list.size())).getId(),
                            list.get(random.nextInt(list.size())).getNickname(), list.get(random.nextInt(list.size())).getAvatar(),
                            KuPaiLiveApplication.mPreferenceProvider.getPhoto());
                }

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取客服列表----onError:" + msg);
                ToastUtils.show(mContext,msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取客服列表----onFailure:" + e.getMessage());
            }
        });


    }

    /**
     * 版本更新
     */
    private void updateVersion() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.INDEX_GET_CONFIG)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        ConfigBean config = JSONUtils.jsonString2Bean(json, ConfigBean.class);
                        int versionCode = 0;
                        if (config != null
                                && !StringUtils.isEmpty(config.getApp_version())) {
                            try {
                                versionCode = Integer.parseInt(config.getApp_version());
                            } catch (Exception e) {
                                toast("服务器版本有误！");
                                LogUtils.e(TAG, e.getLocalizedMessage());
                                return;
                            }
                        }
                        if (AppUtils.getVersionCode(mContext) >= versionCode) {
                            toast("当前为最新版本！");
                            return;
                        }
                        if (StringUtils.isEmpty(config.getApp_download())) {
                            toast("更新地址有误！");
                            return;
                        }
                        //请求服务器版本
                        CheckVersionRunnable runnable = CheckVersionRunnable.from(mContext)
                                .setApkPath(SystemDir.DIR_UPDATE_APK)//文件存储路径
                                .setDownLoadUrl(config.getApp_download())
                                .setServerUpLoadLocalVersion("" + (AppUtils.getVersionCode(mContext) + 1))
                                .setServerVersion(config.getApp_version())
                                .setUpdateMsg("更新内容")
                                .isUseCostomDialog(true)
                                .setNotifyTitle(getResources().getString(R.string.app_name))
                                .setVersionShow("V" + config.getApp_version());
                        //启动通知，去下载
                        ThreadPoolUtils.newInstance().execute(runnable);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private void doLoginOut() {
        //清除登录信息
        LoginCheckUtils.clearUserInfo();
        //删除登录授权
        clearAuthLogin();

        ActivityManagerUtils.remove(MainActivity.class);
       // ActivityManagerUtils.clear();
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearAuthLogin() {
        UMAuthListener authListener = new UMAuthListener() {
            /**
             * @desc 授权开始的回调
             * @param platform 平台名称
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param data 用户资料返回
             */
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                LogUtils.e(TAG,t.getLocalizedMessage());
            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
            }
        };
      //  UMShareAPI.get(mContext).deleteOauth(mContext,SHARE_MEDIA.WEIXIN, authListener);
      //  UMShareAPI.get(mContext).deleteOauth(mContext, SHARE_MEDIA.QQ, authListener);

    }
}
