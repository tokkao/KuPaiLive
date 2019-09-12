package com.benben.kupaizhibo.frag;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.commoncore.utils.StatusBarUtils;
import com.benben.kupaizhibo.LazyBaseFragments;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.HomeViewPagerAdapter;
import com.benben.kupaizhibo.ui.home.SearchAnchorActivity;
import com.benben.kupaizhibo.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 首页fragment
 */
public class MainHomeFragment extends LazyBaseFragments {

    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.vp_home)
    NoScrollViewPager vpHome;
    @BindView(R.id.rlyt_top_tab)
    RelativeLayout rlytTopTab;

    public static MainHomeFragment getInstance() {
        MainHomeFragment sf = new MainHomeFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_home, null);
        return mRootView;
    }

    @Override
    public void initView() {

        ArrayList<String> tabList = new ArrayList<>();
        tabList.add(getString(R.string.home_live));
        tabList.add(getString(R.string.home_one_to_one));
        for (String name : tabList) {
            xTablayout.addTab(xTablayout.newTab().setText(name));
        }

        List<LazyBaseFragments> mFragmentList = new ArrayList<>();
        mFragmentList.add(HomeLiveFragment.getInstance());
        mFragmentList.add(HomeOneToOneFragment.getInstance());
        vpHome.setNoScroll(true);
        vpHome.setAdapter(new HomeViewPagerAdapter(getChildFragmentManager(), tabList, mFragmentList));
        xTablayout.setupWithViewPager(vpHome);


        rlytTopTab.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

    }


    @Override
    public void initData() {

        xTablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    xTablayout.getTabAt(0).select();
                    showUnDevelopDialog();
                }

            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
    }

    private void showUnDevelopDialog() {
        View view = View.inflate(mContext, R.layout.dialog_not_available, null);

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            //去除系统自带的margin
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置dialog在界面中的属性
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        alertDialog.show();

        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });
    }
    @OnClick({R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search://搜索
                startActivity(new Intent(mContext, SearchAnchorActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }
}
