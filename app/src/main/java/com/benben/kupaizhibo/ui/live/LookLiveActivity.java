package com.benben.kupaizhibo.ui.live;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.GuardSeatListAdapter;
import com.benben.kupaizhibo.adapter.GuardTypeListAdapter;
import com.benben.kupaizhibo.adapter.LiveChatMessageAdapter;
import com.benben.kupaizhibo.adapter.MessageListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.FollowResultBean;
import com.benben.kupaizhibo.bean.GuardSeatListBean;
import com.benben.kupaizhibo.bean.GuardTypeBean;
import com.benben.kupaizhibo.bean.GuardTypeListBean;
import com.benben.kupaizhibo.bean.LiveInfoBean;
import com.benben.kupaizhibo.bean.LiveRoomBean;
import com.benben.kupaizhibo.bean.ReportCategoryBean;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.bean.socket.CTMessageBean;
import com.benben.kupaizhibo.bean.socket.GiftCategoryInfoBean;
import com.benben.kupaizhibo.bean.socket.GiftInfoBean;
import com.benben.kupaizhibo.bean.socket.SendGiftBean;
import com.benben.kupaizhibo.bean.socket.SendMessageUtils;
import com.benben.kupaizhibo.bean.socket.SocketResponseBodyBean;
import com.benben.kupaizhibo.bean.socket.SocketResponseHeaderBean;
import com.benben.kupaizhibo.config.Constants;
import com.benben.kupaizhibo.config.SystemDir;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.NormalWebViewActivity;
import com.benben.kupaizhibo.ui.mine.MyInvitationActivity;
import com.benben.kupaizhibo.ui.mine.RechargeDiamondActivity;
import com.benben.kupaizhibo.ui.mine.UserHomePageActivity;
import com.benben.kupaizhibo.utils.SocketIoUtils;
import com.benben.kupaizhibo.utils.SoftKeyBoardListener;
import com.benben.kupaizhibo.widget.ContributionPopupWindow;
import com.benben.kupaizhibo.widget.CustomPopWindow;
import com.benben.kupaizhibo.widget.LiveGiftListPopupWindow;
import com.benben.kupaizhibo.widget.RollAdsLayout;
import com.benben.kupaizhibo.widget.ViewerListPopupWindow;
import com.king.view.flutteringlayout.view.HeartHonorLayout;
import com.kongzue.dialog.v3.MessageDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.limlee.hiframeanimationlib.FrameAnimationView;
import org.limlee.hiframeanimationlib.FrameDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qssq666.giftmodule.bean.GiftDemoModel;
import cn.qssq666.giftmodule.bean.UserDemoInfo;
import cn.qssq666.giftmodule.interfacei.GiftModelI;
import cn.qssq666.giftmodule.interfacei.UserInfoI;
import cn.qssq666.giftmodule.periscope.GiftAnimLayout;
import okhttp3.Call;

/**
 * 功能:看直播
 * create by zjn on 2019/5/14 0014
 * email:168455992@qq.com
 */
public class LookLiveActivity extends BaseActivity implements SocketCallbackListener {
    private static final String TAG = "LookLiveActivity";

    @BindView(R.id.video_view)
    TXCloudVideoView videoView;
    @BindView(R.id.civ_user_photo)
    CircleImageView civUserPhoto;
    @BindView(R.id.tv_user_nickname)
    TextView tvUserNickname;
    @BindView(R.id.tv_other_info)
    TextView tvOtherInfo;
    @BindView(R.id.llyt_user_info)
    LinearLayout llytUserInfo;
    @BindView(R.id.rlyt_guanzhu)
    RelativeLayout rlytGuanzhu;
    @BindView(R.id.rlyt_close)
    RelativeLayout rlytClose;
    @BindView(R.id.rlyt_top)
    RelativeLayout rlytTop;
    @BindView(R.id.rlv_chat_info)
    RecyclerView rlvChatInfo;
    @BindView(R.id.edt_input_content)
    EditText edtInputContent;
    @BindView(R.id.llyt_options)
    LinearLayout llytOptions;
    @BindView(R.id.tv_guanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.llyt_root)
    LinearLayout llytRoot;
    @BindView(R.id.galt_little_gift)
    GiftAnimLayout galtLittleGift;
    @BindView(R.id.frame_animation)
    FrameAnimationView frameAnimation;

    @BindView(R.id.roll_ads_msg)
    RollAdsLayout rollAdsMsg;
    @BindView(R.id.iv_guard)
    ImageView ivGuard;
    @BindView(R.id.rlyt_editor)
    RelativeLayout rlytEditor;
    @BindView(R.id.rlyt_collection)
    RelativeLayout rlytCollection;
    @BindView(R.id.rlyt_sharing)
    RelativeLayout rlytSharing;
    @BindView(R.id.rlyt_contribu)
    RelativeLayout rlytContribu;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.btn_send_msg)
    Button btnSendMsg;
    @BindView(R.id.llyt_editor)
    LinearLayout llytEditor;
    @BindView(R.id.tv_viewer_number)
    TextView tvViewerNumber;
    @BindView(R.id.heart_view)
    View heartView;
    @BindView(R.id.flutteringLayout)
    HeartHonorLayout flutteringLayout;

    //是否刷新
    private boolean isRefresh = false;
    //刷新聊天消息
    private static final int REFRESH_RECYCLERVIEW = 0;
    //刷新进入房间的用户列表
    private static final int REFRESH_ENTER_ROOM_USER = 1;
    //显示礼物动画
    private static final int SHOW_GIFT_ANMITION = 2;
    //发送红包
    private static final int SHOW_RED_ENVELOPES_VIEW = 3;
    //用户禁言提醒
    private static final int SHOW_JINYAN_TOAST = 4;
    //主播已下播
    private static final int CLOSE_VIDEO = 5;

    //刷新心形图标
    private static final int REFRESH_HEART_INFO = 6;
    //观看人数
    private long mLookNum = 0;
    private TXLivePlayer mLivePlayer;
    private LiveInfoBean mLiveInfoBean;
    private LiveRoomBean mLiveRoomBean;
    private boolean mPlaying = false;
    private SocketCallbackListener mSocketCallbackListener;
    private LiveChatMessageAdapter mLiveChatMessageAdapter;
    //消息列表容器
    private LinkedList<SocketResponseHeaderBean> mResponseHeaderBeanList = new LinkedList<>();
    //最近一条消息
    private List<SocketResponseHeaderBean> mLastResponseHeaderBeanList;
    private List<GiftCategoryInfoBean> mGiftCategoryInfoBeanList;
    //进入房间的用户列表
    private List<CTMessageBean> mEnterRoomUserList = new ArrayList<>();
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
                case SHOW_JINYAN_TOAST:
                    toast("您被禁言5分钟");
                    break;
                case CLOSE_VIDEO:
                    ToastUtils.show(mContext, "直播已结束，请观看其他直播");
                    finish();
                    break;
                case REFRESH_HEART_INFO:
                    flutteringLayout.addHeart(Color.parseColor("#FFEA7080"),R.mipmap.xin1,R.mipmap.xin1);
                    break;
            }
        }
    };


    private List<ReportCategoryBean> mReportCategoryBeanList;
    private CustomPopWindow mReportCategoryListPopWindow;
    private MessageListAdapter.LookLiveGiftBarAdapter lookLiveGiftBarAdapter;
    private int keyHeight;
    //守护坐席列表adapter
    private GuardSeatListAdapter mGuardSeatListAdapter;
    //守护类型列表adapter
    private GuardTypeListAdapter mGuardTypeListAdapter;
    //是否关注该主播
    private boolean isFollow = false;
    private int guardPage = 1;
    private int openGuardType = -1; //开通守护类型

    public static void startLiveWithData(Activity mContext,LiveInfoBean liveInfoBean){
        if(StringUtils.isEmpty(liveInfoBean.getPasswd())){
            Intent intent = new Intent(mContext, LookLiveActivity.class);
            intent.putExtra("liveinfo", liveInfoBean);
            mContext.startActivity(intent);
        }else {
            Intent intent = new Intent(mContext, LookLiveActivity.class);
            intent.putExtra("liveinfo", liveInfoBean);
            mContext.startActivity(intent);
           /* InputDialog.show((AppCompatActivity) mContext,"提示","请输入房间密码","确定","取消")
                    .setHintText("请输入6位房间密码")
            .setInputInfo(new InputInfo()
                    .setMAX_LENGTH(6)                                          //设置最大长度10位
                    .setInputType(InputType.TYPE_CLASS_NUMBER)//设置密码输入类型（注意此处请勿使用“InputType.TYPE_CLASS_TEXT | InputType.{YOUR_TYPE}”位运算，若不清楚此问题请按照范例使用即可）
            ).setOnOkButtonClickListener((baseDialog, v, inputStr) -> {
                if(inputStr.equals(liveInfoBean.getPasswd())){
                    Intent intent = new Intent(mContext, LookLiveActivity.class);
                    intent.putExtra("liveinfo", liveInfoBean);
                    mContext.startActivity(intent);
                }else {
                    ToastUtils.show(mContext,"密码错误，请重试");
                }
                return false;
            });*/

        }

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_look_live;
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void initData() {
        //设置顶部消息仅播放一次
        rollAdsMsg.setLoop(false);
        //keyHeight = this.getWindowManager().getDefaultDisplay().getHeight() / 3;
        mLiveInfoBean = ((LiveInfoBean) getIntent().getSerializableExtra("liveinfo"));
        //获取直播间信息
        getLiveRoomInfo(mLiveInfoBean.getUser_id());

        mSocketCallbackListener = this;
        edtInputContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });
        LinearLayoutManager vm = new LinearLayoutManager(mContext);
        vm.setOrientation(LinearLayoutManager.VERTICAL);
        rlvChatInfo.setLayoutManager(vm);
        //设置回收复用池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(RecyclerView.VERTICAL, 20);
        rlvChatInfo.setRecycledViewPool(recycledViewPool);
        mLiveChatMessageAdapter = new LiveChatMessageAdapter(mContext,mLiveInfoBean.getStream());
        rlvChatInfo.setAdapter(mLiveChatMessageAdapter);

        getGiftList();
        //初始化giftAnimLayout
        lookLiveGiftBarAdapter = new MessageListAdapter.LookLiveGiftBarAdapter(mContext);
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

        SoftKeyBoardListener.setListener(mContext, onSoftKeyBoardChangeListener);
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
        CTMessageBean ctMessageBean = JSONUtils.jsonString2Bean(receiveGiftInfoBean.getCt(), CTMessageBean.class);
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

    private void getLiveRoomInfo(String anchorId) {
        BaseOkHttpClient.newBuilder()
                .addParam("anchor_id", anchorId)
                .url(NetUrlUtils.LIVES_ENTER_ROOM)
                .json().post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                mLiveRoomBean = JSONUtils.jsonString2Bean(result, LiveRoomBean.class);
                //http://122.114.74.51:20000
                if (mLiveRoomBean != null) {
                    mLiveRoomBean.getSocket_handle().setNickname(KuPaiLiveApplication.mPreferenceProvider.getUserName());
                    SocketIoUtils.getInstance()
                            .setSocketCallbackListener(mSocketCallbackListener) //回调监听初始化
                            .connect(mLiveRoomBean.getSocket_url());
                    initTxPlayer();
                    refreshUI();
                }
            }

            @Override
            public void onError(int code, String msg) {
                if (code == -3001) {
                    toast(msg);
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void refreshUI() {
        tvUserNickname.setText(mLiveRoomBean.getNickname());
        ImageUtils.getPic(mLiveRoomBean.getAvatar(), civUserPhoto, mContext, R.mipmap.ic_default_pic);

        if ("0".equals(mLiveRoomBean.getIs_follow())) {
            //未关注
            isFollow = false;
            rlytGuanzhu.setBackgroundResource(R.drawable.shape_red_radius12_ff6261);
            tvGuanzhu.setText(getString(R.string.add_follow));
            tvGuanzhu.setTextColor(getResources().getColor(R.color.text_white));
        } else {
            //已关注
            isFollow = true;
            rlytGuanzhu.setBackgroundResource(R.drawable.shape_white_radius12_ffffff);
            tvGuanzhu.setText(getString(R.string.followed));
            tvGuanzhu.setTextColor(getResources().getColor(R.color.color_666666));
        }

        refreshOtherInfo();
    }

    private void refreshOtherInfo() {
        LogUtils.e(TAG, "mEnterRoomUserList.size() = " + mEnterRoomUserList.size());
        tvOtherInfo.setText((StringUtils.isEmpty(mLiveRoomBean.getCity()) ? "未知" : mLiveRoomBean.getCity()) + "  |  "
                + mLiveRoomBean.getFollow_num() + "个粉丝");
        tvViewerNumber.setText(getString(R.string.viewer_number, String.valueOf(mLiveRoomBean.getView_num() + mLookNum)));
    }

    private void initTxPlayer() {
        if (mPlaying) return;
        //mPlayerView 即 step1 中添加的界面 view
        //创建 player 对象
        mLivePlayer = new TXLivePlayer(mContext);
        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(videoView);
        // 设置填充模式
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        // 设置画面渲染方向
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        String flvUrl = mLiveRoomBean.getPull().getRtmp();
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP); //推荐 FLV
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFollowStatus();
    }

    //更新关注状态
    private void updateFollowStatus() {
        BaseOkHttpClient.newBuilder()
                .addParam("anchor_id", mLiveInfoBean.getUser_id())
                .url(NetUrlUtils.LIVES_ENTER_ROOM)
                .json().post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                mLiveRoomBean = JSONUtils.jsonString2Bean(result, LiveRoomBean.class);
                //http://122.114.74.51:20000
                if (mLiveRoomBean != null) {
                    if ("0".equals(mLiveRoomBean.getIs_follow())) {
                        //未关注
                        isFollow = false;
                        rlytGuanzhu.setBackgroundResource(R.drawable.shape_red_radius12_ff6261);
                        tvGuanzhu.setText(getString(R.string.add_follow));
                        tvGuanzhu.setTextColor(getResources().getColor(R.color.text_white));
                    } else {
                        //已关注
                        isFollow = true;
                        rlytGuanzhu.setBackgroundResource(R.drawable.shape_white_radius12_ffffff);
                        tvGuanzhu.setText(getString(R.string.followed));
                        tvGuanzhu.setTextColor(getResources().getColor(R.color.color_666666));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(true); // true 代表清除最后一帧画面
        }
        videoView.onDestroy();
        SocketIoUtils.getInstance().onDestory();
        frameAnimation.stop();
        frameAnimation.setOnFrameListener(null);
    }


    @OnClick({R.id.civ_user_photo, R.id.rlyt_guanzhu, R.id.rlyt_close,
            R.id.iv_guard, R.id.rlyt_collection, R.id.rlyt_sharing, R.id.rlyt_contribu,
            R.id.iv_gift, R.id.btn_send_msg, R.id.rlyt_editor, R.id.tv_viewer_number,R.id.heart_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_user_photo:
//                跳转到主播的主页
                Intent intent = new Intent(mContext, UserHomePageActivity.class);
                intent.putExtra("user_id", mLiveInfoBean.getUser_id());
                //intent.putExtra("is_follow", isFollow);
                startActivityForResult(intent, 101);
                break;
            case R.id.tv_viewer_number: //观众列表
                if (mLiveInfoBean == null || StringUtils.isEmpty(mLiveInfoBean.getStream())) {
                    toast(mContext.getResources().getString(R.string.get_user_list_failed));
                    return;
                }
                ViewerListPopupWindow viewerListPopupWindow = new ViewerListPopupWindow(mContext, mLiveInfoBean.getStream());
                viewerListPopupWindow.showWindow(mLiveInfoBean.getStream());
                break;
            case R.id.rlyt_guanzhu://关注
                if (mLiveInfoBean == null || StringUtils.isEmpty(mLiveInfoBean.getStream())) {
                    toast(mContext.getResources().getString(R.string.get_user_list_failed));
                    return;
                }
                updateFollow(mLiveRoomBean.getUser_id());
                break;
            case R.id.rlyt_close://关闭直播间
                finish();
                break;
            case R.id.iv_gift://礼物
                if (mGiftCategoryInfoBeanList != null && !mGiftCategoryInfoBeanList.isEmpty()) {
                    openSendGiftPop();
                } else {
                    getGiftList();
                }
                break;
            case R.id.iv_guard://守护
                showGuardDialog();
                break;
            case R.id.rlyt_collection://收藏
                showUnDevelopDialog();
                break;
            case R.id.rlyt_sharing://分享
                //弹出分享列表  朋友圈 微信 QQ  QQ空间 微博
                openSelectSharePop();
                break;
            case R.id.rlyt_contribu://贡献榜
                showContributionView();
                break;
            case R.id.btn_send_msg://发送消息
                sendMessage();
                break;
            case R.id.rlyt_editor://编辑
                llytEditor.setVisibility(View.VISIBLE);
                showSoftInputFromWindow(edtInputContent);
                break;
            case R.id.heart_view://心形点赞
                SocketIoUtils.getInstance().sendMsg(Constants.EVENT_BROAD_CAST, SendMessageUtils.getSendHeartBean());
                break;
        }
    }

    //敬请期待
    private void showUnDevelopDialog() {
        View view = View.inflate(mContext, R.layout.dialog_not_available, null);

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            //去除系统自带的margin
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置dialog在界面中的属性
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        alertDialog.show();

        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });
    }

    //贡献榜弹窗
    private void showContributionView() {
        ContributionPopupWindow contributionPopupWindow = new ContributionPopupWindow(mContext, getSupportFragmentManager());
        contributionPopupWindow.showWindow();
        contributionPopupWindow.setOnButtonClickListener(new ContributionPopupWindow.OnButtonClickListener() {
            @Override
            public void OnClickEditor() {//编辑
                llytEditor.setVisibility(View.VISIBLE);
                showSoftInputFromWindow(edtInputContent);
            }

            @Override
            public void OnClickCollection() {//收藏

            }

            @Override
            public void OnClickShare() {//分享
                openSelectSharePop();
            }

            @Override
            public void OnClickContribution() {//贡献榜
                if (contributionPopupWindow.isShowing()) {
                    contributionPopupWindow.dismiss();
                } else {
                    contributionPopupWindow.showWindow();
                }
            }

            @Override
            public void OnClickGift() {
                if (mGiftCategoryInfoBeanList != null && !mGiftCategoryInfoBeanList.isEmpty()) {
                    openSendGiftPop();
                } else {
                    getGiftList();
                }

            }
        });
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
            guardPage++;
            isRefresh = false;
            getGuardSeatList(refreshLayout2, guardPage);
        });
        //开通守护
        tvRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardSeatDialog.dismiss();
                showRechargeGuardDialog();
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
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取守护特权说明----onFailure:" + e.getMessage());
            }
        });


    }

    //开通守护的dialog
    private void showRechargeGuardDialog() {
        //初始化选择状态
        openGuardType = -1;
        View rechargeGuardView = View.inflate(mContext, R.layout.dialog_recharge_guard, null);

        AlertDialog rechargeGuardDialog = new AlertDialog.Builder(mContext)
                .setView(rechargeGuardView)
                .create();
        Window window = rechargeGuardDialog.getWindow();
        if (window != null) {
            //去除系统自带的margin
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置dialog在界面中的属性
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        TextView tvDiamondBalance = rechargeGuardView.findViewById(R.id.tv_diamond_balance);
        RecyclerView rlvGuardList = rechargeGuardView.findViewById(R.id.rlv_guard_list);
        rlvGuardList.setLayoutManager(new LinearLayoutManager(mContext));
        mGuardTypeListAdapter = new GuardTypeListAdapter(mContext);
        rlvGuardList.setAdapter(mGuardTypeListAdapter);
        TextView tvRecharge = rechargeGuardView.findViewById(R.id.tv_sure);
        TextView tvGuardPrerogative = rechargeGuardView.findViewById(R.id.tv_guard_prerogative);

        getGuardTypeList(tvDiamondBalance);

        //开通守护
        tvRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openGuardType == -1) {
                    toast(mContext.getResources().getString(R.string.guard_type_not_null));
                    return;
                }
                rechargeGuardDialog.dismiss();
                //去支付
                openGuard();
            }
        });
        //守护特权
        tvGuardPrerogative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGuardDescription();
            }
        });
        mGuardTypeListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<GuardTypeListBean>() {
            @Override
            public void onItemClick(View view, int position, GuardTypeListBean model) {
                //选择守护类型
                openGuardType = model.getType();
            }

            @Override
            public void onItemLongClick(View view, int position, GuardTypeListBean model) {

            }
        });

        rechargeGuardDialog.show();
    }

    //开通守护
    private void openGuard() {
        BaseOkHttpClient.newBuilder()
                .addParam("type", openGuardType) //0=>金牌守护 1=>银牌守护
                .url(NetUrlUtils.RECHARGE_GUARD)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "开通守护----onSuccess:" + json);
                // JSONUtils.jsonString2Beans(json,.class);
                toast(msg);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "开通守护----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "开通守护----onFailure:" + e.getMessage());
            }
        });


    }

    //获取守护类型列表
    private void getGuardTypeList(TextView textView) {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_GUARD_TYPE_LIST)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取守护类型列表----onSuccess:" + json);
                GuardTypeBean guardTypeBean = JSONUtils.jsonString2Bean(json, GuardTypeBean.class);
                ArrayList<GuardTypeListBean> guardTypeBeans = new ArrayList<>();
                GuardTypeListBean goldGuardBean = new GuardTypeListBean();
                GuardTypeListBean silverGuardBean = new GuardTypeListBean();
                goldGuardBean.setType(0);
                goldGuardBean.setSelect(false);
                goldGuardBean.setGuard_price(guardTypeBean.getGold_medal_money());
                goldGuardBean.setGuard_name(mContext.getResources().getString(R.string.gold_guard));

                silverGuardBean.setType(1);
                silverGuardBean.setSelect(false);
                silverGuardBean.setGuard_price(guardTypeBean.getSilver_medal_money());
                silverGuardBean.setGuard_name(mContext.getResources().getString(R.string.silver_guard));
                //余额
                textView.setText(guardTypeBean.getUser_money());
                guardTypeBeans.add(goldGuardBean);
                guardTypeBeans.add(silverGuardBean);
                mGuardTypeListAdapter.appendToList(guardTypeBeans);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取守护类型列表----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取守护类型列表----onFailure:" + e.getMessage());
            }
        });


    }

    //获取守护坐席列表
    private void getGuardSeatList(RefreshLayout refreshLayout, int page) {
        BaseOkHttpClient.newBuilder()
                .addParam("stream", mLiveInfoBean.getStream())
                .addParam("page", page)
                .url(NetUrlUtils.GET_GUARD_LIST)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取守护坐席列表----onSuccess:" + json);
                List<GuardSeatListBean> guardSeatList = JSONUtils.jsonString2Beans(JSONUtils.getNoteJson(json, "lists"), GuardSeatListBean.class);
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


    //自动弹出弹出软键盘
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);

    }


    //发送消息
    private void sendMessage() {
        String searchContent = edtInputContent.getText().toString();
        if (StringUtils.isEmpty(searchContent)) {
            toast(getString(R.string.cant_send_empty_message));
            return;
        }
        llytEditor.setVisibility(View.GONE);
        ScreenUtils.closeKeybord(edtInputContent, mContext);
        String json = SendMessageUtils.getSendChatMsgBean(searchContent);
         LogUtils.e(TAG, "send message json = "+json );
        SocketIoUtils.getInstance().sendMsg(Constants.EVENT_BROAD_CAST, json);
        edtInputContent.setText("");
        edtInputContent.setHint(getString(R.string.enter_the_content));
    }

    //获取主播信息
    private void getUserInfo() {

        if (mLiveInfoBean == null || mLiveInfoBean.getUser_id() == null) {
            return;
        }
        BaseOkHttpClient.newBuilder()
                .addParam("uid", mLiveInfoBean.getUser_id())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "直播用户信息dialog----onSuccess: result=" + result + "----msg=" + msg);
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


    /*打开分享弹窗*/
    private void openSelectSharePop() {
       /* CommonSharePopupWindow mCommonSharePopupWindow = new CommonSharePopupWindow(mContext);
        mCommonSharePopupWindow.setOnButtonClickListener(type -> startActivityForResult(new Intent(mContext, MyInvitationActivity.class).putExtra("type",type).putExtra("enter_type",0),102));
        mCommonSharePopupWindow.showWindow(getWindow().getDecorView().getRootView());*/
        startActivityForResult(new Intent(mContext, MyInvitationActivity.class), 102);
    }


    //关注或取消关注
    private void updateFollow(int id) {
        BaseOkHttpClient.newBuilder()
                .addParam("anchor_id", id)
                .url(NetUrlUtils.FOLLOW)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                toast(msg);
                FollowResultBean result = JSONUtils.jsonString2Bean(json, FollowResultBean.class);
                if (0 == result.getFollow()) {
                    //取消成功
                    isFollow = false;
                    rlytGuanzhu.setBackgroundResource(R.drawable.shape_red_radius12_ff6261);
                    tvGuanzhu.setText(getString(R.string.add_follow));
                    tvGuanzhu.setTextColor(getResources().getColor(R.color.text_white));
                } else {
                    //关注成功
                    isFollow = true;
                    rlytGuanzhu.setBackgroundResource(R.drawable.shape_white_radius12_ffffff);
                    tvGuanzhu.setText(getString(R.string.followed));
                    tvGuanzhu.setTextColor(getResources().getColor(R.color.color_666666));
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


    /*获取礼物列表*/
    private void getGiftList() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.LIVES_GET_GIFT)
                .get().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                if (!StringUtils.isEmpty(result)) {
                    mGiftCategoryInfoBeanList = JSONUtils.jsonString2Beans(result, GiftCategoryInfoBean.class);
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

    /*打开礼物弹窗*/
    private void openSendGiftPop() {
        //初始化礼物选中状态
        for (int i = 0; i < mGiftCategoryInfoBeanList.size(); i++) {
            for (int j = 0; j < mGiftCategoryInfoBeanList.get(i).getGift().size(); j++) {
                mGiftCategoryInfoBeanList.get(i).getGift().get(j).setSelect(false);
            }
        }
        LiveGiftListPopupWindow liveGiftListPopupWindow = new LiveGiftListPopupWindow(mContext,
                mGiftCategoryInfoBeanList);
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_look_live, null);
        liveGiftListPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        liveGiftListPopupWindow.setOnButtonClickListener(new LiveGiftListPopupWindow.OnButtonClickListener() {
            @Override
            public void OnClickSelectGiftInfo(GiftInfoBean giftInfoBean, int parentPosition, int subPosition) {
                /*if (giftInfoBean == null) {
                    toast("你还没有选择礼物");
                    return;
                }*/
                Log.e(TAG, "OnClickSelectGiftInfo: parentPosition=" + parentPosition + "----subPosition = " + subPosition + "----giftInfoBean = " + giftInfoBean);
                sendGift(mGiftCategoryInfoBeanList.get(parentPosition).getGift().get(subPosition), liveGiftListPopupWindow);
            }
        });
        getUserBalance(liveGiftListPopupWindow);
    }

    //获取用户余额
    private void getUserBalance(LiveGiftListPopupWindow liveGiftListPopupWindow) {
        BaseOkHttpClient.newBuilder()
                .addParam("uid", KuPaiLiveApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "获取用户信息 刷新余额  onSuccess: result=" + result + "----msg=" + msg);
                UserInfoBean userInfoBean = JSONUtils.jsonString2Bean(result, UserInfoBean.class);

                liveGiftListPopupWindow.setTvBobi(StringUtils.getWanStr(userInfoBean.getUser_bobi()));
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

    //赠送礼物
    private void sendGift(GiftInfoBean giftInfoBean, LiveGiftListPopupWindow liveGiftListPopupWindow) {

        if (mLiveInfoBean == null || mLiveInfoBean.getUser_id() == null || mLiveInfoBean.getStream() == null) {
            toast("赠送失败，请刷新直播间再试。");
            return;
        }
        BaseOkHttpClient.newBuilder()
                .addParam("gift_id", giftInfoBean.getId())
                .addParam("number", 1)
                .addParam("anchor_id", mLiveInfoBean.getUser_id())
                .addParam("stream", mLiveInfoBean.getStream())
                .addParam("type", 0)
                .url(NetUrlUtils.LIVES_SEND_GIFT)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "赠送礼物----onSuccess: result=" + result + "----msg=" + msg);

                SendGiftBean sendGiftBean = JSONUtils.jsonString2Bean(result, SendGiftBean.class);
                liveGiftListPopupWindow.setTvBobi(StringUtils.getWanStr(Float.parseFloat(sendGiftBean.getUser_bobi())));
                KuPaiLiveApplication.mPreferenceProvider.setUserLevel(String.valueOf(sendGiftBean.getLevel()));
                String sendGiftJson = SendMessageUtils.getSendGiftBean(sendGiftBean.getGift_token());
                Log.e(TAG, "OnClickSelectGiftInfo: sendGiftJson" + sendGiftJson);
                SocketIoUtils.getInstance().sendMsg(Constants.EVENT_BROAD_CAST, sendGiftJson);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "发送礼物失败 =" + msg);
                if (code == -298) {
                    MessageDialog.show(mContext, getResources().getString(R.string.tips), getResources().getString(R.string.balance_not_enough), getResources().getString(R.string.recharge), getResources().getString(R.string.negative))
                            .setOnOkButtonClickListener((baseDialog, v) -> {
                                startActivity(new Intent(mContext, RechargeDiamondActivity.class));
                                return false;
                            }).show();
                } else {
                    toast(msg);
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });


    }


    @Override
    public void onConnect(Object... args) {
        //开始握手
        String json = JSONUtils.toJsonString(mLiveRoomBean.getSocket_handle());
        SocketIoUtils.getInstance().sendMsg(Constants.EVENT_HANDSHAKE, json);
    }

    @Override
    public void onDisConnect(Object... args) {
        //断开连接后，清空已进入的用户列表
        mEnterRoomUserList.clear();
        mHandler.sendEmptyMessage(REFRESH_ENTER_ROOM_USER);
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
                    try {
                        String json = getUnescapeJson(args[i].toString());
                        Log.e(TAG, "onBroadCastingListener: json= " + json);
                        disposeBroadCastMessage(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    if ("409002".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                        mHandler.sendEmptyMessage(SHOW_JINYAN_TOAST);
                    } else {
                        //聊天消息
                        if ("2".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())) {
                            //直接取ct
                            //聊天消息
                            mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                        } else if ("0".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())) {
                            LogUtils.e(TAG, "直播间观众：" + mLastResponseHeaderBeanList.get(i).getMsg().get(0).getCt());
                            mEnterRoomUserList.add(JSONUtils.jsonString2Bean(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getCt(), CTMessageBean.class));
                            mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                            mLookNum++;
                            mHandler.sendEmptyMessage(REFRESH_ENTER_ROOM_USER);
                        }
                    }
                } else if ("SystemNot".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                    //系统消息
                    mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                } else if ("disconnect".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_()) &&
                        "1".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getAction()) &&
                        "0".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())
                ) {
                    //退出房间
                    if (mLookNum > 0) {
                        mLookNum--;
                    }

                    //退出房间
                    CTMessageBean ctMessageBean = JSONUtils.jsonString2Bean(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getCt(), CTMessageBean.class);
                    removeUser(ctMessageBean.getId());
                    mHandler.sendEmptyMessage(REFRESH_ENTER_ROOM_USER);
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
                } else if ("ShutUpUser".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                    //禁言提示
                    if ("1".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getAction())
                            && "4".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())
                    ) {
                        //剔出房间系统消息信息监听
                        mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                    }
                } else if ("KickUser".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                    //踢出用户监听
                    if ("2".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getAction())
                            && "4".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())
                    ) {
                        //剔出房间系统消息信息监听
                        mResponseHeaderBeanList.addLast(mLastResponseHeaderBeanList.get(i));
                    }
                } else if ("light".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                    //踢出用户监听
                    if ("2".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getAction())
                            && "0".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).getMsgtype())
                    ) {
                        //心型图标通知
                        mHandler.sendEmptyMessage(REFRESH_HEART_INFO);
                    }
                } else if ("CloseVideo".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_()) || "StartEndLive".equals(mLastResponseHeaderBeanList.get(i).getMsg().get(0).get_method_())) {
                    //主播关播了
                    mHandler.sendEmptyMessage(CLOSE_VIDEO);
                }
                mHandler.sendEmptyMessage(REFRESH_RECYCLERVIEW);
            }
        }
    }

    private void removeUser(String id) {
        Iterator<CTMessageBean> iter = mEnterRoomUserList.iterator();
        while (iter.hasNext()) {
            if (id.equals(iter.next().getId())) {
                iter.remove();
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
     * 软键盘弹出收起监听
     */
    private SoftKeyBoardListener.OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener = new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
        @Override
        public void keyBoardShow(int height) {
            LogUtils.e(TAG, "软键盘弹出");
            rlytTop.setVisibility(View.INVISIBLE);
            tvViewerNumber.setVisibility(View.INVISIBLE);
            ivGuard.setVisibility(View.INVISIBLE);

        }

        @Override
        public void keyBoardHide(int height) {
            LogUtils.e(TAG, "软键盘收起");
            rlytTop.setVisibility(View.VISIBLE);
            tvViewerNumber.setVisibility(View.VISIBLE);
            ivGuard.setVisibility(View.VISIBLE);
            llytEditor.setVisibility(View.GONE);
        }
    };

}
