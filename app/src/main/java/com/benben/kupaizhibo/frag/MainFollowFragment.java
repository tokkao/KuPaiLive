package com.benben.kupaizhibo.frag;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StatusBarUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.LazyBaseFragments;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.HomeLiveListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.LiveInfoBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.home.SearchAnchorActivity;
import com.benben.kupaizhibo.ui.live.LookLiveActivity;
import com.benben.kupaizhibo.utils.LoginCheckUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 关注fragment
 */
public class MainFollowFragment extends LazyBaseFragments {

    private static final String TAG = "MainFollowFragment";
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rlyt_title_bar)
    RelativeLayout rlytTitleBar;
    @BindView(R.id.rlv_live_list)
    RecyclerView rlvLiveList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;

    private List<LiveInfoBean> mLiveInfoList = new ArrayList<>();
    private HomeLiveListAdapter mLiveListAdapter;
    private boolean isRefresh = true;

    public static MainFollowFragment getInstance() {
        MainFollowFragment sf = new MainFollowFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_follow, null);
        return mRootView;
    }

    @Override
    public void initView() {

        initRefreshLayout();
        rlytTitleBar.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        rlvLiveList.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        mLiveListAdapter = new HomeLiveListAdapter(mContext);
        rlvLiveList.setAdapter(mLiveListAdapter);
        mLiveListAdapter.appendToList(mLiveInfoList);
        mLiveListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<LiveInfoBean>() {
            @Override
            public void onItemClick(View view, int position, LiveInfoBean model) {
                LoginCheckUtils.checkUserIsLogin(mContext, new LoginCheckUtils.CheckCallBack() {
                    @Override
                    public void onCheckResult(boolean flag) {
                        if (flag) {
                            LookLiveActivity.startLiveWithData(mContext,model);
                        }
                    }
                });
            }

            @Override
            public void onItemLongClick(View view, int position, LiveInfoBean model) {

            }
        });


    }

    @Override
    protected void loadData() {
        super.loadData();
        getFollowAnchorList();
    }

    private void getFollowAnchorList() {
        //获取首页关注主播
        BaseOkHttpClient.newBuilder()
                .addParam("type", 1)// 1关注2同城
                .url(NetUrlUtils.GET_CITY_FOLLOW_LIVE)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {

            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "获取关注列表----onSuccess:" + result);
                if (!StringUtils.isEmpty(result)) {
                    List<LiveInfoBean> list = JSONUtils.jsonString2Beans(result, LiveInfoBean.class);
                    if (list != null && !list.isEmpty()) {
                        mLiveInfoList = list;
                        mLiveListAdapter.refreshList(mLiveInfoList);
                    }
                }
                if (mLiveListAdapter.getItemCount() == 0) {
                    llytNoData.setVisibility(View.VISIBLE);
                    rlvLiveList.setVisibility(View.GONE);
                } else {
                    llytNoData.setVisibility(View.GONE);
                    rlvLiveList.setVisibility(View.VISIBLE);
                }
                refreshLayout.finishRefresh();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取关注列表----onError:" + msg);

                refreshLayout.finishRefresh(false);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取关注列表----onFailure:" + e.getMessage());

                refreshLayout.finishRefresh(false);
            }
        });

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                startActivity(new Intent(mContext, SearchAnchorActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

        }
    }

    private void initRefreshLayout() {
        //设置 Header 背景为透明
        refreshLayout.setPrimaryColorsId(R.color.transparent, R.color.text_white);
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setEnableLoadMore(false);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                getFollowAnchorList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                getFollowAnchorList();
            }
        });
    }
}
