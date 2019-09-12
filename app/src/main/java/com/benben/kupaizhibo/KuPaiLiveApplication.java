package com.benben.kupaizhibo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.benben.commoncore.utils.ActivityManagerUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.kupaizhibo.cerror.TheAppCrashHandler;
import com.benben.kupaizhibo.utils.PreferenceProvider;
import com.benben.kupaizhibo.utils.UMengHelper;
import com.hyphenate.helper.HyphenateHelper;
import com.kongzue.dialog.util.DialogSettings;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

/**
 * Create by wanghk on 2019-05-24.
 * Describe: Application 初始化
 */
public class KuPaiLiveApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "KuPaiLiveApplication";
    public static PreferenceProvider mPreferenceProvider;// preference Provider
    public TheAppCrashHandler m_CrashHandler;
    public static KuPaiLiveApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();


        //友盟三方登录，设置每次登录都需要重新授权
        UMengHelper.init(this);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);

        mApplication = this;
        mPreferenceProvider = new PreferenceProvider(this);
        //错误日志捕捉
        m_CrashHandler = TheAppCrashHandler.getInstance();
        m_CrashHandler.init(this);

        //初始化对话框风格
//        Material 风格对应 DialogSettings.STYLE_MATERIAL，
//        Kongzue 风格对应 DialogSettings.STYLE_KONGZUE，
//        iOS 风格对应 DialogSettings.STYLE_IOS
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.isUseBlur = false;

        registerActivityLifecycleCallbacks(this);

        //初始化环信客服
        HyphenateHelper.initOnlyIM(this);

        //启用腾讯直播调试
        initTxlive();
        //腾讯bugly
      /*  第三个参数为SDK调试模式开关，调试模式的行为特性如下：
        输出详细的Bugly SDK的Log；
        每一条Crash都会被立即上报；
        自定义日志将会在Logcat中输出。
        建议在测试阶段建议设置成true，发布时设置为false。*/
        CrashReport.initCrashReport(getApplicationContext(), "19deb969cb", true);
    }

    private void initTxlive() {
        String licenceURL = "http://license.vod2.myqcloud.com/license/v1/621f9220ff1609fc57c930a6ff8c0366/TXLiveSDK.licence";
        String licenceKey = "5e93ca4a70d8fe8fa94b79b3a8c19fc4";
        String sdkver = TXLiveBase.getSDKVersionStr();
        TXLiveBase.setConsoleEnabled(true);
        TXLiveBase.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
        TXLiveBase.getInstance().setLicence(this,licenceURL,licenceKey);
        LogUtils.e(TAG, "liteav sdk version is : " + sdkver);
    }

    public static KuPaiLiveApplication getInstance() {
        if (mApplication == null) {
            mApplication = new KuPaiLiveApplication();
        }
        return mApplication;
    }

    static {
        //设置全局的Header构建器

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader
            createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_F5F5F5, R.color.color_333333);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                layout.setPrimaryColorsId(R.color.color_F5F5F5, R.color.color_333333);//全局设置主题颜色
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityManagerUtils.addOneActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityManagerUtils.removeClear(activity);
    }
}
