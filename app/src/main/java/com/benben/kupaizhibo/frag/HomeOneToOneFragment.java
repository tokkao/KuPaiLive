package com.benben.kupaizhibo.frag;

import android.view.LayoutInflater;
import android.view.View;

import com.benben.kupaizhibo.LazyBaseFragments;
import com.benben.kupaizhibo.R;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 首页-1v1fragment
 */
public class HomeOneToOneFragment extends LazyBaseFragments {



    public static HomeOneToOneFragment getInstance() {
        HomeOneToOneFragment sf = new HomeOneToOneFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_one_to_one, null);
        return mRootView;
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {

    }
}
