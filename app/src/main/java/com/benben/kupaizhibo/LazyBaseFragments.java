package com.benben.kupaizhibo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 懒加载fragment基类
 */
public abstract class LazyBaseFragments extends Fragment {
    protected View mRootView;

    /**
     * 是否为可见状态
     */
    protected boolean isVisible;
    /**
     * 是否初始视图完成
     */
    private boolean isPrepared;

    protected Activity mContext;

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = bindLayout(inflater);
            unbinder = ButterKnife.bind(this, mRootView);
            initView();
            initData();
            return mRootView;
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //如果setUserVisibleHint()在rootView创建前调用时，那么
        //就等到rootView创建完后才回调onFragmentVisibleChange(true)
        //保证onFragmentVisibleChange()的回调发生在rootView创建完成之后，以便支持ui操作
        if (mRootView == null) {
            mRootView = view;
        }
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        onVisible();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isPrepared = false;
    }

    //用于取消页面UI渲染
    public boolean isPrepared() {
        return isPrepared;
    }

    /**
     * setUserVisibleHint是在onCreateView之前调用的
     * <p>
     * 延迟加载，需要和ViewPager配合使用,参考以下文章介绍
     * https://blog.csdn.net/crkky/article/details/81514284
     * <p>
     * 用到时可复写 loadData和onInvisible
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见做懒加载
     */
    private void onVisible() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isVisible && isPrepared) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isVisible = false;
//            isPrepared = false;
        }
    }

    /**
     * 不可见-做一些销毁工作
     */
    protected void onInvisible() {


    }

    /**
     * 绑定布局
     *
     * @param inflater
     * @return
     */
    public abstract View bindLayout(LayoutInflater inflater);

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 延迟加载，需要和ViewPager配合使用
     * 用到时可复写此方法
     */
    protected void loadData() {

    }

    protected void showProgress() {
    }

    protected void closeProgress() {
    }
}
