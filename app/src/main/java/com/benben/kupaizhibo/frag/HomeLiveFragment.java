package com.benben.kupaizhibo.frag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.benben.commoncore.utils.DensityUtil;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.LazyBaseFragments;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.HomeLiveListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.LiveInfoBean;
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
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 首页-直播fragment
 */
public class HomeLiveFragment extends LazyBaseFragments {

    private static final String TAG = "HomeLiveFragment";
    @BindView(R.id.banner_home)
    Banner bannerHome;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rlv_live_list)
    RecyclerView rlvLiveList;
    @BindView(R.id.llyt_dot)
    LinearLayout llytDot;
    /*当前显示的是第几页*/
    private int curIndex = 0;
    //轮播图
    private List<LiveInfoBean> mBannerList;
    //直播列表
    private List<LiveInfoBean> mLiveInfoList = new ArrayList<>();
    private boolean isRefresh = true;
    private HomeLiveListAdapter mLiveListAdapter;

    public static HomeLiveFragment getInstance() {
        HomeLiveFragment sf = new HomeLiveFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_home_live, null);
        return mRootView;
    }

    @Override
    public void initView() {
        initRefreshLayout();
        initBanner();
        getActiveList();
    }

    private void initBanner() {

        /*mBannerImageList = new ArrayList<>();
        mBannerImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561985725062&di=dc2655ed56cb5977b2722ca7b955f3ec&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20120121%2FImg332833001.jpg");
        mBannerImageList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1157420823,1033510587&fm=26&gp=0.jpg");
        mBannerImageList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2305300061,1108099563&fm=26&gp=0.jpg");
        mBannerImageList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1982049859,1410641884&fm=26&gp=0.jpg");
*/
        bannerHome.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object obj, ImageView imageView) {
                LiveInfoBean activeInfoBean = (LiveInfoBean) obj;
                ImageUtils.getCircularPic(activeInfoBean.getThumb(), imageView, mContext, 10, R.drawable.image_placeholder);
            }
        });
        bannerHome.setBannerStyle(BannerConfig.NOT_INDICATOR);
//        bannerHome.setIndicatorGravity(BannerConfig.CENTER);
        bannerHome.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        bannerHome.setOnBannerListener(position -> {
            if(!LoginCheckUtils.checkLoginShowDialog(mContext)) return;
            LookLiveActivity.startLiveWithData(mContext,mBannerList.get(position));
        });


    }

    View selectDotView;
    View noSelectDotView;

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        llytDot.removeAllViews();
        for (int i = 0; i < mBannerList.size(); i++) {
            llytDot.addView(LayoutInflater.from(mContext).inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        selectDotView = llytDot.getChildAt(0).findViewById(R.id.v_dot);
        selectDotView.setBackgroundResource(R.drawable.dot_selected);
        ViewGroup.LayoutParams layoutParams = selectDotView.getLayoutParams();
        layoutParams.width = DensityUtil.dip2px(mContext, 14);
        layoutParams.height = DensityUtil.dip2px(mContext, 6);
        selectDotView.setLayoutParams(layoutParams);
        bannerHome.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
            /*    for (int i = 0; i < arr.length; i++) {
                    arr[i].notifyDataSetChanged();
                }
*/
                // 取消圆点选中
                noSelectDotView = llytDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot);
                ViewGroup.LayoutParams layoutParams = noSelectDotView.getLayoutParams();
                layoutParams.width = DensityUtil.dip2px(mContext, 6);
                layoutParams.height = DensityUtil.dip2px(mContext, 6);
                noSelectDotView.setLayoutParams(layoutParams);
                noSelectDotView.setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                selectDotView = llytDot.getChildAt(position)
                        .findViewById(R.id.v_dot);
                layoutParams = selectDotView.getLayoutParams();
                layoutParams.width = DensityUtil.dip2px(mContext, 14);
                layoutParams.height = DensityUtil.dip2px(mContext, 6);
                selectDotView.setLayoutParams(layoutParams);
                selectDotView.setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void initRefreshLayout() {
        //设置 Header 背景为透明
        refreshLayout.setPrimaryColorsId(R.color.transparent, R.color.text_white);
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getActiveList();
                getLiveList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                getLiveList();
            }
        });
    }


    @Override
    public void initData() {
        rlvLiveList.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        mLiveListAdapter = new HomeLiveListAdapter(mContext);
        rlvLiveList.setAdapter(mLiveListAdapter);
        mLiveListAdapter.appendToList(mLiveInfoList);

        getLiveList();


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
    }

    //获取直播列表
    private void getLiveList() {

        BaseOkHttpClient.newBuilder()
                .addParam("topic_id", "9999")
                .url(NetUrlUtils.INDEX_GET_TOPIC_LIVE)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取首页直播列表----onSuccess:" + json);
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                if (!StringUtils.isEmpty(json)) {
                    List<LiveInfoBean> list = JSONUtils.jsonString2Beans(json, LiveInfoBean.class);
                    if (list != null && !list.isEmpty()) {
                        if (isRefresh) {
                            mLiveInfoList = list;
                        } else {
                            mLiveInfoList.addAll(list);
                        }
                        mLiveListAdapter.refreshList(mLiveInfoList);
                    }
                }

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取首页直播列表----onError:" + msg);
                ToastUtils.show(mContext, msg);
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取首页直播列表----onFailure:" + e.getMessage());
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }
        });


    }


    //首页轮播图
    private void getActiveList() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_BANNER_LIST)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "获取首页轮播图----onSuccess:" + result);
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                if (!StringUtils.isEmpty(result)) {
                  //  String data = JSONUtils.getNoteJson(result, "lists");
                    mBannerList = JSONUtils.jsonString2Beans(result, LiveInfoBean.class);
                    if (mBannerList != null && !mBannerList.isEmpty()) {
                        bannerHome.setImages(mBannerList);
                        bannerHome.start();
                        setOvalLayout();
                    }
                }
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取首页轮播图----onError:" + msg);
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取首页轮播图----onFailure:" + e.getMessage());
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }
        });
    }

}
