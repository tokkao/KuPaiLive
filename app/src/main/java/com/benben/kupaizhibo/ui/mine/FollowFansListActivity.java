package com.benben.kupaizhibo.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.RxBus;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.FollowFansListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.MyFansFollowListBean;
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
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.benben.kupaizhibo.config.Constants.RXBUS_KEY_REFRESH_MINEFRAGMENT;

/**
 * Created by: wanghk 2019-07-04.
 * Describe:关注/粉丝列表
 */
public class FollowFansListActivity extends BaseActivity {

    private static final String TAG = "FollowFansListActivity";
    @BindView(R.id.rlv_user_list)
    RecyclerView rlvUserList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    private int mFType; // 0 TA的关注  1 TA的粉丝   2 我的关注 3 我的粉丝
    private int mPageSize = 15;
    private int mPageStart = 1;
    private String mTitle;
    private boolean isRefresh = true;
    private FollowFansListAdapter mFollowFansListAdapter;
    private List<MyFansFollowListBean.DataBean> mFansFollowList;
    //他人的user_id
    private String mOtherUserId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow_fans_list;
    }

    @Override
    protected void initData() {
        mFType = getIntent().getIntExtra("type", 0);
        mOtherUserId = getIntent().getStringExtra("other_user_id");

        switch (mFType) {
            case 0:
                mTitle = getString(R.string.hes_follow);
                break;
            case 1:
                mTitle = getString(R.string.hes_fans);
                break;
            case 2:
                mTitle = getString(R.string.my_follow);
                break;
            case 3:
                mTitle = getString(R.string.my_fans);
                break;
        }

        centerTitle.setText(mTitle);

        rlvUserList.setLayoutManager(new LinearLayoutManager(mContext));

        mFollowFansListAdapter = new FollowFansListAdapter(mContext, mFType);
        rlvUserList.setAdapter(mFollowFansListAdapter);

        initRefreshLayout();

        if (mFType == 0 || mFType == 1) {
            getOtherFollowFansList();
        } else {
            getMyFollowFansList();
        }


        mFollowFansListAdapter.setOnFollowClickListener(new FollowFansListAdapter.OnFollowClickListener() {
            @Override
            public void onFollowClick(View view, int position, MyFansFollowListBean.DataBean model) {
                updateFollowStatus(position);
            }
        });

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getInstance().post(RXBUS_KEY_REFRESH_MINEFRAGMENT);
                onBackPressed();
            }
        });
    }


    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mPageStart = 1;
                if (mFType == 0 || mFType == 1) {
                    getOtherFollowFansList();
                } else {
                    getMyFollowFansList();
                }

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                mPageStart++;
                if (mFType == 0 || mFType == 1) {
                    getOtherFollowFansList();
                } else {
                    getMyFollowFansList();
                }
            }
        });
    }

    private void getMyFollowFansList() {


        BaseOkHttpClient.newBuilder()
                .addParam("page", mPageStart)
                .addParam("type", mFType == 2 ? 1 : 2) // 1关注 2粉丝
                .url(NetUrlUtils.ATTENTION)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "我的关注/粉丝列表----onSuccess:" + json);
                MyFansFollowListBean myFansFollowListBean = JSONUtils.jsonString2Bean(json, MyFansFollowListBean.class);
                mFansFollowList = myFansFollowListBean.getData();
                if (isRefresh) {
                    if (mFType == 2) { //如果是我的关注  全部设置为已关注
                        for (int i = 0; i < myFansFollowListBean.getData().size(); i++) {
                            myFansFollowListBean.getData().get(i).setIs_follow(1);
                        }
                    }
                    mFollowFansListAdapter.refreshList(myFansFollowListBean.getData());
                } else {
                    mFollowFansListAdapter.appendToList(myFansFollowListBean.getData());
                }
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

                if (mFollowFansListAdapter.getItemCount() <= 0) {
                    llytNoData.setVisibility(View.VISIBLE);
                    rlvUserList.setVisibility(View.GONE);
                } else {
                    llytNoData.setVisibility(View.GONE);
                    rlvUserList.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "我的关注/粉丝列表----onError:" + msg);
                ToastUtils.show(mContext, msg);
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "我的关注/粉丝列表----onFailure:" + e.getMessage());
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

            }
        });


    }

    private void getOtherFollowFansList() {


        BaseOkHttpClient.newBuilder()
                .addParam("page", mPageStart)
                .addParam("uid", mOtherUserId)
                .addParam("type", mFType == 0 ? 1 : 2) // 1关注 2粉丝
                .url(NetUrlUtils.GET_OTHER_FANS_FOLLOW)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "他人的关注/粉丝列表----onSuccess:" + json);
                MyFansFollowListBean myFansFollowListBean = JSONUtils.jsonString2Bean(json, MyFansFollowListBean.class);
                mFansFollowList = myFansFollowListBean.getData();
                if (isRefresh) {
                    mFollowFansListAdapter.refreshList(myFansFollowListBean.getData());
                } else {
                    mFollowFansListAdapter.appendToList(myFansFollowListBean.getData());
                }
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

                if (mFollowFansListAdapter.getItemCount() <= 0) {
                    llytNoData.setVisibility(View.VISIBLE);
                    rlvUserList.setVisibility(View.GONE);
                } else {
                    llytNoData.setVisibility(View.GONE);
                    rlvUserList.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "他人的关注/粉丝列表----onError:" + msg);
                ToastUtils.show(mContext, msg);
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "他人的关注/粉丝列表----onFailure:" + e.getMessage());
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

            }
        });


    }


    //更新关注状态
    private void updateFollowStatus(int position) {

        BaseOkHttpClient.newBuilder()
                .addParam("anchor_id", mFansFollowList.get(position).getUid())
                .url(NetUrlUtils.FOLLOW)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "更新关注状态----onSuccess:" + json);
                mFansFollowList.get(position).setIs_follow(mFansFollowList.get(position).getIs_follow() == 0 ? 1 : 0);
                mFollowFansListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "更新关注状态----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "更新关注状态----onFailure:" + e.getMessage());
            }
        });

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
