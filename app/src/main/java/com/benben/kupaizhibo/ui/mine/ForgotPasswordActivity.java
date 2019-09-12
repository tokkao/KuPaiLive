package com.benben.kupaizhibo.ui.mine;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.InputCheckUtil;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.VerifyCodeBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.widget.VerifyCodeButton;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 忘记密码/密码修改
 */
public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.edt_forgot_phone)
    EditText edtForgotPhone;
    @BindView(R.id.edt_forgot_verify_code)
    EditText edtForgotVerifyCode;
    @BindView(R.id.btn_forgot_get_verify_code)
    VerifyCodeButton btnForgotGetVerifyCode;
    @BindView(R.id.edt_forgot_new_pwd)
    EditText edtForgotNewPwd;
    @BindView(R.id.edt_forgot_re_pwd)
    EditText edtForgotRePwd;
    @BindView(R.id.btn_forgot_confirm)
    Button btnForgotConfirm;

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;

    //获取验证返回结果
    private VerifyCodeBean mVerifyCode;
    private int type;  // 0 忘记密码  1 密码修改

    @Override
    protected int getStatusBarColor() {
        return R.color.color_FFFFFF;
    }

    @Override
    protected boolean needStatusBarDarkText() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_pwd;
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", 0);
        centerTitle.setText(type == 0 ? R.string.forget_password : R.string.password_modify);
    }

    @OnClick({R.id.rl_back, R.id.btn_forgot_get_verify_code,
            R.id.btn_forgot_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                onBackPressed();
                break;
            case R.id.btn_forgot_get_verify_code:
                getVerifyCode();
                break;
            case R.id.btn_forgot_confirm:
                doRestPwd();
                break;
        }
    }

    private void doRestPwd() {
        String phone = edtForgotPhone.getText().toString().trim();
        String code = edtForgotVerifyCode.getText().toString().trim();
        String newPwd = edtForgotNewPwd.getText().toString().trim();
        String rePwd = edtForgotRePwd.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            toast(getString(R.string.enter_the_phone_number));
            return;
        }
        if(!InputCheckUtil.checkPhoneNum(phone)){
            toast(mContext.getResources().getString(R.string.enter_the_right_mobile));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            toast(getString(R.string.enter_the_verification_code));
            return;
        }
        if (edtForgotNewPwd.getText().toString().contains(" ") || edtForgotRePwd.getText().toString().contains(" ")) {
            toast(getString(R.string.pwd_cant_include_blank));
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            toast(getString(R.string.enter_the_new_pwd));
            return;
        }
        if (newPwd.length() < 6 || newPwd.length() > 12) {
            toast(getString(R.string.password_length));
            return;
        }
        if (!newPwd.equals(rePwd)) {
            toast(getString(R.string.pwd_no_same));
            return;
        }
        if (mVerifyCode == null) {
            toast(getString(R.string.get_verification_code_first));
            return;
        }
        BaseOkHttpClient.newBuilder()
                .addParam("scene", "login")
                .addParam("password", newPwd)
                .addParam("mobile", phone)
                .addParam("code", code)
                .addParam("code_id", mVerifyCode.getCode_id())
                .url(NetUrlUtils.FORGET_PASSWORD)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                toast(getString(R.string.pwd_modify_successful));
                finish();
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(ForgotPasswordActivity.class.getSimpleName(), msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(ForgotPasswordActivity.class.getSimpleName(), e.getMessage());
            }
        });
    }

    private void getVerifyCode() {
        String phone = edtForgotPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            toast(getString(R.string.enter_the_phone_number));
            return;
        }
        if(!InputCheckUtil.checkPhoneNum(phone)){
            toast(mContext.getResources().getString(R.string.enter_the_right_mobile));
            return;
        }


        BaseOkHttpClient.newBuilder()
                .addParam("mobile", phone)
                .addParam("scene", "login")
                .url(NetUrlUtils.GET_CODE)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                mVerifyCode = JSONUtils.jsonString2Bean(result, VerifyCodeBean.class);
                //开启倒计时
                btnForgotGetVerifyCode.startTimer();
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(ForgotPasswordActivity.class.getSimpleName(), msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(ForgotPasswordActivity.class.getSimpleName(), e.getMessage());
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (ScreenUtils.isShowKeyboard(mContext, edtForgotNewPwd))
            ScreenUtils.closeKeybord(edtForgotNewPwd, mContext);
    }

}
