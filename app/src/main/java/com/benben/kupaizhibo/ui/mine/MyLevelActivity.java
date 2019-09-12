package com.benben.kupaizhibo.ui.mine;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.LazyBaseFragments;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.HomeViewPagerAdapter;
import com.benben.kupaizhibo.frag.MyLevelFragment;
import com.hyphenate.helper.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 我的等级
 */
public class MyLevelActivity extends BaseActivity {
    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_level;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.my_level));

        ArrayList<String> tabList = new ArrayList<>();
        tabList.add(getString(R.string.user_level));
        tabList.add(getString(R.string.anchor_level));
        for (String name : tabList) {
            xTablayout.addTab(xTablayout.newTab().setText(name));
        }

        List<LazyBaseFragments> mFragmentList = new ArrayList<>();
        MyLevelFragment userLevelFragment = MyLevelFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("type",0);
        userLevelFragment.setArguments(bundle);
        MyLevelFragment anchorLevelFragment = MyLevelFragment.getInstance();
        Bundle anchorBundle = new Bundle();
        anchorBundle.putInt("type",1);
        anchorLevelFragment.setArguments(anchorBundle);

        mFragmentList.add(userLevelFragment);
        mFragmentList.add(anchorLevelFragment);

        vpContent.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager(), tabList, mFragmentList));
        xTablayout.setupWithViewPager(vpContent);

    }


    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

}
