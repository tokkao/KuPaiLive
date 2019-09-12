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
import com.benben.kupaizhibo.adapter.WalletEarningAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.WalletEarningBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.hyphenate.helper.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
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
 * Describe:收益
 */
public class MyProfitActivity extends BaseActivity {


    private static final String TAG = "MyProfitActivity";
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
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;

    private int mPageSize = 15;
    private int mPageStart = 1;
    private boolean isRefresh = false;
    private int year;
    private WalletEarningAdapter mAdapter;
    private int month;
    private int day;
    private String mCurrentTime;
    private Calendar mCurrentCalendar;
    private Calendar displayCalendar;
    private Date mCurrentDate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_profit;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.profit));
        rightTitle.setText("提现");
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new WalletEarningAdapter(mContext);
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
                getDataList();
                isRefresh = true;
            }
        });
        refreshLayout.setEnableLoadMore(false);
    }

    //获取账单流水列表
    private void getDataList() {

        BaseOkHttpClient.newBuilder()
                .addParam("select_time", mCurrentTime)
                .url(NetUrlUtils.USER_EARING_RECORD)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        //总记录数
                        String total_earing = JSONUtils.getNoteJson(json, "total_earing");
                        if (!StringUtils.isEmpty(total_earing)) {
                            tvMoney.setText(total_earing);
                        }else {
                            tvMoney.setText("0");
                        }
                        //明细数据
                        List<WalletEarningBean> lstWalletEarning = JSONUtils.jsonString2Beans(
                                JSONUtils.getNoteJson(json, "earing_list"), WalletEarningBean.class);

                        if (lstWalletEarning != null && lstWalletEarning.size() > 0) {
                            mAdapter.refreshList(lstWalletEarning);
                        } else {
                            mAdapter.clear();
                        }
                        //是否有数据
                        if (mAdapter.getItemCount() == 0) {
                            llytNoData.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.GONE);
                        } else {
                            llytNoData.setVisibility(View.GONE);
                            refreshLayout.setVisibility(View.VISIBLE);
                        }
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.finishRefresh();
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.finishRefresh(false);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.finishRefresh(false);
                        }
                    }
                });

    }

    @OnClick({R.id.iv_back_date, R.id.iv_front_date,R.id.right_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_date://前一天
                selectDate(-1);
                break;
            case R.id.iv_front_date://后一天
                selectDate(1);
                break;
            case R.id.right_title://提现
                startActivity(new Intent(mContext, WithdrawActivity.class));
                break;
        }
    }

    //选择日期
    private void selectDate(int type) {
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
