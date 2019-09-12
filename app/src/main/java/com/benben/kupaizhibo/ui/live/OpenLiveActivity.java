package com.benben.kupaizhibo.ui.live;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.FileUtils;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.TCUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.GuardSeatListAdapter;
import com.benben.kupaizhibo.adapter.LiveChatMessageAdapter;
import com.benben.kupaizhibo.adapter.MessageListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.GuardSeatListBean;
import com.benben.kupaizhibo.bean.LiveRoomBean;
import com.benben.kupaizhibo.bean.StopLiveResultBean;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.bean.socket.CTMessageBean;
import com.benben.kupaizhibo.bean.socket.SendMessageUtils;
import com.benben.kupaizhibo.bean.socket.SocketResponseBodyBean;
import com.benben.kupaizhibo.bean.socket.SocketResponseHeaderBean;
import com.benben.kupaizhibo.config.Constants;
import com.benben.kupaizhibo.config.SystemDir;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.NormalWebViewActivity;
import com.benben.kupaizhibo.ui.mine.MyInvitationActivity;
import com.benben.kupaizhibo.utils.SocketIoUtils;
import com.benben.kupaizhibo.widget.BeautyFilterPopupWindow;
import com.benben.kupaizhibo.widget.RollAdsLayout;
import com.benben.kupaizhibo.widget.UserInfoDialog;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.UMShareAPI;

import org.apache.commons.lang3.StringEscapeUtils;
import org.limlee.hiframeanimationlib.FrameAnimationView;
import org.limlee.hiframeanimationlib.FrameDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qssq666.giftmodule.bean.GiftDemoModel;
import cn.qssq666.giftmodule.bean.UserDemoInfo;
import cn.qssq666.giftmodule.interfacei.GiftModelI;
import cn.qssq666.giftmodule.interfacei.UserInfoI;
import cn.qssq666.giftmodule.periscope.GiftAnimLayout;
import okhttp3.Call;

/**
 * 功能:开直播
 * create by zjn on 2019/5/14 0014
 * email:168455992@qq.com
 */
public class OpenLiveActivity extends BaseActivity implements SocketCallbackListener {

    private static final String TAG = "OpenLiveActivity";

    @BindView(R.id.video_view)
    TXCloudVideoView mCaptureView;
    @BindView(R.id.civ_user_photo)
    CircleImageView civUserPhoto;
    @BindView(R.id.tv_user_nickname)
    TextView tvUserNickname;
    @BindView(R.id.tv_other_info)
    TextView tvOtherInfo;
    @BindView(R.id.llyt_user_info)
    LinearLayout llytUserInfo;
    @BindView(R.id.rlyt_close)
    RelativeLayout rlytClose;
    @BindView(R.id.rlv_chat_info)
    RecyclerView rlvChatInfo;
    @BindView(R.id.iv_volume)
    ImageView ivVolume;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.iv_beauty)
    ImageView ivBeauty;
    @BindView(R.id.iv_sharing)
    ImageView ivSharing;
    @BindView(R.id.galt_little_gift)
    GiftAnimLayout galtLittleGift;
    @BindView(R.id.frame_animation)
    FrameAnimationView frameAnimation;

    //顶部闪屏消息提示
    @BindView(R.id.roll_ads_msg)
    RollAdsLayout rollAdsMsg;
    @BindView(R.id.tv_viewer_number)
    TextView tvViewerNumber;
    private static final int REFRESH_RECYCLERVIEW = 0;
    //显示礼物动画
    private static final int SHOW_GIFT_ANMITION = 2;
    //发送红包
    private static final int SHOW_RED_ENVELOPES_VIEW = 3;
    //刷新进入房间的用户列表
    private static final int REFRESH_ENTER_ROOM_USER = 1;
    @BindView(R.id.tv_current_profit)
    TextView tvCurrentProfit;
    private float mCurrentProfit = 0f;
    private TXLivePushConfig mLivePushConfig;
    private LiveRoomBean mLiveRoomBean;
    private TXLivePusher mLivePusher;
    private boolean mFlashTurnOn = false;
    private boolean mHWVideoEncode = true;
    private boolean mFrontCamera = true;
    //美颜等级设置，默认
//    private int  mBeautyLevel = 5;
//    private int  mWhiteningLevel = 3;
//    private int  mRuddyLevel = 2;
    //是否静音
    private boolean mPusherMute = false;
    private LiveChatMessageAdapter mLiveChatMessageAdapter;
    //消息列表容器
    private LinkedList<SocketResponseHeaderBean> mResponseHeaderBeanList = new LinkedList<>();
    //最近一条消息
    private List<SocketResponseHeaderBean> mLastResponseHeaderBeanList;
    //观看时长
    private long mSecond = 0;
    //观看人数
    private long mLookNum = 0;
    private Timer mBroadcastTimer;
    private BroadcastTimerTask mBroadcastTimerTask;
    private TextView tvZhiboShouyi;
    private TextView tvHuodeFensi;
    private TextView tvZhiboLookNum;
    private TextView tvZhiboTime;
    //美颜级别
    private int whiteningLevel = 0;
    //美白级别
    private int beautyLevel = 0;
    //守护坐席页数
    private int guardPage = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_RECYCLERVIEW:
                    mLiveChatMessageAdapter.clear();
                    mLiveChatMessageAdapter.appendToList(mResponseHeaderBeanList);
                    rlvChatInfo.smoothScrollToPosition(mResponseHeaderBeanList.size() - 1);
                    break;
                case REFRESH_ENTER_ROOM_USER:
                    refreshOtherInfo();
                    break;
                case SHOW_GIFT_ANMITION:
                    SocketResponseBodyBean socketResponseBodyBean = ((SocketResponseBodyBean) msg.getData().getSerializable("gift_info_list"));
                    showGiftAnimation(socketResponseBodyBean);
                    break;
                case SHOW_RED_ENVELOPES_VIEW:

                    break;
            }
        }
    };


    private AlertDialog mCloseLiveDialog;
    //推流时间点 几分钟之后
    private String timeLater;
    private CountDownTimer mCountDownTimer;
    private String rtmpUrl;
    private boolean isRefresh;
    private GuardSeatListAdapter mGuardSeatListAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_live;
    }


    private void refreshOtherInfo() {
        LogUtils.e(TAG, "refreshOtherInfo  lookNum = " + mLookNum);
        tvOtherInfo.setText((StringUtils.isEmpty(mLiveRoomBean.getCity()) ? "未知" : mLiveRoomBean.getCity()) + "  |  "
                + mLiveRoomBean.getFollow_num() + "粉丝");
        tvViewerNumber.setText(getString(R.string.viewer_number, String.valueOf(mLookNum)));

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initData() {
        //设置顶部消息仅播放一次
        rollAdsMsg.setLoop(false);

        mLiveRoomBean = ((LiveRoomBean) getIntent().getSerializableExtra("liveInfo"));
        timeLater = getIntent().getStringExtra("time_later");
        LinearLayoutManager vm = new LinearLayoutManager(mContext);
        vm.setOrientation(LinearLayoutManager.VERTICAL);
        rlvChatInfo.setLayoutManager(vm);
        //设置回收复用池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(RecyclerView.VERTICAL, 20);
        rlvChatInfo.setRecycledViewPool(recycledViewPool);
        mLiveChatMessageAdapter = new LiveChatMessageAdapter(mContext, mLiveRoomBean.getStream());
        rlvChatInfo.setAdapter(mLiveChatMessageAdapter);
        if (mLiveRoomBean != null) {
            SocketIoUtils.getInstance()
                    .setSocketCallbackListener(this) //回调监听初始化
                    .connect(mLiveRoomBean.getSocket_url());
            initLivePush();
            refreshUI();


        }

        //初始化giftAnimLayout
        MessageListAdapter.LookLiveGiftBarAdapter lookLiveGiftBarAdapter = new MessageListAdapter.LookLiveGiftBarAdapter(mContext);
        galtLittleGift.setGiftAdapterAndCallBack(lookLiveGiftBarAdapter);//具体作用看GiftBarAdapter注释
        galtLittleGift.setMaxShowCount(3);
        galtLittleGift.setHidenAnim(R.anim.follow_anim_from_left_vertical_hidden);//  giftAnimLayout.setHidenAnim(R.anim.follow_anim_from_left_to_right_hidden);
        galtLittleGift.setShowDuration(6000);
        galtLittleGift.setMustAnimHide(true);
        galtLittleGift.setAllowAcrossAnimBug(false);
        galtLittleGift.setThanQueueClearFirstAndNotAddQueue(false);
        galtLittleGift.setCacheVewCount(3);
        galtLittleGift.setThanMaxShowClearZero(true);

        galtLittleGift.setOnGiftBarFaceClick(new GiftAnimLayout.OnGiftBarFaceClick() {
            @Override
            public void onClick(UserInfoI userInfo) {
                //点击礼物的事件
                // ToastUtils.show(mContext,userInfo.getName());
            }
        });


        //播放一次
        frameAnimation.setOneShot(true);
        //播放状态的监听
        frameAnimation.setOnFrameListener(new FrameAnimationView.OnFrameListener() { //添加监听器
            @Override
            public void onFrameStart() {
                Log.e(TAG, "帧动画播放开始！");
                frameAnimation.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFrameEnd() {
                Log.e(TAG, "帧动画播放结束！");
                frameAnimation.setVisibility(View.INVISIBLE);

            }
        });

//        //大眼
//        mLivePusher.setEyeScaleLevel();
//        //瘦脸
//        mLivePusher.setFaceSlimLevel();
//        mLivePusher.setFilter();
    }


    //显示礼物动画
    private void showGiftAnimation(SocketResponseBodyBean receiveGiftInfoBean) {
        //送礼物的用户信息
        UserDemoInfo userDemoInfo = new UserDemoInfo();
        //昵称
        userDemoInfo.setName(receiveGiftInfoBean.getUname());
        //id
        userDemoInfo.setUserId(receiveGiftInfoBean.getUid());
        //头像
        userDemoInfo.setPortraitUri(receiveGiftInfoBean.getUhead()); //头像
        LogUtils.e(TAG, receiveGiftInfoBean.getCt());

        CTMessageBean ctMessageBean = JSONUtils.jsonString2Bean(receiveGiftInfoBean.getCt(), CTMessageBean.class);

        try { //当前收益
            mCurrentProfit += Float.parseFloat(ctMessageBean.getTotal_fee());
            tvCurrentProfit.setText(String.valueOf(mCurrentProfit));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        GiftModelI model = new GiftDemoModel();
        //礼物id
        ((GiftDemoModel) model).setId(Integer.parseInt(ctMessageBean.getGift_id()));
        //礼物icon
        ((GiftDemoModel) model).setImage(ctMessageBean.getGift_thumb());
        //礼物名称
        ((GiftDemoModel) model).setTitle(ctMessageBean.getGift_name());

        //显示礼物动画
        galtLittleGift.showNewGift(mContext, userDemoInfo, model);

        //豪华礼物
        if (!StringUtils.isEmpty(ctMessageBean.getZip_addr())) {
            //豪华礼物，顶部添加消息提示
            rollAdsMsg.addOneAds(receiveGiftInfoBean.getUname() + ":送了一个" + ctMessageBean.getGift_name());

            List<FrameDrawable> frameDrawables = new ArrayList<>();


            String dirPath = SystemDir.DIR_GIFT_FILE + "/" + ctMessageBean.getZip_name() + "/icon";
            Log.e(TAG, "handleMessage: dirPath = " + dirPath);

            List<String> imageList = FileUtils.getPictures(dirPath);

            if (imageList != null) {
                Log.e(TAG, "handleMessage: imageList = " + imageList);


                for (String framePath : imageList) {
                    FrameDrawable frameDrawable = new FrameDrawable(framePath, 100);
                    frameDrawables.add(frameDrawable);
                }
                Log.e(TAG, "handleMessage: add(frameDrawable) ");

                if (frameAnimation.isRunning()) {
                    frameAnimation.stop();
                }
                frameAnimation.addFrameDrawable(frameDrawables);
                frameAnimation.setVisibility(View.VISIBLE);
                frameAnimation.start();
            }
        }
    }

    /**
     * 功能:更新直播状态
     */
    private void updateLiveStatus(int anchorId) {
        BaseOkHttpClient.newBuilder().addParam("anchor_id", anchorId)
                .addParam("live_status", "1")
                .url(NetUrlUtils.LIVES_CHANGE_STATUS)
                .json().post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {

            }

            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void refreshUI() {
        tvUserNickname.setText(mLiveRoomBean.getNickname() + "");

        tvOtherInfo.setText((StringUtils.isEmpty(mLiveRoomBean.getCity()) ? "未知" : mLiveRoomBean.getCity()) + "  |  "
                + mLiveRoomBean.getFollow_num() + "个粉丝");
        tvViewerNumber.setText(getString(R.string.viewer_number, String.valueOf(mLiveRoomBean.getView_num())));
        ImageUtils.getPic(mLiveRoomBean.getAvatar(), civUserPhoto,
                mContext, R.mipmap.icon_default_header);
        tvCurrentProfit.setText(StringUtils.getWanStr(mLiveRoomBean.getEarnings()));
    }

    private void initLivePush() {
        mLivePusher = new TXLivePusher(mContext);
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.setVideoEncodeGop(5);
        mLivePushConfig.setTouchFocus(false);
//        mLivePushConfig.setBeautyFilter(mBeautyLevel, mWhiteningLevel, mRuddyLevel);
        mLivePusher.setConfig(mLivePushConfig);
        //b true 流畅优先 false 画质优先 b true 动态分辨率 false 固态分辨率
        mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, true, true);
        //设置美颜
//        mLivePusher.setBeautyFilter();
        //开启硬件加速
        if (mHWVideoEncode) {
            if (mLivePushConfig != null) {
                if (Build.VERSION.SDK_INT < 18) {
                    mHWVideoEncode = false;
                }
            }
        }
        mLivePushConfig.setHardwareAcceleration(mHWVideoEncode ?
                TXLiveConstants.ENCODE_VIDEO_HARDWARE : TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
        mLivePusher.setConfig(mLivePushConfig);
        rtmpUrl = mLiveRoomBean.getRtmp();
        switch (timeLater){
            case "0":
                startCountDown(0);
                break;
            case "1":
                startCountDown(3*60000);
                break;
            case "2":
                startCountDown(5*60000);
                break;
            case "3":
                startCountDown(10*60000);
                break;
        }


        mLivePusher.startCameraPreview(mCaptureView);
        startRecord();
    }

    //开播倒计时
    private void startCountDown(long time_length) {

        mCountDownTimer = new CountDownTimer(time_length, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                toast(mContext.getResources().getString(R.string.live_start));
                //开始推流
                mLivePusher.startPusher(rtmpUrl);
                updateLiveStatus(mLiveRoomBean.getUser_id());
            }
        };
        mCountDownTimer.start();
    }
    /**
     * 开启红点与计时动画
     */
    private void startRecord() {
        //直播时间
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    /**
     * 关闭红点与计时动画
     */
    private void stopRecord() {
        //直播时间
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
    }

    @OnClick({R.id.civ_user_photo, R.id.rlyt_close, R.id.iv_volume, R.id.iv_camera,
            R.id.iv_sharing, R.id.iv_beauty, R.id.iv_gift,R.id.iv_guard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_user_photo://头像
                //跳转到用户主页
                //获取用户信息 弹出用户信息的dialog
                // getUserInfo();
                break;
            case R.id.rlyt_close://关直播
                stopRtmpPublish();
                break;
            case R.id.iv_volume://麦克风关闭开启
                mPusherMute = !mPusherMute;
                if (mLivePusher != null) {
                    mLivePusher.setMute(mPusherMute);
                }
                ivVolume.setImageResource(mPusherMute ? R.mipmap.icon_microphone_close : R.mipmap.icon_microphone);
                break;
            case R.id.iv_camera://切换前后摄像头
                // 默认是前置摄像头
                if (mFrontCamera) {
                    mFrontCamera = false;
                } else {
                    mFrontCamera = true;
                }
                mLivePusher.switchCamera();
                break;
         /*   case R.id.rlyt_shanguang:
                //mFlashTurnOn为true表示打开，否则表示关闭
                if (!mFrontCamera) {
                    mFlashTurnOn = !mFlashTurnOn;
                    mLivePusher.turnOnFlashLight(mFlashTurnOn);
                    ivShanguang.setImageResource(mFlashTurnOn ? R.mipmap.icon_shanguangk : R.mipmap.icon_shanguang);
                }
                break;*/
            case R.id.iv_sharing://分享
                //测试
                openSelectSharePop();
                /*//获取分享码
                getShowInviteCode();*/
                break;
            case R.id.iv_beauty://美颜
                showBeautyPop();
                break;
            case R.id.iv_gift://礼物
                break;
            case R.id.iv_guard://守护
                if(mLiveRoomBean == null ||StringUtils.isEmpty(mLiveRoomBean.getStream())){
                    toast("获取直播间信息失败，请重试");
                    return;
                }
                showGuardDialog();
                break;
        }
    }


    //守护坐席的弹窗
    private void showGuardDialog() {
        View guardSeatView = View.inflate(mContext, R.layout.dialog_guard_seat_list, null);

        AlertDialog guardSeatDialog = new AlertDialog.Builder(mContext)
                .setView(guardSeatView)
                .create();
        Window window = guardSeatDialog.getWindow();
        if (window != null) {
            //去除系统自带的margin
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置dialog在界面中的属性
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        SmartRefreshLayout refreshLayout = guardSeatView.findViewById(R.id.refresh_layout);
        LinearLayout llytNoData = guardSeatView.findViewById(R.id.llyt_no_data);
        llytNoData.setVisibility(View.GONE);
        refreshLayout.setPrimaryColorsId(R.color.transparent);
        RecyclerView rlvGuardList = guardSeatView.findViewById(R.id.rlv_guard_list);
        rlvGuardList.setLayoutManager(new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false));
        mGuardSeatListAdapter = new GuardSeatListAdapter(mContext);
        rlvGuardList.setAdapter(mGuardSeatListAdapter);
        TextView tvRecharge = guardSeatView.findViewById(R.id.tv_sure);
        TextView tvGuardPrerogative = guardSeatView.findViewById(R.id.tv_guard_prerogative);
        //获取守护列表
        getGuardSeatList(refreshLayout, guardPage);
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            guardPage = 1;
            isRefresh = true;
            getGuardSeatList(refreshLayout1, guardPage);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout2 -> {
            guardPage ++;
            isRefresh = false;
            getGuardSeatList(refreshLayout2, guardPage);
        });
        //开通守护
        tvRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              toast("不能在自己的直播间开通守护");
            }
        });
        //守护特权
        tvGuardPrerogative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGuardDescription();
            }
        });
        guardSeatDialog.show();
    }

    //获取守护坐席列表
    private void getGuardSeatList(RefreshLayout refreshLayout, int page) {
        BaseOkHttpClient.newBuilder()
                .addParam("stream", mLiveRoomBean.getStream())
                .addParam("page", page)
                .url(NetUrlUtils.GET_GUARD_LIST)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取守护坐席列表----onSuccess:" + json);
                List<GuardSeatListBean> guardSeatList = JSONUtils.jsonString2Beans(JSONUtils.getNoteJson(json,"lists"), GuardSeatListBean.class);
                if (isRefresh) {
                    mGuardSeatListAdapter.refreshList(guardSeatList);
                } else {
                    mGuardSeatListAdapter.appendToList(guardSeatList);
                }
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取守护坐席列表----onError:" + msg);
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取守护坐席列表----onFailure:" + e.getMessage());

                refreshLayout.finishRefresh(false);
                refreshLayout.finishLoadMore(false);
            }
        });
    }

    //获取守护说明
    private void getGuardDescription() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_GUARD_DESCRIPTION)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取守护特权说明----onSuccess:" + json);
                String noteJson = JSONUtils.getNoteJson(json, "content");
                NormalWebViewActivity.startWithData(mContext, noteJson, getString(R.string.guard_prerogative), true, true);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取守护特权说明----onError:" + msg);
                ToastUtils.show(mContext,msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取守护特权说明----onFailure:" + e.getMessage());
            }
        });
    }

    //选择美颜功能
    private void showBeautyPop() {

        BeautyFilterPopupWindow beautyFilterPopupWindow = new BeautyFilterPopupWindow(mContext, whiteningLevel, beautyLevel);
        beautyFilterPopupWindow.setOnProgressChangeListener(new BeautyFilterPopupWindow.OnProgressChangeListener() {
            @Override
            public void OnWhiteningLevelChangeListener(int level) {
                /*beautyLevel	int	美颜级别，取值范围0 - 9； 0表示关闭，1 - 9值越大，效果越明显。
                whiteningLevel	int	美白级别，取值范围0 - 9；0表示关闭，1 - 9值越大，效果越明显。
                ruddyLevel	int	红润级别，取值范围0 - 9；0表示关闭，1 - 9值越大，效果越明显。*/
                whiteningLevel = level;
                mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, beautyLevel / 9, whiteningLevel / 9, 0);
            }

            @Override
            public void OnBeautyLevelChangeListener(int level) {
                beautyLevel = level;
                mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, beautyLevel / 9, whiteningLevel / 9, 0);

            }
        });
        beautyFilterPopupWindow.showWindow(getWindow().getDecorView().getRootView());


    }

    //获取分享码
    private void getShowInviteCode() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.SHOW_INVITE_CODE)
                .get()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                String share_url = JSONUtils.getNoteJson(result, "share_url");
                if (!StringUtils.isEmpty(share_url)) {
                    //选择分享平台
                    openSelectSharePop();
                } else {
                    toast("邀请码获取失败");
                }
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

    //获取主播信息
    private void getUserInfo() {

        if (mLiveRoomBean == null) {
            return;
        }
        BaseOkHttpClient.newBuilder()
                .addParam("uid", mLiveRoomBean.getUser_id())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "直播用户信息dialog----onSuccess: result=" + result + "----msg=" + msg);
                UserInfoBean mUserInfoBean = JSONUtils.jsonString2Bean(result, UserInfoBean.class);
                UserInfoDialog zhuBoInfoDialog = UserInfoDialog.getInstance();
                boolean isFollow = mUserInfoBean.getFollow() == 1;
                //    zhuBoInfoDialog.showZhuBoDialog(mContext, mUserInfoBean, isFollow);
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

    //结束推流并进行清理
    public void stopRtmpPublish() {
        MessageDialog.show(mContext, getString(R.string.end_of_live), getString(R.string.confirm_end_of_live), mContext.getResources().getString(R.string.positive), mContext.getResources().getString(R.string.negative))
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        closeLive();
                        return false;
                    }
                }).show();


    }

    private void closeLive() {

        //获取直播统计信息

        BaseOkHttpClient.newBuilder()
                .addParam("anchor_id", mLiveRoomBean.getUser_id() + "")
                .addParam("stream", mLiveRoomBean.getStream())
                .addParam("is_initiative_close", "1")
                .url(NetUrlUtils.LIVES_STOP_ROOM)
                .json().post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "关播----onSuccess:" + msg);
                //发送关播消息
                SocketIoUtils.getInstance().sendMsg(Constants.EVENT_BROAD_CAST,
                        SendMessageUtils.getCloseLiveMsgBean());
                getLiveStopMsg();
                stopRecord();

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "关播----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "关播----onFailure:" + e.getMessage());

            }
        });
    }

    //获取直播统计信息
    private void getLiveStopMsg() {
        BaseOkHttpClient.newBuilder()
                .addParam("anchor_id", String.valueOf(mLiveRoomBean.getUser_id()))
                .addParam("stream", mLiveRoomBean.getStream())
                .url(NetUrlUtils.LIVES_GET_STOP_MSG)
                .json().post()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {

                if (!StringUtils.isEmpty(result)) {
                    StopLiveResultBean data = JSONUtils.jsonString2Bean(result, StopLiveResultBean.class);
                    openStopLiveResultInfoPop(data);
                } else {
                    toast("直播统计数据获取失败！");
                    openStopLiveResultInfoPop(null);
                }
            }

            @Override
            public void onError(int code, String msg) {
                ToastUtils.show(mContext, msg);
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });

    }

    /**
     * 打开直播结束结果信息统计
     */
    private void openStopLiveResultInfoPop(StopLiveResultBean data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置dialog主题
        mCloseLiveDialog = builder.create();
        mCloseLiveDialog.setCanceledOnTouchOutside(false);
        View dialogAlertView = mContext.getLayoutInflater().inflate(R.layout.pop_live_profit, null);
        handleLogic(dialogAlertView, data);
        mCloseLiveDialog.setView(dialogAlertView); // 设置view 和左上右下的边距
        Window window = mCloseLiveDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCloseLiveDialog.show();

    }

    private void handleLogic(View contentView, StopLiveResultBean data) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_sure:
                        mCloseLiveDialog.dismiss();
                        LogUtils.e(TAG, "stop_begin");
                        if (mLivePusher != null) {
                            LogUtils.e(TAG, "stop_mLivePusher != null");
                            mLivePusher.setPushListener(null);
                            mLivePusher.stopPusher();
                            mLivePusher.stopScreenCapture();
                            mLivePusher.stopCameraPreview(true);
                        }
                        LogUtils.e(TAG, "stop_over");

                        finish();
                        break;
                }
            }
        };
        tvZhiboShouyi = contentView.findViewById(R.id.tv_zhibo_shouyi);
        tvHuodeFensi = contentView.findViewById(R.id.tv_huode_fensi);
        tvZhiboLookNum = contentView.findViewById(R.id.tv_zhibo_look_num);
        tvZhiboTime = contentView.findViewById(R.id.tv_zhibo_time);
        if (data != null) {
            tvZhiboShouyi.setText(mContext.getResources().getString(R.string.live_profit, data.getTotal_fee()));
            tvHuodeFensi.setText(mContext.getResources().getString(R.string.get_fans, String.valueOf(data.getFans_number())));
            tvZhiboLookNum.setText(mContext.getResources().getString(R.string.viewer_total, String.valueOf(data.getNums())));
            tvZhiboTime.setText(mContext.getResources().getString(R.string.live_total_time, data.getTimes()));
        } else {
            tvZhiboShouyi.setText(mContext.getResources().getString(R.string.live_profit, "0"));
            tvHuodeFensi.setText(mContext.getResources().getString(R.string.get_fans, "0"));
            tvZhiboLookNum.setText(mContext.getResources().getString(R.string.viewer_total, "0"));
            tvZhiboTime.setText(mContext.getResources().getString(R.string.live_total_time, TCUtils.formattedTime(0)));
        }

        contentView.findViewById(R.id.tv_sure).setOnClickListener(listener);
    }

    /*打开分享弹窗*/
    private void openSelectSharePop() {
        startActivityForResult(new Intent(mContext, MyInvitationActivity.class), 102);
    }


    @Override
    public void onConnect(Object... args) {
        //开始握手
        String json = JSONUtils.toJsonString(mLiveRoomBean.getSocket_handle());
        SocketIoUtils.getInstance().sendMsg(Constants.EVENT_HANDSHAKE, json);
    }

    @Override
    public void onDisConnect(Object... args) {

    }

    @Override
    public void onBroadCastingListener(Object... args) {
        for (int i = 0; i < args.length; i++) {
            LogUtils.e(TAG, "广播信息 " + args[i]);
            if (!StringUtils.isEmpty(args[i].toString())) {
                if ("[\"stopplay\"]".equals(args[i].toString())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("您被超管强制下播了");
                            finish();
                        }
                    });
                } else {
                    //处理消息
                    String json = getUnescapeJson(args[i].toString());
                    LogUtils.e(TAG, "onBroadCastingListener:  json=" + json);
                    disposeBroadCastMessage(json);
                }
            }
        }
    }

    private void disposeBroadCastMessage(String json) {
        mLastResponseHeaderBeanList = JSONUtils.jsonString2Beans(json, SocketResponseHeaderBean.class);
        if (mLastResponseHeaderBeanList != null && !mLastResponseHeaderBeanList.isEmpty()) {

            for (int i = 0; i < mLastResponseHeaderBeanList.size(); i++) {
                //取第一条
                if ("SendMsg".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                    //聊天消息
                    if ("2".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())) {
                        //直接取ct
                        //聊天消息
                        mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                    } else if ("0".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())) {
                        mLookNum++;
                        LogUtils.e(TAG, "SendMsg   looknum = " + mLookNum);

                        mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                        mHandler.sendEmptyMessage(REFRESH_ENTER_ROOM_USER);
                    }
                } else if ("SystemNot".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                    //系统消息
                    mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                } else if ("disconnect".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_()) &&
                        "1".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getAction()) &&
                        "0".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())
                ) {
                    LogUtils.e(TAG, "disconnect   looknum = " + mLookNum);
                    //退出房间
                    if (mLookNum > 0) {
                        mLookNum--;
                        mHandler.sendEmptyMessage(REFRESH_ENTER_ROOM_USER);
                    }
                } else if ("SendGift".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                    if (mLastResponseHeaderBeanList == null || mLastResponseHeaderBeanList.size() <= 0) {
                        return;
                    }

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("gift_info_list", mLastResponseHeaderBeanList.get(i).getMsg().get(0));
                    message.setData(bundle);
                    message.what = SHOW_GIFT_ANMITION;
                    mHandler.sendMessage(message);
                    mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                }
                mHandler.sendEmptyMessage(REFRESH_RECYCLERVIEW);
            }
        }
    }

    @Override
    public void onHandShakeListener(Object... args) {
        LogUtils.e(TAG, "握手成功");
    }

    private String getUnescapeJson(String toString) {
        //先去掉前后中括号
        String json = toString.substring(2, toString.length() - 2);
        json = "\"[" + json + "]\"";
        json = StringEscapeUtils.unescapeJson(json);
        json = json.replaceAll("\"\\{", "\\{");
        json = json.replaceAll("\\}\"", "\\}");
        json = json.substring(1, json.length() - 1);
        return json;
    }


    /**
     * 记时器
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            ++mSecond;
        }
    }

    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
        stopRtmpPublish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        frameAnimation.stop();
        frameAnimation.setOnFrameListener(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
