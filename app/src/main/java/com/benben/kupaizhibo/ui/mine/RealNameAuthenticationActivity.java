package com.benben.kupaizhibo.ui.mine;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.benben.commoncore.utils.InputCheckUtil;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.AuthStatusBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.hyphenate.helper.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe:实名认证
 */
public class RealNameAuthenticationActivity extends BaseActivity {
    private static final String TAG = "RealNameAuthenticationActivity";
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_id_number)
    EditText edtIdNumber;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private AuthStatusBean authStatusBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_real_name_authentication;
    }

    @Override
    protected void initData() {

        authStatusBean = (AuthStatusBean) getIntent().getSerializableExtra("auth_info");
        if(authStatusBean != null){
            switch (authStatusBean.getType()) {//-1失败，0待审核，1成功
                case 0://待审核
                    initTitle(getString(R.string.wait_authentication));
                    btnConfirm.setVisibility(View.GONE);
                    edtName.setEnabled(false);
                    edtIdNumber.setEnabled(false);
                    break;
                case -1://失败
                    initTitle(getString(R.string.authentication_failed));
                    break;
                case 1://成功
                    initTitle(getString(R.string.authentication_success));
                    btnConfirm.setVisibility(View.GONE);
                    edtName.setEnabled(false);
                    edtIdNumber.setEnabled(false);
                    break;
            }
        }else {
            initTitle(getString(R.string.real_name_authentication));
        }
       // edtName.requestFocus();

        refreshData();
    }

    private void refreshData() {
        if(authStatusBean != null){
            edtIdNumber.setText(authStatusBean.getSfz());
            edtName.setText(authStatusBean.getName());
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

    @OnClick({R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                confirmInfo();
                break;
        }
    }

    //提交信息
    private void confirmInfo() {
        String name = edtName.getText().toString().trim();
        String idNumber = edtIdNumber.getText().toString().trim();
        if (StringUtils.isEmpty(name)) {
            toast(getString(R.string.name_not_null));
            return;
        }
        if (StringUtils.isEmpty(idNumber)) {
            toast(getString(R.string.id_number_not_null));
            return;
        }
        if(!InputCheckUtil.checkIdCard(idNumber)){
            toast(mContext.getResources().getString(R.string.enter_the_right_id_number));
            return;
        }

        BaseOkHttpClient.newBuilder()
                .addParam("name", edtName.getText().toString().trim())
                .addParam("sfz_id", edtIdNumber.getText().toString().trim())
                .url(NetUrlUtils.REAL_NAME_AUTHENTICATION)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "实名认证----onSuccess:" + json);
                toast(msg);
                finish();

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "实名认证----onError:" + msg);
                ToastUtils.show(mContext,msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "实名认证----onFailure:" + e.getMessage());
            }
        });



    }


    @Override
    protected void onPause() {
        super.onPause();
        if (ScreenUtils.isShowKeyboard(mContext, edtIdNumber))
            ScreenUtils.closeKeybord(edtIdNumber, mContext);
    }

}
