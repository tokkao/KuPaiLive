package com.benben.kupaizhibo.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.benben.kupaizhibo.LazyBaseFragments;

import java.util.List;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 主界面PagerAdapter
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<LazyBaseFragments> fragments;
    private FragmentManager fragmentManager;

    public MainViewPagerAdapter(FragmentManager fm, List<LazyBaseFragments> fragmentList) {
        super(fm);
        this.fragments = fragmentList;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
