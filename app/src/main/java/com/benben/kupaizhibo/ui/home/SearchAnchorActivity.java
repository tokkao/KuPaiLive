package com.benben.kupaizhibo.ui.home;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StatusBarUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.HomeLiveListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.LiveInfoBean;
import com.benben.kupaizhibo.bean.SearchLiveListBean;
import com.benben.kupaizhibo.bean.SearchRankingBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
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
 * Created by: wanghk 2019-07-02.
 * Describe: 搜索主播页面
 */
public class SearchAnchorActivity extends BaseActivity {
    private static final String TAG = "SearchAnchorActivity";
    @BindView(R.id.edt_search_anchor)
    EditText edtSearchAnchor;
    @BindView(R.id.tv_negative)
    TextView tvNegative;
    @BindView(R.id.rlv_live_list)
    RecyclerView rlvLiveList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    private HomeLiveListAdapter mLiveListAdapter;
    private List<LiveInfoBean> mLiveInfoList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_streamer;
    }

    @Override
    protected void initData() {
        initRefreshLayout();


        //默认弹出软键盘
        edtSearchAnchor.setFocusable(true);
        edtSearchAnchor.setFocusableInTouchMode(true);
        edtSearchAnchor.requestFocus();

        edtSearchAnchor.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getSearchInfoList();
                return true;
            }
            return false;
        });


        rlvLiveList.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        mLiveListAdapter = new HomeLiveListAdapter(mContext);
        rlvLiveList.setAdapter(mLiveListAdapter);
        mLiveListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<LiveInfoBean>() {
            @Override
            public void onItemClick(View view, int position, LiveInfoBean model) {
                LoginCheckUtils.checkUserIsLogin(mContext, new LoginCheckUtils.CheckCallBack() {
                    @Override
                    public void onCheckResult(boolean flag) {
                        if(flag){
                            LookLiveActivity.startLiveWithData(mContext,model);
                        }
                    }
                });
            }

            @Override
            public void onItemLongClick(View view, int position, LiveInfoBean model) {

            }
        });
        getSearchRanking();
    }

    //获取搜索排行
    private void getSearchRanking() {
        BaseOkHttpClient.newBuilder()
                .addParam("type", 0)//查询类型 0：直播 1：陪护 2：活动
                .url(NetUrlUtils.GET_SEARCH_RANKING)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "首页搜索排行----onSuccess:" + json);
                String noteJson = JSONUtils.getNoteJson(json, "lives_ranking");
                String noteJson1 = JSONUtils.getNoteJson(noteJson, "data");

                List<SearchLiveListBean> lstSearchRanking = JSONUtils.jsonString2Beans(noteJson1,
                        SearchLiveListBean.class);

                if (lstSearchRanking != null && !lstSearchRanking.isEmpty()) {
                    for (int i = 0; i < lstSearchRanking.size(); i++) {
                        LiveInfoBean liveInfoBean = lstSearchRanking.get(i).convertLiveInfo();
                        mLiveInfoList.add(liveInfoBean);
                    }
                    mLiveListAdapter.refreshList(mLiveInfoList);
                } else {
                    mLiveListAdapter.clear();
                }
                if (mLiveListAdapter.getItemCount() > 0) {
                    llytNoData.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                } else {
                    llytNoData.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "首页搜索排行----onError:" + msg);
                ToastUtils.show(mContext,msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "首页搜索排行----onFailure:" + e.getMessage());
            }
        });



    }


    private void initRefreshLayout() {
        //设置 Header 背景为透明
        // refreshLayout.setPrimaryColorsId(R.color.transparent, R.color.text_white);
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(StringUtils.isEmpty(edtSearchAnchor.getText().toString().trim())){
                    getSearchRanking();
                }else {
                    getSearchInfoList();
                }

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }

    //获取搜索结果
    private void getSearchInfoList() {
        String keyWords = edtSearchAnchor.getText().toString().trim();
        if (StringUtils.isEmpty(keyWords)) {
            //llytNoData.setVisibility(View.VISIBLE);
            //refreshLayout.setVisibility(View.GONE);
            toast(mContext.getResources().getString(R.string.keyword_not_null));
            return;
        }

        BaseOkHttpClient.newBuilder()
                .addParam("keywords", keyWords)
                .url(NetUrlUtils.GET_SEARCH_INFO)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        LogUtils.e(TAG, "搜索直播列表----onSuccess:" + json);
                        List<SearchRankingBean> lstSearchRanking = JSONUtils.jsonString2Beans(json,
                                SearchRankingBean.class);

                        if (lstSearchRanking != null && !lstSearchRanking.isEmpty()) {
                            mLiveInfoList.clear();
                            for (int i = 0; i < lstSearchRanking.size(); i++) {
                                LiveInfoBean liveInfoBean = lstSearchRanking.get(i).convertLiveInfo();
                                mLiveInfoList.add(liveInfoBean);
                            }
                            mLiveListAdapter.refreshList(mLiveInfoList);
                        }
                        if (mLiveListAdapter.getItemCount() > 0) {
                            llytNoData.setVisibility(View.GONE);
                            refreshLayout.setVisibility(View.VISIBLE);
                        } else {
                            llytNoData.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, "搜索直播列表----onError:" + msg);
                        toast(msg);
                        llytNoData.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, "搜索直播列表----onFailure:" + e.getMessage());
                    }
                });

    }

    @OnClick({R.id.tv_negative})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_negative:
                finish();
                break;
        }
    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (ScreenUtils.isShowKeyboard(mContext, edtSearchAnchor))
            ScreenUtils.closeKeybord(edtSearchAnchor, mContext);
    }

}
