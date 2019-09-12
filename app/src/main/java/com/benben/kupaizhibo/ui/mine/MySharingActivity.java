package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.MySharingListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.MyShareListBean;
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
 * Created by: wanghk 2019-07-04.
 * Describe: 我的分享
 */
public class MySharingActivity extends BaseActivity {


    private static final String TAG = "MySharingActivity";
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;

    private int mPageSize = 15;
    private int mPageStart = 1;
    private boolean isRefresh = true;
    private MySharingListAdapter mMySharingListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_reward;
    }

    @Override
    protected void initData() {

        initTitle(getString(R.string.my_sharing));
        rightTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_my_sharing), null, null, null);
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        mMySharingListAdapter = new MySharingListAdapter(mContext);
        rlvList.setAdapter(mMySharingListAdapter);

        initRefreshLayout();

    }

    private void initRefreshLayout() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mPageStart = 1;
                getMySharingList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                mPageStart++;
                getMySharingList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRefresh = true;
        mPageStart = 1;
        getMySharingList();
    }

    //我的分享列表
    private void getMySharingList() {

        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_SHARING_LIST)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "我的分享列表----onSuccess:" + json);
                List<MyShareListBean> myShareList = JSONUtils.jsonString2Beans(json, MyShareListBean.class);
                mMySharingListAdapter.refreshList(myShareList);
                if (mMySharingListAdapter.getItemCount() <= 0){
                    rlvList.setVisibility(View.GONE);
                    llytNoData.setVisibility(View.VISIBLE);
                }else {
                    rlvList.setVisibility(View.VISIBLE);
                    llytNoData.setVisibility(View.GONE);
                }

                refreshLayout.finishRefresh(true);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "我的分享列表----onError:" + msg);
                ToastUtils.show(mContext, msg);
                refreshLayout.finishRefresh(true);
                rlvList.setVisibility(View.GONE);
                llytNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "我的分享列表----onFailure:" + e.getMessage());
                refreshLayout.finishRefresh(true);
                rlvList.setVisibility(View.GONE);
                llytNoData.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

    @OnClick({R.id.right_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_title://分享
                startActivityForResult(new Intent(mContext, MyInvitationActivity.class).putExtra("enter_type", 1), 102);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == 202 && data != null) {
            getMySharingList();
        }
    }

}
