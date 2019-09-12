package com.benben.kupaizhibo.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.benben.kupaizhibo.LazyBaseFragments;

import java.util.List;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:首页viewpager适配器
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    //tab数据
    private List<String> mDatas;

    private List<LazyBaseFragments> mFragments;

    public HomeViewPagerAdapter(FragmentManager fm, List<String> mDatas, List<LazyBaseFragments> fragments) {
        super(fm);
        this.mDatas = mDatas;
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size() == mFragments.size() ? mFragments.size() : 0;
    }

    /**
     * 重写此方法，返回TabLayout的内容
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mDatas.get(position);
    }
}
