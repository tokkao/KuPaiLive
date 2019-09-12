package com.benben.kupaizhibo.ui.mine;


import android.widget.TextView;

import com.benben.commoncore.utils.AppUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.hyphenate.helper.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.tv_settings_version_name)
    TextView tvSettingsVersionName;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {
        centerTitle.setText(mContext.getResources().getString(R.string.about_us));
        tvSettingsVersionName.setText(mContext.getResources().getString(R.string.version_code, AppUtils.getVerName(mContext)));

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }
    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
