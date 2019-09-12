package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.DeviceUtils;
import com.benben.commoncore.utils.InputCheckUtil;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.UserLoginSuccessBean;
import com.benben.kupaizhibo.bean.VerifyCodeBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.NormalWebViewActivity;
import com.benben.kupaizhibo.utils.LoginCheckUtils;
import com.benben.kupaizhibo.widget.VerifyCodeButton;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.helper.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_verify_code)
    EditText edtVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    VerifyCodeButton btnGetVerifyCode;
    @BindView(R.id.edt_new_pwd)
    EditText edtNewPwd;
    @BindView(R.id.edt_re_pwd)
    EditText edtRePwd;
    @BindView(R.id.edt_invitation_code)
    EditText edtInvitationCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_user_registration_agreement)
    TextView tvUserRegistrationAgreement;


    //获取验证返回结果
    private VerifyCodeBean mVerifyCode;

    @Override
    protected void initData() {
        initTitle(getString(R.string.new_user_register));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }


    @OnClick({R.id.btn_confirm, R.id.tv_user_registration_agreement, R.id.btn_get_verify_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user_registration_agreement:
                String url = "http://bjzb.hncjne.com/xy.html";
                NormalWebViewActivity.startWithData(mContext, url, mContext.getResources().getString(R.string.register_agreement), true, true);

                // getArticleList();
                break;
            case R.id.btn_get_verify_code:
                getVerifyCode();
                break;
            case R.id.btn_confirm:
                doRegister();
                break;
        }
    }

    //获取直播公约
    private void getArticleList() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.ARTICLE_GET_LIST)
                .get().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "获取注册协议----onSuccess:" + result);
                String url = JSONUtils.getNoteJson(result, "zhuce_xieyi");
                NormalWebViewActivity.startWithData(mContext, url, mContext.getResources().getString(R.string.register_agreement), true, true);

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取注册协议----onError:" + msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取注册协议----onFailure:" + e.getMessage());
            }
        });

    }


    //点击注册
    private void doRegister() {
        String account = edtPhone.getText().toString().trim();//用户名
        String code = edtVerifyCode.getText().toString().trim();//验证码
        String newPassword = edtNewPwd.getText().toString().trim();//新密码
        String rePassword = edtRePwd.getText().toString().trim();//再次确认密码
        String invitationCode = edtInvitationCode.getText().toString().trim();//邀请码

        if (StringUtils.isEmpty(account)) {
            toast(getString(R.string.iphone_number_not_null));
            return;
        }
        if(!InputCheckUtil.checkPhoneNum(account)){
            toast(mContext.getResources().getString(R.string.enter_the_right_mobile));
            return;
        }
        if (StringUtils.isEmpty(code)) {
            toast(getString(R.string.verification_code_not_null));
            return;
        }
        if (edtNewPwd.getText().toString().contains(" ") || edtRePwd.getText().toString().contains(" ")) {
            toast(getString(R.string.pwd_cant_include_blank));
            return;
        }
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(rePassword)) {
            toast(getString(R.string.password_not_null));
            return;
        }
        /*if (StringUtils.isEmpty(invitationCode)) {
            toast(getString(R.string.invitation_code_not_null));
            return;
        }*/

        if (newPassword.length() < 6 || newPassword.length() > 12) {
            toast(getString(R.string.password_length));
            return;
        }

        if (!newPassword.equals(rePassword)) {
            toast(getString(R.string.pwd_no_same));
            return;
        }

        if (mVerifyCode == null) {
            toast(getString(R.string.get_verification_code_first));
            return;
        }

        BaseOkHttpClient.newBuilder()
                .addParam("mobile", account)
                .addParam("code", code)
                .addParam("code_id", mVerifyCode.getCode_id())
                .addParam("scene", "register")
                .addParam("password", newPassword)
                .addParam("invite_code", invitationCode)
                .url(NetUrlUtils.REGIST_USER)
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "注册----onSuccess:" + result);
                autoLogin(account,newPassword);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "注册----onError:" + msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "注册----onFailure:" + e.getMessage());
            }
        });
    }

    //注册完之后自动登录
    private void autoLogin(String account, String newPassword) {
        String deviceId = DeviceUtils.getIMEI(this);


        BaseOkHttpClient.newBuilder()
                .addParam("username", account)
                .addParam("password", newPassword)
                .addParam("uuid", deviceId)
                .url(NetUrlUtils.USER_NAME_LOGIN)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "密码登录----onSuccess:" + result);
                UserLoginSuccessBean data = JSONUtils.jsonString2Bean(result, UserLoginSuccessBean.class);
                if (data != null) {
                    //保存用户信息
                    LoginCheckUtils.saveLoginInfo(data, account);
                   // toast(getString(R.string.login_successful));

                    startActivity(new Intent(mContext,SelectSexActivity.class));
                    //登录环信客服
                    //loginChatClient(data, account);
                    //登录环信IM
                    loginEmClient(data);
                } else {
                    toast(getString(R.string.user_not_exist_please_go_register));
                }
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "密码登录----onError:" + msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "密码登录----onFailure:" + e.getMessage());
            }
        });
    }

    //登录环信
    private void loginEmClient(UserLoginSuccessBean data) {
        if(StringUtils.isEmpty(data.getEasemob()))return;
        EMClient.getInstance().login("kpzb-" + data.getId(), data.getEasemob(), new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.e(TAG, "环信登录成功 "+"kpzb-" + data.getId()+ "        "+data.getEasemob() );

                finish();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "EMClient login code: " + code + "  msg: " + msg);
                finish();
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    //获取验证码
    private void getVerifyCode() {
        String account = edtPhone.getText().toString().trim();//用户名
        if (StringUtils.isEmpty(account)) {
            toast(getString(R.string.iphone_number_not_null));
            return;
        }
        if(!InputCheckUtil.checkPhoneNum(account)){
            toast(mContext.getResources().getString(R.string.enter_the_right_mobile));
            return;
        }
        BaseOkHttpClient.newBuilder()
                .addParam("mobile", account)
                .addParam("scene", "register")
                .url(NetUrlUtils.GET_CODE)
                .json().post()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "注册获取验证码----onSuccess:" + result);
                mVerifyCode = JSONUtils.jsonString2Bean(result, VerifyCodeBean.class);
                 LogUtils.e(TAG, "current thread  reg= "+Thread.currentThread().getName() );
                btnGetVerifyCode.startTimer();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "注册获取验证码----onError:" + msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "注册获取验证码----onFailure:" + e.getMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ScreenUtils.isShowKeyboard(mContext, edtPhone))
            ScreenUtils.closeKeybord(edtPhone, mContext);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }
}
