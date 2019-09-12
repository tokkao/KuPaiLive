package com.benben.kupaizhibo.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Created by: wanghk 2019-07-10.
 * Describe:
 */
public class ContributionPagerAdapter extends PagerAdapter {

    private List<View> mList;
    private List<String> mTitle;

    public ContributionPagerAdapter(List<View> list, List<String> titles) {
        this.mList = list;
        this.mTitle = titles;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mList.get(position);
        container.addView(view);
        //最后要返回的是控件本身
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        //         super.destroyItem(container, position, object);
    }

    //目的是展示title上的文字，
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }


}
