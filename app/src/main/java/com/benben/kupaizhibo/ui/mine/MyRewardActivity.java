package com.benben.kupaizhibo.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.MyRewardListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.MyRewardListBean;
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

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 我的打赏
 */
public class MyRewardActivity extends BaseActivity {

    private static final String TAG = "MyRewardActivity";
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;

    private int mPageSize = 15;
    private int mPageStart = 1;
    private boolean isRefresh = true;
    private MyRewardListAdapter mMyRewardListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_reward;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.my_reward));

        rlvList.setLayoutManager(new LinearLayoutManager(mContext));

        mMyRewardListAdapter = new MyRewardListAdapter(mContext);
        rlvList.setAdapter(mMyRewardListAdapter);

        initRefreshLayout();
        getMyRewardList();
    }


    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mPageStart = 1;
                getMyRewardList();
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                mPageStart++;
                getMyRewardList();
            }
        });
    }

    //获取我的打赏列表
    private void getMyRewardList() {

        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_REWARD_LIST)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "我的打赏列表----onSuccess:" + json);
                List<MyRewardListBean> myRewardListBeans = JSONUtils.jsonString2Beans(json, MyRewardListBean.class);
                mMyRewardListAdapter.refreshList(myRewardListBeans);
                refreshLayout.finishRefresh(true);

                if (mMyRewardListAdapter.getItemCount() <= 0) {
                    rlvList.setVisibility(View.GONE);
                    llytNoData.setVisibility(View.VISIBLE);
                } else {
                    rlvList.setVisibility(View.VISIBLE);
                    llytNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "我的打赏列表----onError:" + msg);
                ToastUtils.show(mContext, msg);
                refreshLayout.finishRefresh(true);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "我的打赏列表----onFailure:" + e.getMessage());
                refreshLayout.finishRefresh(true);
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
