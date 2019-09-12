package com.benben.kupaizhibo.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.benben.commoncore.utils.DensityUtil;
import com.benben.commoncore.utils.LogUtils;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.GiftGridViewAdapter;
import com.benben.kupaizhibo.adapter.GiftTabAdapter;
import com.benben.kupaizhibo.adapter.ViewPagerAdapter;
import com.benben.kupaizhibo.bean.LiveTopicTagBean;
import com.benben.kupaizhibo.bean.socket.GiftCategoryInfoBean;
import com.benben.kupaizhibo.bean.socket.GiftInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能:直播间,礼物的弹出pop
 */
public class LiveGiftListPopupWindow extends PopupWindow {
    RecyclerView rlvTabContent;
    ViewPager vpContent;
    LinearLayout llytDot;
    TextView tvBobi;
    EditText edtNum;
    RelativeLayout rlytSend;
    RelativeLayout rlyt_root;
    LinearLayout llPop;
    private View mView;
    private Context mContext;
    private OnButtonClickListener mOnButtonClickListener;
    private List<GiftCategoryInfoBean> mGiftCategoryInfoBeanList;
    private List<View> mPagerList;//页面集合
    /*总的页数*/
    private int pageCount;
    /*每一页显示的个数*/
    private int pageSize = 8;
    /*当前显示的是第几页*/
    private int curIndex = 0;
    private GiftGridViewAdapter[] arr;
    private LayoutInflater mInflater;
    private ViewPagerAdapter viewPagerAdapter;
    private GiftInfoBean mCurrentSelectModel;
    private int mSelectParentIndex;
    private int mSelectSubIndex;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }

    public LiveGiftListPopupWindow(Activity activity, List<GiftCategoryInfoBean> list) {
        super(activity);
        this.mContext = activity;
        this.mGiftCategoryInfoBeanList = list;
        init();
    }

    public void setTvBobi(String bobi) {
        tvBobi.setText(mContext.getResources().getString(R.string.coin_balance, bobi));

    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_live_gift_list, null);
        rlvTabContent = mView.findViewById(R.id.rlv_tab_content);
        vpContent = mView.findViewById(R.id.vp_content);
        llytDot = mView.findViewById(R.id.llyt_dot);
        tvBobi = mView.findViewById(R.id.tv_bobi);
        edtNum = mView.findViewById(R.id.edt_num);
        rlytSend = mView.findViewById(R.id.rlyt_send);
        rlyt_root = mView.findViewById(R.id.rlyt_root);
        llPop = mView.findViewById(R.id.ll_pop);
        // 导入布局
        this.setContentView(mView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        rlyt_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setOutsideTouchable(true);
        setFocusable(true);
        final ColorDrawable dw = new ColorDrawable(0);
        setBackgroundDrawable(dw);
        initData();
        rlytSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonClickListener == null) {
                    return;
                }
                mOnButtonClickListener.OnClickSelectGiftInfo(mCurrentSelectModel, mSelectParentIndex, mSelectSubIndex);
            }
        });
    }

    private void initData() {

        //初始化标签
        List<LiveTopicTagBean> tagBeanList = new ArrayList<>();
        LiveTopicTagBean liveTopicTagBean;
        for (int i = 0; i < mGiftCategoryInfoBeanList.size(); i++) {
            liveTopicTagBean = new LiveTopicTagBean();
            liveTopicTagBean.setId(mGiftCategoryInfoBeanList.get(i).getId());
            liveTopicTagBean.setTitle(mGiftCategoryInfoBeanList.get(i).getName());
            if (i == 0) {
                liveTopicTagBean.setSelect(true);
            }
            tagBeanList.add(liveTopicTagBean);
        }

        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置回收复用池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        rlvTabContent.setRecycledViewPool(recycledViewPool);
        recycledViewPool.setMaxRecycledViews(RecyclerView.HORIZONTAL, 10);
        rlvTabContent.setLayoutManager(lm);
        GiftTabAdapter giftTabAdapter = new GiftTabAdapter(mContext);
        rlvTabContent.setAdapter(giftTabAdapter);
        giftTabAdapter.appendToList(tagBeanList);
        giftTabAdapter.setOnButtonClickListener((dataBean, position) -> {
            giftTabAdapter.notifyDataSetChanged();
            llytDot.removeAllViews();
            setGiftData(position);

        });
        llytDot.removeAllViews();
        //初始化下面信息
        setGiftData(0);

    }

    private void setGiftData(int index) {
        mInflater = LayoutInflater.from(mContext);
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mGiftCategoryInfoBeanList.get(index).getGift().size() * 1.0 / pageSize);
        arr = new GiftGridViewAdapter[pageCount];
        //viewpager
        mPagerList = new ArrayList<>();
        for (int j = 0; j < pageCount; j++) {
            final ActiveLiveGridView gridview = (ActiveLiveGridView) mInflater.inflate(R.layout.vp_gridview, vpContent, false);
            final GiftGridViewAdapter gridAdapter = new GiftGridViewAdapter(mContext, mGiftCategoryInfoBeanList.get(index).getGift(), j);
          /*  List<GiftInfoBean> giftInfoBeans = mGiftCategoryInfoBeanList.get(index).getGift();
            for (GiftInfoBean bean : giftInfoBeans) {
                bean.setSelect(false);
            }*/
            gridview.setAdapter(gridAdapter);
            arr[j] = gridAdapter;
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < mGiftCategoryInfoBeanList.get(index).getGift().size(); i++) {
                        mCurrentSelectModel = mGiftCategoryInfoBeanList.get(index).getGift().get(i);
                        if (i == id) {
                            if (mCurrentSelectModel.isSelect()) {
                                mCurrentSelectModel.setSelect(false);
                            } else {
                                mCurrentSelectModel.setSelect(true);
                            }
                            LogUtils.e("tag", "==点击位置：" + i + "..id:" + id);
                            mSelectParentIndex = index;
                            mSelectSubIndex = i;

                        } else {
                            mCurrentSelectModel.setSelect(false);
                        }
                        if (mGiftCategoryInfoBeanList.get(index).getGift().get(position).isSelect()) {
                            mCurrentSelectModel = mGiftCategoryInfoBeanList.get(index).getGift().get(position);

                        } else {
                            mCurrentSelectModel = null;

                        }

                    }
                    LogUtils.e("tag", "状态：" + mGiftCategoryInfoBeanList.get(index).getGift().toString());
                    for (int i = 0; i < arr.length; i++) {
                        arr[i].notifyDataSetChanged();
                    }
                    viewPagerAdapter.notifyDataSetChanged();
                }
            });
            mPagerList.add(gridview);
        }
        viewPagerAdapter = new ViewPagerAdapter(mPagerList, mContext);
        vpContent.setAdapter(viewPagerAdapter);
        if (pageCount == 0) {
            return;
        }
        setOvalLayout();
    }

    View selectDotView;
    View noSelectDotView;

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            llytDot.addView(mInflater.inflate(R.layout.gift_dot, null));
        }
        // 默认显示第一页
        selectDotView = llytDot.getChildAt(0).findViewById(R.id.v_dot);
        selectDotView.setBackgroundResource(R.drawable.dot_selected);
        ViewGroup.LayoutParams layoutParams = selectDotView.getLayoutParams();
        layoutParams.width = DensityUtil.dip2px(mContext, 14);
        layoutParams.height = DensityUtil.dip2px(mContext, 6);
        selectDotView.setLayoutParams(layoutParams);
        vpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                for (int i = 0; i < arr.length; i++) {
                    arr[i].notifyDataSetChanged();
                }

                // 取消圆点选中
                noSelectDotView = llytDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot);
                ViewGroup.LayoutParams layoutParams = noSelectDotView.getLayoutParams();
                layoutParams.width = DensityUtil.dip2px(mContext, 6);
                layoutParams.height = DensityUtil.dip2px(mContext, 6);
                noSelectDotView.setLayoutParams(layoutParams);
                noSelectDotView.setBackgroundResource(R.drawable.gift_dot_normal);
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

    public interface OnButtonClickListener {
        void OnClickSelectGiftInfo(GiftInfoBean giftInfoBean, int parentPosition, int subPosition);
    }
}
