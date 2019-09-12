package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AccountDetailsListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.WalletDetailBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.hyphenate.helper.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe:明细
 */
public class AccountDetailsActivity extends BaseActivity {


    private static final String TAG = "AccountDetailsActivity";
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.iv_back_date)
    ImageView ivBackDate;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_front_date)
    ImageView ivFrontDate;
    @BindView(R.id.rlvList)
    RecyclerView rlvList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;

    private int mPageSize = 15;
    private int mPageStart = 1;
    //总记录数
    private int mTotalCount = 0;

    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private AccountDetailsListAdapter mAdapter;
    private String mCurrentTime;
    private Calendar mCurrentCalendar;
    private Calendar displayCalendar;
    private Date mCurrentDate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_details;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.account_details));
        rightTitle.setText(R.string.profit);
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new AccountDetailsListAdapter(mContext);
        rlvList.setAdapter(mAdapter);


        mCurrentDate = new Date();
        mCurrentTime = simpleDateFormat.format(mCurrentDate);
        tvDate.setText(mCurrentTime);

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
                isLoadMore = true;
                mPageStart++;
                getDataList();
            }
        });
    }

    //获取账单流水列表
    private void getDataList() {

        BaseOkHttpClient.newBuilder()
                .addParam("date", mCurrentTime)
                .addParam("page", mPageStart)
                .url(NetUrlUtils.GET_MONEY_DETAIL)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        //总记录数
                        String total = JSONUtils.getNoteJson(json, "total");
                        if (!StringUtils.isEmpty(total)) {
                            mTotalCount = Integer.parseInt(total);
                        } else {
                            mTotalCount = 0;
                        }
                        //明细数据
                        List<WalletDetailBean> lstWalletDetail = JSONUtils.jsonString2Beans(
                                JSONUtils.getNoteJson(json, "data"), WalletDetailBean.class);

                        //加载首页数据前，删除原有数据
                        if (mPageStart == 1) {
                            mAdapter.clear();
                        }

                        if (lstWalletDetail != null && lstWalletDetail.size() > 0) {
                            mAdapter.appendToList(lstWalletDetail);
                        }

                        //是否有数据
                        if (mAdapter.getItemCount() == 0) {
                            llytNoData.setVisibility(View.VISIBLE);
                            rlvList.setVisibility(View.GONE);
                        } else {
                            llytNoData.setVisibility(View.GONE);
                            rlvList.setVisibility(View.VISIBLE);
                        }
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.finishRefresh();
                        }
                        if (isLoadMore) {
                            isLoadMore = false;
                            if (mAdapter.getItemCount() >= mTotalCount) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                refreshLayout.finishLoadMore();
                            }
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.finishRefresh(false);
                        }
                        if (isLoadMore) {
                            isLoadMore = false;
                            refreshLayout.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.finishRefresh(false);
                        }
                        if (isLoadMore) {
                            isLoadMore = false;
                            refreshLayout.finishLoadMore(false);
                        }
                    }
                });

    }

    @OnClick({R.id.right_title, R.id.iv_back_date, R.id.iv_front_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_title://收益
                Intent intent = new Intent(mContext, MyProfitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.iv_back_date://前一天
                selectDate(-1);
                break;
            case R.id.iv_front_date://后一天
                selectDate(1);
                break;
        }
    }

    //选择日期
    private void selectDate(int type) {
        mPageStart = 1;
        Date beforeDate = DateUtils.incDay(mCurrentDate, type);
        mCurrentDate = beforeDate;
        mCurrentTime = simpleDateFormat.format(beforeDate);
        tvDate.setText(mCurrentTime);
        getDataList();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

}
