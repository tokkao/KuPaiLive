package com.benben.kupaizhibo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.benben.commoncore.utils.ActivityManagerUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.hyphenate.helper.StatusBarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/30.
 * 模板Activity的基类 对于简单的页面不使用mvp  Activity直接继承BaseActivity 如果需要使用mvp 则继承MvpBaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected AppCompatActivity mContext;
    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityManagerUtils.add(this);
        this.mContext = this;
        setStatusBar();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this, this);
        initData();

    }

    protected void clearFocus() {
        //判断如果有文本框正获取焦点，尝试隐藏键盘
        View focusView = getWindow().getDecorView().getRootView().findFocus();
        if (focusView != null && focusView instanceof EditText) {
            ScreenUtils.closeKeybord((EditText) focusView, mContext);
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    /**
     * 设置标题
     *
     * @param title 标题名字
     */
    protected void initTitle(String title) {
        TextView tvTitle = findViewById(R.id.center_title);
        tvTitle.setText(title);
        RelativeLayout rlBack = findViewById(R.id.rl_back);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManagerUtils.remove(this);
                finish();
            }
        });
    }

    protected void setStatusBar() {
        //默认设置状态栏透明
        StatusBarUtils.setStatusBarColor(mContext, getStatusBarColor());
        //默认设置状态栏文字颜色
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, needStatusBarDarkText());
    }


    //是否需要将状态栏文字改成黑色，默认为否
    protected boolean needStatusBarDarkText() {
        return false;
    }

    //修改状态栏颜色值,默认透明
    protected @ColorRes
    int getStatusBarColor() {
        return R.color.transparent;
    }

    public void showProgress(){

    }

    public void closeProgress(){

    }

    public void toast(String strMsg){
        ToastUtils.show(this,strMsg);
    }

    public void toast(int resid){
        ToastUtils.show(this,resid);
    }

}
