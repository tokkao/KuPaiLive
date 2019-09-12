package com.benben.kupaizhibo.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.RxBus;
import com.benben.commoncore.utils.StatusBarUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.LazyBaseFragments;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.MessageListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.EasemobUserBean;
import com.benben.kupaizhibo.bean.MainUnreadMessageNumBean;
import com.benben.kupaizhibo.bean.UnReadMsgCountBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.message.MessageSystemActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.helper.HyphenateHelper;
import com.hyphenate.helper.ui.ChatActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 消息fragment
 */
public class MainMessageFragment extends LazyBaseFragments {

    private static final String TAG = "MainMessageFragment_log";
    @BindView(R.id.rlyt_title_bar)
    RelativeLayout rlytTitleBar;
    @BindView(R.id.rlv_message_list)
    RecyclerView rlvMessageList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;

    //是否正在刷新
    private boolean isRefreshing = false;
    //消息列表
    private MessageListAdapter mMessageListAdapter;

    //请求：获取环信会员的昵称和头像，的等待数量
    private volatile int mWaitingForCount;

    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private int unReadMessageCount = 0;
    private boolean flag = false;

    public static MainMessageFragment getInstance() {
        MainMessageFragment sf = new MainMessageFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_message, null);
        return mRootView;
    }

    @Override
    public void initView() {
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
        rlytTitleBar.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);
        initRefreshLayout();
    }

    @Override
    public void initData() {
        //初始化消息列表
        initMessageList();

    }

    @Override
    protected void loadData() {
        super.loadData();
         LogUtils.e(TAG, "loadData***********" );
        //获取未读消息数量
        getUnreadMsgCount();
        //获取系统消息列表
        getSystemMsg();

    }

    private void getUnreadMsgCount() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_UNREAD_MESSAGE_NUM)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取未读消息数量----onSuccess:" + json);
                MainUnreadMessageNumBean mainUnreadMessageNumBean = JSONUtils.jsonString2Bean(json, MainUnreadMessageNumBean.class);
                if (mainUnreadMessageNumBean != null) {
                    unReadMessageCount = mainUnreadMessageNumBean.getMsg_count();
                }
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取未读消息数量----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取未读消息数量----onFailure:" + e.getMessage());
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume***********" );
        if(flag){
            loadData();
            flag = false;
        }
    }

    //初始化消息列表
    private void initMessageList() {
        rlvMessageList.setLayoutManager(new LinearLayoutManager(mContext));
        mMessageListAdapter = new MessageListAdapter(mContext);
        rlvMessageList.setAdapter(mMessageListAdapter);
        mMessageListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<EasemobUserBean>() {
            @Override
            public void onItemClick(View view, int position, EasemobUserBean model) {
                flag = true;
                if (model.isSystemMsg()) {
                    startActivity(new Intent(mContext, MessageSystemActivity.class));
                } else {
                    //开始聊天
                    toServiceOnline(model.getEid());
                }
            }

            @Override
            public void onItemLongClick(View view, int position, EasemobUserBean model) {

            }
        });
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
                        if (!TextUtils.isEmpty(json)) {
                            String userName = JSONUtils.getNoteJson(json, "nickname");
                            String avatar = JSONUtils.getNoteJson(json, "avatar");
                            if (TextUtils.isEmpty(userName)) {
                                userName = "";
                            }
                            if (TextUtils.isEmpty(avatar)) {
                                avatar = "";
                            }

                            HyphenateHelper.callChatIM(mContext, ChatActivity.class, userId, userName, avatar,
                                    KuPaiLiveApplication.mPreferenceProvider.getPhoto());
                        }

                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                        ToastUtils.show(mContext, msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                    }
                });
    }


    private void initRefreshLayout() {
     /*   //设置 Header 背景为透明
        refreshLayout.setPrimaryColorsId(R.color.transparent, R.color.text_white);
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));*/
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getSystemMsg();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }


    //获取系统消息列表
    private void getSystemMsg() {

        EasemobUserBean bean = new EasemobUserBean();
        bean.setSystemMsg(true);
        bean.seteNickName(getString(R.string.message_system_msg));
        mMessageListAdapter.refreshData(bean);
        //获取环信用户
        getAllContacts();
        rlvMessageList.setVisibility(View.VISIBLE);
        llytNoData.setVisibility(View.GONE);
     /*   BaseOkHttpClient.newBuilder()
                .addParam("page", 1)
                .url(NetUrlUtils.GET_SYSTEM_MSG_LIST)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        //获取数据
                        List<MessageListBean> lstMsg = JSONUtils.jsonString2Beans(json, MessageListBean.class);

                        if (lstMsg != null && lstMsg.size() > 0) {
                            EasemobUserBean bean = new EasemobUserBean();
                            bean.setSystemMsg(true);
                            bean.seteNickName(getString(R.string.message_system_msg));
                            bean.seteLastMsgTime(DateUtils.stampToDate(lstMsg.get(0).getCreate_time()));
                            bean.seteLastMsg(lstMsg.get(0).getContent());
                            bean.seteUnreadMsgCount(unReadMessageCount);
                            mMessageListAdapter.refreshData(bean);
                        } else {
                            EasemobUserBean bean = new EasemobUserBean();
                            bean.setSystemMsg(true);
                            bean.seteNickName(getString(R.string.message_system_msg));
                            mMessageListAdapter.refreshData(bean);
                        }

                        if (mMessageListAdapter.getItemCount() <= 0) {
                            rlvMessageList.setVisibility(View.GONE);
                            llytNoData.setVisibility(View.VISIBLE);
                        }else {
                            rlvMessageList.setVisibility(View.VISIBLE);
                            llytNoData.setVisibility(View.GONE);
                        }
                        //获取环信用户
                        getAllContacts();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                        ToastUtils.show(mContext, msg);
                        loadFinish(false);
                        if(mMessageListAdapter.getItemCount() <= 0){
                            rlvMessageList.setVisibility(View.GONE);
                            llytNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                        loadFinish(false);
                        if(mMessageListAdapter.getItemCount() <= 0){
                            rlvMessageList.setVisibility(View.GONE);
                            llytNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });*/
    }

    private void getAllContacts() {
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
            LogUtils.e(TAG, "getAllConversations 耗时: " + (System.currentTimeMillis() - startTime));

            if (allConversations == null || allConversations.isEmpty()) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //加载完成
                        loadFinish(true);
                    }
                });
            } else {
                mWaitingForCount = allConversations.size();
                Iterator<String> keyIterator = allConversations.keySet().iterator();
                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    //获取用户信息
                    getContactInfo(allConversations.get(key));
                }
            }
            //刷新tab 未读数量
            RxBus.getInstance().post(new UnReadMsgCountBean(unReadMessageCount));
        }).start();

    }

    //获取用户昵称和头像
    private void getContactInfo(EMConversation conversation) {
        int unreadMsgCount = conversation.getUnreadMsgCount();
        unReadMessageCount +=unreadMsgCount;
        EMMessage emMessage = conversation.getLastMessage();
        if (emMessage == null) {
            return;
        }
        String lastMsg = "";
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            lastMsg = ((EMTextMessageBody) body).getMessage();
        } else if (body instanceof EMImageMessageBody) {
            lastMsg = getString(R.string.message_type_image);
        } else if (body instanceof EMVideoMessageBody) {
            lastMsg = getString(R.string.message_type_video);
        } else if (body instanceof EMVoiceMessageBody) {
            lastMsg = getString(R.string.message_type_audio);
        } else if (body instanceof EMFileMessageBody) {
            lastMsg = getString(R.string.message_type_file);
        } else if (body instanceof EMLocationMessageBody) {
            lastMsg = getString(R.string.message_type_location);
        } else {
            lastMsg = getString(R.string.message_type_unknown);
        }

        String lastMsgTime = mSdf.format(new Date(emMessage.getMsgTime()));
        String eid = emMessage.getUserName();

        String finalLastMsg = lastMsg;
        BaseOkHttpClient.newBuilder()
                .addParam("eid", emMessage.getUserName())
                .url(NetUrlUtils.GET_EASEMOB_USER)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        mWaitingForCount--;

                        if (!TextUtils.isEmpty(json)) {
                            String userName = JSONUtils.getNoteJson(json, "nickname");
                            String avatar = JSONUtils.getNoteJson(json, "avatar");
                            if (TextUtils.isEmpty(userName)) {
                                userName = "";
                            }
                            if (TextUtils.isEmpty(avatar)) {
                                avatar = "";
                            }
                            EasemobUserBean bean = new EasemobUserBean();
                            bean.setEid(eid);
                            bean.seteNickName(userName);
                            bean.seteAvatar(avatar);
                            bean.seteLastMsgTime(lastMsgTime);
                            bean.seteLastMsg(finalLastMsg);
                            bean.seteUnreadMsgCount(unreadMsgCount);
                            mMessageListAdapter.getList().add(bean);
                        }

                        if (mWaitingForCount == 0) {
                            loadFinish(true);
                            mMessageListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                        ToastUtils.show(mContext, msg);
                        loadFinish(false);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                        loadFinish(false);
                    }
                });
    }

    //加载数据完成
    public void loadFinish(boolean suc) {
        //加载完成
        refreshLayout.finishRefresh(suc);
    }

}
