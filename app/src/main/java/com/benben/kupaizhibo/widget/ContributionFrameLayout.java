package com.benben.kupaizhibo.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.ContributionListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.RankBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 贡献榜fragment
 */
public class ContributionFrameLayout extends FrameLayout {


    private static final String TAG = "ContributionFragment";

    Activity mContext;
    // 0 日榜 1周榜 2月榜
    private int type;
    //贡献榜列表adapter
    private ContributionListAdapter mContributionListAdapter;
    private String rankingType;
    private boolean mIsRefreshing;
    private ViewHolder holder;

    public ContributionFrameLayout(Context context, int type) {
        super(context);
        mContext = (Activity) context;
        this.type = type;
        initView();
    }


    public void initView() {
        View mView = View.inflate(mContext, R.layout.layout_contribution, null);
        addView(mView);

        holder = new ViewHolder(mView);
        //refreshLayout设置背景透明
        holder.refreshLayout.setPrimaryColors(mContext.getResources().getColor(R.color.transparent), mContext.getResources().getColor(R.color.white));
        holder.refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        holder.refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        //刷新
        holder.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getContributionList();
            }
        });
        holder.refreshLayout.setEnableLoadMore(false);

        holder.rlvContribuList.setLayoutManager(new LinearLayoutManager(mContext));
        mContributionListAdapter = new ContributionListAdapter(mContext);
        holder.rlvContribuList.setAdapter(mContributionListAdapter);

        //item点击事件
        mContributionListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<RankBean>() {
            @Override
            public void onItemClick(View view, int position, RankBean model) {

            }

            @Override
            public void onItemLongClick(View view, int position, RankBean model) {

            }
        });
        getContributionList();
    }

    //获取贡献榜列表
    private void getContributionList() {

        switch (type) {
            case 0:
                rankingType = "day";
                break;
            case 1:
                rankingType = "week";
                break;
            case 2:
                rankingType = "month";
                break;
        }


        BaseOkHttpClient.newBuilder()
                .addParam("type", rankingType)
                .addParam("user_id", "")
                .addParam("id", "")
                .addParam("num", "")
                .url(NetUrlUtils.GET_CONTRIBUTION_RANKING)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取用户贡献排行榜----onSuccess:" + json);
                holder.refreshLayout.finishRefresh(true);
                String rankData = JSONUtils.getNoteJson(json, "rankdata");
                List<RankBean> lstRank = JSONUtils.jsonString2Beans(rankData, RankBean.class);
                mContributionListAdapter.refreshList(lstRank);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取用户贡献排行榜----onError:" + msg);

                ToastUtils.show(mContext, msg);
                holder.refreshLayout.finishRefresh(false);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取用户贡献排行榜----onFailure:" + e.getMessage());

                holder.refreshLayout.finishRefresh(false);
            }
        });

    }


    public static
    class ViewHolder {
        public View rootView;
        public RecyclerView rlvContribuList;
        public SmartRefreshLayout refreshLayout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.rlvContribuList = (RecyclerView) rootView.findViewById(R.id.rlv_contribu_list);
            this.refreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        }

    }
}
