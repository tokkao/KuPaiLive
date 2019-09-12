package com.benben.kupaizhibo;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.RxBus;
import com.benben.commoncore.utils.StatusBarUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ThreadPoolUtils;
import com.benben.commoncore.widget.badgeview.BGABadgeTextView;
import com.benben.commoncore.widget.badgeview.BGABadgeViewHelper;
import com.benben.kupaizhibo.adapter.MainViewPagerAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.AuthStatusBean;
import com.benben.kupaizhibo.bean.UnReadMsgCountBean;
import com.benben.kupaizhibo.bean.socket.GiftCategoryInfoBean;
import com.benben.kupaizhibo.frag.MainFollowFragment;
import com.benben.kupaizhibo.frag.MainHomeFragment;
import com.benben.kupaizhibo.frag.MainMessageFragment;
import com.benben.kupaizhibo.frag.MainMineFragment;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.http.DownloadUtil;
import com.benben.kupaizhibo.ui.live.CreateLiveActivity;
import com.benben.kupaizhibo.ui.mine.AnchorAuthenticationActivity;
import com.benben.kupaizhibo.utils.LoginCheckUtils;
import com.benben.kupaizhibo.widget.NoScrollViewPager;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.kongzue.dialog.v3.MessageDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity_log";
    @BindView(R.id.vpMain)
    NoScrollViewPager vpMain;
    @BindView(R.id.tv_tab_home)
    TextView tvTabHome;
    @BindView(R.id.tv_tab_follow)
    TextView tvTabFollow;
    @BindView(R.id.rlyt_open_live)
    RelativeLayout rlytOpenLive;
    @BindView(R.id.tv_tab_message)
    BGABadgeTextView tvTabMessage;
    @BindView(R.id.tv_tab_mine)
    TextView tvTabMine;
    @BindView(R.id.llyt_tab)
    LinearLayout llytTab;
    //底部tab数据
    private ArrayList<TextView> tabs;
    //底部导航栏的高度
    private int mNavigationBarHeight = 0;
    //上次点击返回键时间，双击返回使用
    private long mLastTime = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
      /*  LinearLayout.LayoutParams bottomlayoutParams = (LinearLayout.LayoutParams) llytTab.getLayoutParams();
        bottomlayoutParams.setMargins(0, 0, 0, mNavigationBarHeight);
        llytTab.setLayoutParams(bottomlayoutParams);*/

        //设置消息红点颜色
        tvTabMessage.getBadgeViewHelper().setBadgeGravity(BGABadgeViewHelper.BadgeGravity.RightTop);
        tvTabMessage.getBadgeViewHelper().setBadgeBgColorInt(getResources().getColor(R.color.color_FF6261));


        tvTabHome.setSelected(true);
        tabs = new ArrayList<>();
        tabs.add(tvTabHome);
        tabs.add(tvTabFollow);
        tabs.add(tvTabMessage);
        tabs.add(tvTabMine);

        List<LazyBaseFragments> mFragmentList = new ArrayList<>();
        mFragmentList.add(MainHomeFragment.getInstance());
        mFragmentList.add(MainFollowFragment.getInstance());
        mFragmentList.add(MainMessageFragment.getInstance());
        mFragmentList.add(MainMineFragment.getInstance());

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        vpMain.setAdapter(mainViewPagerAdapter);
        vpMain.setNoScroll(true);
        //检查权限
        checkPermissions();

        RxBus.getInstance().toObservable(UnReadMsgCountBean.class)
                .subscribe(new Observer<UnReadMsgCountBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UnReadMsgCountBean mUnReadMsgCountBean) {
                        //刷新未读消息数量
                        if(mUnReadMsgCountBean.getCount() <= 0){
                            tvTabMessage.hiddenBadge();

                        }else {
                            tvTabMessage.showTextBadge(mUnReadMsgCountBean.getCount()+"");

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        //获取礼物列表
        getGiftList();
    }


    //检查权限
    private void checkPermissions() {
        String[] needPermissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        XXPermissions.with(mContext)
                .permission(needPermissions)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        //只有获取到存储权限才可以更新软件
                       // updateVersion();
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }

    /*获取礼物列表*/
    private void getGiftList() {
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.LIVES_GET_GIFT)
                .get().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                if (!StringUtils.isEmpty(result)) {
                    try {
                        List<GiftCategoryInfoBean> mGiftCategoryInfoBeanList = JSONUtils.jsonString2Beans(result, GiftCategoryInfoBean.class);
                        if (mGiftCategoryInfoBeanList != null && !mGiftCategoryInfoBeanList.isEmpty()) {
                            ThreadPoolUtils.newInstance().execute(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < mGiftCategoryInfoBeanList.size(); i++) {
                                        for (int j = 0; j < mGiftCategoryInfoBeanList.get(i).getGift().size(); j++) {
                                            if (!StringUtils.isEmpty(mGiftCategoryInfoBeanList.get(i).getGift().get(j).getZip_addr())) {
                                                DownloadUtil.get().downLoadGiftImagFile(mGiftCategoryInfoBeanList.get(i).getGift().get(j).getZip_addr(),
                                                        mGiftCategoryInfoBeanList.get(i).getGift().get(j).getZip_name()
                                                );
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });

    }

    @OnClick({R.id.tv_tab_home, R.id.tv_tab_follow, R.id.rlyt_open_live, R.id.rlyt_message, R.id.tv_tab_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tab_home:
                selectTab(0);
                break;
            case R.id.tv_tab_follow:
                selectTab(1);
                break;
            case R.id.rlyt_message:
                selectTab(2);
                break;
            case R.id.tv_tab_mine:
                selectTab(3);
                break;
            case R.id.rlyt_open_live:
                if (!LoginCheckUtils.checkLoginShowDialog(mContext)) return;
                getAuthStatus();
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
                            toast(mContext.getResources().getString(R.string.server_exception));
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
                        if (lstAuthStatus == null) {
                            showAuthDialog();
                            return;
                        }
                        //-1失败，0待审核，1成功
                        if (lstAuthStatus.getType() == -1 || lstAuthStatus.getType() == 0) {
                            startActivity(new Intent(mContext, AnchorAuthenticationActivity.class).putExtra("auth_info", lstAuthStatus));
                        } else {
                            startActivity(new Intent(mContext, CreateLiveActivity.class));
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        showAuthDialog();
                        LogUtils.e(TAG, "获取认证状态----onError:" + msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, "获取认证状态----onFailure:" + e.getMessage());
                    }
                });
    }

    private void showAuthDialog() {
        //前往认证
        MessageDialog.show(this, getResources().getString(R.string.no_permission_create_live), getResources().getString(R.string.immediately_go_to_authentication), getResources().getString(R.string.go_to_authentication), getResources().getString(R.string.negative))
                .setOnOkButtonClickListener((baseDialog, v) -> {
                    startActivity(new Intent(mContext, AnchorAuthenticationActivity.class).putExtra("enter_type", 0));
                    return false;
                }).show();
    }

    //修改选中tab的状态
    private void selectTab(int position) {
        for (int i = 0; i < tabs.size(); i++) {
            if (i == position) {
                vpMain.setCurrentItem(position);
                tabs.get(i).setSelected(true);
                tabs.get(i).setTextColor(getResources().getColor(R.color.color_FF6261));
            } else {
                tabs.get(i).setSelected(false);
                tabs.get(i).setTextColor(getResources().getColor(R.color.color_737373));
            }
        }
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, position == 2);
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setColor(mContext, R.color.transparent);
     //   mNavigationBarHeight = StatusBarUtils.getNavigationBarHeight(mContext);
    }

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间

        if ((mNowTime - mLastTime) > 2000) {//比较两次按键时间差
            Toast.makeText(this, getResources().getString(R.string.press_again_exit_app), Toast.LENGTH_SHORT).show();
            mLastTime = mNowTime;
        } else {//退出程序
            this.finish();
            System.exit(0);
        }
    }

}
