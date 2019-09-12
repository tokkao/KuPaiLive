package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.benben.commoncore.utils.InputCheckUtil;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.AuthStatusBean;
import com.benben.kupaizhibo.bean.DefaultControlStatusBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.hyphenate.helper.StatusBarUtils;
import com.kongzue.dialog.v3.MessageDialog;
import com.suke.widget.SwitchButton;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 家长控制模式
 */
public class ParentalControlActivity extends BaseActivity {

    private static final String TAG = "ParentalControlActivity";
    @BindView(R.id.sb_control)
    SwitchButton sbControl;
    @BindView(R.id.view_switch)
    View viewSwitch;
    @BindView(R.id.edt_authentic_info)
    EditText edtAuthenticInfo;
    private int status = 0;  // 0=》控制 1=》取消控制
    private boolean isManual = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_parental_control;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.parents_control));

        getDefaultStatus();
        sbControl.setOnCheckedChangeListener((view, isChecked) -> {
            if (isManual) {
                status = isChecked ? 0 : 1;
                switchStatus();
            }
        });

        viewSwitch.setOnClickListener(view -> {
            //检查是否已经实名认证
            getRealNameAuthStatus();

        });
    }

    //改变控制状态
    private void changeControlStatus() {
        if (StringUtils.isEmpty(edtAuthenticInfo.getText().toString().trim())) {
            toast(mContext.getResources().getString(R.string.id_number_not_null));
            return;
        }
        if (!InputCheckUtil.checkIdCard(edtAuthenticInfo.getText().toString().trim())) {
            toast(mContext.getResources().getString(R.string.enter_the_right_id_number));
            return;
        }
        isManual = true;
        sbControl.toggle();
    }

    private void getDefaultStatus() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_DEFAULT_CONTROL_STATUS)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "默认控制状态----onSuccess:" + json);
                DefaultControlStatusBean defaultControlStatusBean = JSONUtils.jsonString2Bean(json, DefaultControlStatusBean.class);
                status = defaultControlStatusBean.getControl();
                isManual = false;
                sbControl.setChecked(status == 0);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "默认控制状态----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "默认控制状态----onFailure:" + e.getMessage());
            }
        });


    }

    private void switchStatus() {


        BaseOkHttpClient.newBuilder()
                .addParam("type", status) // 0 控制 1 取消控制
                .addParam("sfz", edtAuthenticInfo.getText().toString().trim())
                .url(NetUrlUtils.SWITCH_PARENTAL_CONTROL_MODE)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "切换家长控制模式----onSuccess:" + json);
                toast(msg);
                finish();
            }

            @Override
            public void onError(int code, String msg) {

                LogUtils.e(TAG, "切换家长控制模式----onError:" + msg);
                ToastUtils.show(mContext, msg);
                isManual = false;
                sbControl.toggle();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                isManual = false;
                sbControl.toggle();
                LogUtils.e(TAG, "切换家长控制模式----onFailure:" + e.getMessage());
            }
        });


    }

    //查询实名认证状态
    private void getRealNameAuthStatus() {
        BaseOkHttpClient.newBuilder()
                .addParam("type", 0)//0=>实名认证 1=>主播认证
                .url(NetUrlUtils.AUTHENTICATION_STATUS)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "实名认证状态----onSuccess:" + json);
                if (StringUtils.isEmpty(json)) {
                    ToastUtils.show(mContext, mContext.getResources().getString(R.string.server_exception));
                    return;
                }
                AuthStatusBean lstAuthStatus = JSONUtils.jsonString2Bean(json,
                        AuthStatusBean.class);

                //-1失败，0待审核，1成功

                switch (lstAuthStatus.getType()){
                    case -1:
                        MessageDialog.show(mContext, getResources().getString(R.string.real_name_authentication), getResources().getString(R.string.authentication_failed), getResources().getString(R.string.again_authentication), getResources().getString(R.string.negative))
                                .setOnOkButtonClickListener((baseDialog, v) -> {
                                    startActivity(new Intent(mContext, RealNameAuthenticationActivity.class).putExtra("auth_info",lstAuthStatus));
                                    return false;
                                }).show();
                        break;
                    case 0:
                        MessageDialog.show(mContext, getResources().getString(R.string.real_name_authentication), getResources().getString(R.string.wait_authentication), getResources().getString(R.string.check_authentication), getResources().getString(R.string.negative))
                                .setOnOkButtonClickListener((baseDialog, v) -> {
                                    startActivity(new Intent(mContext, RealNameAuthenticationActivity.class).putExtra("auth_info",lstAuthStatus));
                                    return false;
                                }).show();
                        break;
                    case 1:
                        changeControlStatus();
                        break;
                }

            }

            @Override
            public void onError(int code, String msg) {
               // toast(msg);
                LogUtils.e(TAG, "实名认证状态----onError:" + msg);
                MessageDialog.show(mContext, getResources().getString(R.string.real_name_authentication), getResources().getString(R.string.open_control_need_authentication), getResources().getString(R.string.go_to_authentication), getResources().getString(R.string.negative))
                        .setOnOkButtonClickListener((baseDialog, v) -> {
                            startActivity(new Intent(mContext, RealNameAuthenticationActivity.class));
                            return false;
                        }).show();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                toast(e.getMessage());
                LogUtils.e(TAG, "实名认证状态----onFailure:" + e.getMessage());
            }


        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

}
