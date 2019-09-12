package com.benben.kupaizhibo.ui.mine;

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
import com.benben.kupaizhibo.adapter.WithdrawRecordListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.WithdrawRecordListBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Author:zhn
 * Time:2019/5/15 0015 15:43
 * <p>
 * 我的钱包-提现记录
 */
public class WithdrawRecordActivity extends BaseActivity {

    private static final String TAG = "WithdrawRecordActivity";
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private boolean isRefresh;
    private int mPageSize = 15;
    private int mPageStart = 1;
    private WithdrawRecordListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw_record;
    }

    @Override
    protected void initData() {
        initTitle("提现记录");

        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new WithdrawRecordListAdapter(mContext);
        rlvList.setAdapter(mAdapter);

        initRefreshLayout();
        getDataList();
    }
    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mPageStart = 1;
                getDataList();

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                mPageStart++;
               getDataList();
            }
        });
    }

    private void getDataList() {

        BaseOkHttpClient.newBuilder()
                .addParam("page", mPageStart)
                .addParam("limit", mPageSize)
                .url(NetUrlUtils.GET_WITHDRAW_RECORD)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "提现记录----onSuccess:" + json);
                List<WithdrawRecordListBean> beanList = JSONUtils.jsonString2Beans(json, WithdrawRecordListBean.class);
                if (isRefresh) {
                    mAdapter.refreshList(beanList);
                } else {
                    mAdapter.appendToList(beanList);
                }
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

                if (mAdapter.getItemCount() <= 0) {
                    llytNoData.setVisibility(View.VISIBLE);
                    rlvList.setVisibility(View.GONE);
                } else {
                    llytNoData.setVisibility(View.GONE);
                    rlvList.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "提现记录----onError:" + msg);
                ToastUtils.show(mContext, msg);
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "提现记录----onFailure:" + e.getMessage());
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);

            }
        });

    }
}
