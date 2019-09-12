package com.benben.kupaizhibo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.ContributionPagerAdapter;

import java.util.ArrayList;


/**
 * 贡献榜
 */

public class ContributionPopupWindow extends PopupWindow {


    private static final String TAG = "ContributionPopupWindow";
    private View mView;
    private Activity mContext;
    private OnButtonClickListener mOnButtonClickListener;
    private  FragmentManager mFragmentManager;
    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }
    public ContributionPopupWindow(Activity activity, FragmentManager supportFragmentManager) {
        super(activity);
        this.mContext = activity;
        this.mFragmentManager = supportFragmentManager;
        init();
    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_contribution, null);
        // 导入布局
        this.setContentView(mView);
        ViewHolder holder = new ViewHolder(mView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        final ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);

        ArrayList<String> tabTitleList = new ArrayList<>();
        tabTitleList.add(mContext.getResources().getString(R.string.day_list));
        tabTitleList.add(mContext.getResources().getString(R.string.week_list));
        tabTitleList.add(mContext.getResources().getString(R.string.month_list));
        for (int i = 0; i < tabTitleList.size(); i++) {
            holder.xTablayout.addTab(holder.xTablayout.newTab().setText(tabTitleList.get(i)));
        }
        ArrayList<View> mFragmentList = new ArrayList<>();

        for (int i = 0; i < tabTitleList.size(); i++) {
            ContributionFrameLayout fragment = new ContributionFrameLayout(mContext,i);
//            Bundle bundle = new Bundle();
//            bundle.putInt("type", i);
//            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
        }
        holder.vpContent.setAdapter(new ContributionPagerAdapter(mFragmentList,tabTitleList));
//        holder.vpContent.setAdapter(new HomeViewPagerAdapter(mFragmentManager, tabTitleList, mFragmentList));
        holder.xTablayout.setupWithViewPager(holder.vpContent);


        //关闭
        holder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //编辑
        holder.rlytEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnButtonClickListener != null){
                    mOnButtonClickListener.OnClickEditor();
                }
            }
        });
        //收藏
        holder.rlytCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnButtonClickListener != null){
                    mOnButtonClickListener.OnClickCollection();
                }
            }
        });
        //分享
        holder.rlytSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnButtonClickListener != null){
                    mOnButtonClickListener.OnClickShare();
                }
            }
        });
        //贡献榜
        holder.rlytContribu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mOnButtonClickListener != null){
                    mOnButtonClickListener.OnClickContribution();
                }
            }
        });
        //礼物
        holder.ivGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnButtonClickListener != null){
                    mOnButtonClickListener.OnClickGift();
                }
            }
        });
    }


    //加载弹窗
    public void showWindow() {
        showAtLocation(mContext.getWindow().getDecorView().getRootView(), Gravity.TOP, 0, 0);
    }

    public interface OnButtonClickListener {
        void OnClickEditor();

        void OnClickCollection();

        void OnClickShare();

        void OnClickContribution();

        void OnClickGift();
    }

    public static
    class ViewHolder {
        public View rootView;
        public XTabLayout xTablayout;
        public ImageView ivClose;
        public ViewPager vpContent;
        public RelativeLayout rlytEditor;
        public RelativeLayout rlytCollection;
        public RelativeLayout rlytSharing;
        public RelativeLayout rlytContribu;
        public ImageView ivGift;
        public LinearLayout llytOptions;
        public LinearLayout llytRoot;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.xTablayout = (XTabLayout) rootView.findViewById(R.id.xTablayout);
            this.ivClose = (ImageView) rootView.findViewById(R.id.iv_close);
            this.vpContent = (ViewPager) rootView.findViewById(R.id.vp_content);
            this.rlytEditor = (RelativeLayout) rootView.findViewById(R.id.rlyt_editor);
            this.rlytCollection = (RelativeLayout) rootView.findViewById(R.id.rlyt_collection);
            this.rlytSharing = (RelativeLayout) rootView.findViewById(R.id.rlyt_sharing);
            this.rlytContribu = (RelativeLayout) rootView.findViewById(R.id.rlyt_contribu);
            this.ivGift = (ImageView) rootView.findViewById(R.id.iv_gift);
            this.llytOptions = (LinearLayout) rootView.findViewById(R.id.llyt_options);
            this.llytRoot = (LinearLayout) rootView.findViewById(R.id.llyt_root);
        }

    }
}
