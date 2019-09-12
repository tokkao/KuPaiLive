package com.benben.kupaizhibo.ui.message;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.MessageSystemAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.MessageListBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.hyphenate.helper.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Author:zhn
 * Time:2019/5/13 0013 16:50
 * <p>
 * 系统消息页面
 */
public class MessageSystemActivity extends BaseActivity {

    private static final String TAG = "MessageSystemActivity";

    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.rlv_system_message)
    RecyclerView rlvSystemMessage;
    @BindView(R.id.ly_view_no_data)
    View lyViewNoData;
    @BindView(R.id.stf_sys_msg_layout)
    SmartRefreshLayout stfSysMsgLayout;

    //是否正在刷新
    private boolean mIsRefreshing = false;
    //是否正在加载更多
    private boolean mIsLoadMore = false;

    //页号，默认是1
    private int pageIndex = 1;
    //消息类型,默认是0
    private int msgType = 0;

    //消息列表
    private MessageSystemAdapter mMessageSystemAdapter;

    @Override
    protected void initData() {
        centerTitle.setText(getString(R.string.message_system_msg));
        //初始化下拉刷新
        initRefreshLayout();
        //初始化消息列表
        initMessageList();
        //获取消息列表
        getMessageList();
    }


    private void initMessageList() {
        rlvSystemMessage.setLayoutManager(new LinearLayoutManager(mContext));
        mMessageSystemAdapter = new MessageSystemAdapter(mContext);
        rlvSystemMessage.setAdapter(mMessageSystemAdapter);
    }

    private void initRefreshLayout() {
        //下拉刷新
        stfSysMsgLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mIsRefreshing = true;
                pageIndex = 1;
                getMessageList();
            }
        });

        //上拉加载
        stfSysMsgLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                mIsLoadMore = true;
                getMessageList();
            }
        });
    }
    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, true);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_message;
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        onBackPressed();
    }

    private void getMessageList() {
       /* ArrayList<MessageListBean> messageListBeans = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            MessageListBean messageListBean = new MessageListBean();
            messageListBean.setContent("如果你无法简洁的表达你的想法，那只说明你还不够了解它。");
            messageListBean.setCreate_time("2019-7-02 14:22");
            messageListBean.setEvent("系统消息");
            messageListBeans.add(messageListBean);
        }
        mMessageSystemAdapter.appendToList(messageListBeans);
        lyViewNoData.setVisibility(View.GONE);*/
        BaseOkHttpClient.newBuilder()
                .addParam("page", pageIndex)
                .addParam("msgtype", msgType)
                .url(NetUrlUtils.GET_SYSTEM_MSG_LIST)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        //获取数据
                        List<MessageListBean> lstMsg = JSONUtils.jsonString2Beans(json, MessageListBean.class);

                        if (mIsRefreshing) {
                            mIsRefreshing = false;
                            stfSysMsgLayout.finishRefresh();
                            //清空列表
                            mMessageSystemAdapter.clear();
                        }
                        if (mIsLoadMore) {
                            mIsLoadMore = false;
                            //判断是否加载完成
                            if (lstMsg == null || lstMsg.size() == 0) {
                                stfSysMsgLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                stfSysMsgLayout.finishLoadMore();
                            }
                        }

                        if (lstMsg != null) {
                            mMessageSystemAdapter.appendToList(lstMsg);
                        }

                        if (mMessageSystemAdapter.getItemCount() == 0) {
                            lyViewNoData.setVisibility(View.VISIBLE);
                            rlvSystemMessage.setVisibility(View.GONE);
                        } else {
                            lyViewNoData.setVisibility(View.GONE);
                            rlvSystemMessage.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                        toast(msg);
                        if (mIsRefreshing) {
                            mIsRefreshing = false;
                            stfSysMsgLayout.finishRefresh(false);
                        }
                        if (mIsLoadMore) {
                            mIsLoadMore = false;
                            stfSysMsgLayout.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                        if (mIsRefreshing) {
                            mIsRefreshing = false;
                            stfSysMsgLayout.finishRefresh(false);
                        }
                        if (mIsLoadMore) {
                            mIsLoadMore = false;
                            stfSysMsgLayout.finishLoadMore(false);
                        }
                    }
                });
    }
}
