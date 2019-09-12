package com.benben.kupaizhibo.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.DeviceUtils;
import com.benben.commoncore.utils.InputCheckUtil;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.RxBus;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.UserLoginSuccessBean;
import com.benben.kupaizhibo.bean.VerifyCodeBean;
import com.benben.kupaizhibo.config.Constants;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.utils.LoginCheckUtils;
import com.benben.kupaizhibo.utils.PlatformUtils;
import com.benben.kupaizhibo.widget.VerifyCodeButton;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.benben.kupaizhibo.config.Constants.RXBUS_KEY_REFRESH_MINEFRAGMENT;

/**
 * Created by: wanghk 2019-07-01.
 * Describe: 登录
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.edt_login_account)
    EditText edtLoginAccount;
    @BindView(R.id.edt_login_pwd)
    EditText edtLoginPwd;
    @BindView(R.id.llyt_login_pwd)
    LinearLayout llytLoginPwd;
    @BindView(R.id.edt_login_v_code)
    EditText edtLoginVCode;
    @BindView(R.id.btn_login_get_verify_code)
    VerifyCodeButton btnLoginGetVerifyCode;
    @BindView(R.id.llyt_login_verify_code)
    LinearLayout llytLoginVerifyCode;
    @BindView(R.id.tv_login_way_switch)
    TextView tvLoginWaySwitch;

    //密码登录
    private final int KEY_LOGIN_WAY_PWD = 1;
    //验证码登录
    private final int KEY_LOGIN_WAY_VERIFY_CODE = 2;

    //默认验证码登录
    private int mLoginWay = KEY_LOGIN_WAY_VERIFY_CODE;

    //获取验证返回结果
    private VerifyCodeBean mVerifyCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        //更新布局
        refreshUI();
    }


    //更新布局
    private void refreshUI() {
        if (mLoginWay == KEY_LOGIN_WAY_PWD) {
            llytLoginPwd.setVisibility(View.VISIBLE);
            llytLoginVerifyCode.setVisibility(View.GONE);
            tvLoginWaySwitch.setText(getString(R.string.verification_code_login));
            edtLoginPwd.clearFocus();
        } else {
            llytLoginPwd.setVisibility(View.GONE);
            llytLoginVerifyCode.setVisibility(View.VISIBLE);
            tvLoginWaySwitch.setText(getString(R.string.password_login));
            edtLoginPwd.clearFocus();
        }
    }

    @OnClick({R.id.iv_login_wechat, R.id.iv_login_qq, R.id.btn_login_get_verify_code, R.id.tv_login_way_switch, R.id.btn_login_confirm, R.id.tv_login_register, R.id.tv_login_forgot_pwd, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_login_wechat:
                if (!PlatformUtils.isWeixinAvilible(mContext)) {
                    toast("您的手机上没有安装微信!");
                    return;
                }
                XXPermissions.with(mContext)
                        .permission(Manifest.permission.READ_PHONE_STATE)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                loginByThirdPlatform(SHARE_MEDIA.WEIXIN);
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                toast(getString(R.string.permission_authorize));
                            }
                        });
                break;
            case R.id.iv_login_qq:
                if (!PlatformUtils.isQQClientAvailable(mContext)) {
                    toast("您的手机上没有安装QQ!");
                    return;
                }
                XXPermissions.with(mContext)
                        .permission(Manifest.permission.READ_PHONE_STATE)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                loginByThirdPlatform(SHARE_MEDIA.QQ);
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                toast(getString(R.string.permission_authorize));
                            }
                        });
                break;
            case R.id.btn_login_get_verify_code:
                getVerifyCode();
                break;
            case R.id.tv_login_way_switch:
                if (mLoginWay == KEY_LOGIN_WAY_PWD) {
                    mLoginWay = KEY_LOGIN_WAY_VERIFY_CODE;
                    refreshUI();
                } else {
                    mLoginWay = KEY_LOGIN_WAY_PWD;
                    refreshUI();
                }
                break;
            case R.id.btn_login_confirm:
                doCheckPermission();
                break;
            case R.id.tv_login_register:
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.tv_login_forgot_pwd:
                startActivity(new Intent(mContext, ForgotPasswordActivity.class));
                break;

        }
    }

    //三方登录
    private void loginByThirdPlatform(SHARE_MEDIA platform) {
        UMAuthListener authListener = new UMAuthListener() {
            /**
             * @desc 授权开始的回调
             * @param platform 平台名称
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {
                 LogUtils.e(TAG, "授权开始" );
            }

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param data 用户资料返回
             */
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                LogUtils.e(TAG, "授权完成" );
                int type = 0;
                if (platform == SHARE_MEDIA.QQ) {
                    type = BindMobileActivity.TYPE_QQ;
                } else if (platform == SHARE_MEDIA.WEIXIN) {
                    type = BindMobileActivity.TYPE_WEI_XIN;
                }
                 LogUtils.e(TAG, "data = "+data.toString() );
                checkThirdLogin(type,
                        data.get("uid"),
                        data.get("name"),
                        data.get("iconurl"),
                        "0".equals(data.get("gender")) ? 1 : 2);
            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                LogUtils.e(TAG, "授权错误" );
                LogUtils.e(TAG, t.getLocalizedMessage());
            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                LogUtils.e(TAG, "授权取消" );
                Toast.makeText(mContext, "已取消", Toast.LENGTH_LONG).show();
            }
        };
        UMShareAPI.get(mContext).getPlatformInfo(mContext, platform, authListener);
    }

    private void checkThirdLogin(int type, String uid, String name, String avatar, int sex) {
        String deviceId = DeviceUtils.getIMEI(mContext);
        if (TextUtils.isEmpty(deviceId)) {
            toast(getString(R.string.get_device_number_failed));
            return;
        }
        BaseOkHttpClient.newBuilder()
                .addParam("uuid", deviceId)
                .addParam("unionid", uid)
                .addParam("type", type)
                .addParam("nickname", name)
                .addParam("avatar", avatar)
                .addParam("sex", sex)
                .url(NetUrlUtils.THIRD_LOGIN)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "第三方登录----onSuccess:" + result);
                UserLoginSuccessBean data = JSONUtils.jsonString2Bean(result, UserLoginSuccessBean.class);
                if (data != null) {
                    KuPaiLiveApplication.mPreferenceProvider.setThirdLoginType(String.valueOf(type));
                    KuPaiLiveApplication.mPreferenceProvider.setOpenId(String.valueOf(uid));
                    //保存用户信息
                    LoginCheckUtils.saveLoginInfo(data, data.getMobile());
                    toast(getString(R.string.login_successful));
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
                LogUtils.e(TAG, "第三方登录----onError:" + msg);

                if (code == -2) {
                    BindMobileActivity.startWithData(mContext,
                            Constants.BIND_MOBILE_REQUEST_CODE,
                            type,
                            uid,
                            name,
                            avatar,
                            sex,0);
                }
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "第三方登录----onFailure:" + e.getMessage());
            }
        });
    }

    //检查权限
    private void doCheckPermission() {
        XXPermissions.with(mContext)
                .permission(Manifest.permission.READ_PHONE_STATE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        doLogin();
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        toast(getString(R.string.permission_authorize));
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.BIND_MOBILE_REQUEST_CODE
                && resultCode == RESULT_OK) {
            finish();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //去登录
    private void doLogin() {
        if (mLoginWay == KEY_LOGIN_WAY_PWD) {
            //密码登录
            doLoginByPwd();
        } else {
            //验证码登录
            doLoginByVerifyCode();
        }
    }

    //验证码登录
    private void doLoginByVerifyCode() {
        String account = edtLoginAccount.getText().toString().trim();
        String code = edtLoginVCode.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            toast(getString(R.string.enter_the_phone_number));
            return;
        }
        if(!InputCheckUtil.checkPhoneNum(account)){
            toast(mContext.getResources().getString(R.string.enter_the_right_mobile));
            return;
        }
        if (mVerifyCode == null || mVerifyCode.getCode_id() == null) {
            toast(getString(R.string.get_verification_code_first));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            toast(getString(R.string.enter_the_verification_code));
            return;
        }
        String deviceId = DeviceUtils.getIMEI(mContext);
        if (TextUtils.isEmpty(deviceId)) {
            toast(getString(R.string.get_device_number_failed));
            return;
        }
        BaseOkHttpClient.newBuilder()
                .addParam("mobile", account)
                .addParam("code", code)
                .addParam("scene", "login")
                .addParam("code_id", mVerifyCode.getCode_id())
                .addParam("uuid", deviceId)
                .url(NetUrlUtils.USER_MOBILE_LOGIN)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "验证码登录----onSuccess:" + result);
                UserLoginSuccessBean data = JSONUtils.jsonString2Bean(result, UserLoginSuccessBean.class);
                if (data != null) {
                    //保存用户信息
                    LoginCheckUtils.saveLoginInfo(data, account);
                    toast(getString(R.string.login_successful));
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
                LogUtils.e(TAG, "验证码登录----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "验证码登录----onFailure:" + e.getMessage());
            }
        });
    }

    //密码登录
    private void doLoginByPwd() {
        String account = edtLoginAccount.getText().toString().trim();
        String pwd = edtLoginPwd.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            toast(mContext.getResources().getString(R.string.enter_the_phone_number));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            toast(mContext.getResources().getString(R.string.enter_the_password));
            return;
        }
        if(!InputCheckUtil.checkPhoneNum(account)){
            toast(mContext.getResources().getString(R.string.enter_the_right_mobile));
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 12) {
            toast(mContext.getResources().getString(R.string.password_length));
            return;
        }



        String deviceId = DeviceUtils.getIMEI(this);
        if (TextUtils.isEmpty(deviceId)) {
            toast(mContext.getResources().getString(R.string.get_device_number_failed));
            return;
        }

        BaseOkHttpClient.newBuilder()
                .addParam("username", account)
                .addParam("password", pwd)
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
                    toast(getString(R.string.login_successful));
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

    //登录环信IM
    private void loginEmClient(UserLoginSuccessBean data) {
        if (StringUtils.isEmpty(data.getEasemob())) {
            //关闭登录页
            RxBus.getInstance().post(RXBUS_KEY_REFRESH_MINEFRAGMENT);
            finish();
            return;
        }
        //账号 ，密码，回调
        EMClient.getInstance().login("kpzb-" + data.getId(), data.getEasemob(), new EMCallBack() {
            @Override
            public void onSuccess() {
                 LogUtils.e(TAG, "环信登录成功 "+"kpzb-" + data.getId()+ "        "+data.getEasemob() );
                // 加载所有会话到内存
                EMClient.getInstance().chatManager().loadAllConversations();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RxBus.getInstance().post(RXBUS_KEY_REFRESH_MINEFRAGMENT);
                        //关闭登录页
                        finish();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "EMClient login code: " + code + "  msg: " + msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RxBus.getInstance().post(RXBUS_KEY_REFRESH_MINEFRAGMENT);
                        //关闭登录页
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

//    //登录环信客服
//    private void loginChatClient(UserLoginSucBean data, String account) {
//        ChatClient.getInstance().login("zrjm-" + data.getId(), data.getEasemob(), new Callback() {
//            @Override
//            public void onSuccess() {
//                // 加载所有会话到内存
//                EMClient.getInstance().chatManager().loadAllConversations();
//                //保存用户信息
//                LoginCheckUtils.saveLoginInfo(data, account);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        toast("登录成功！");
//                        //关闭登录页
//                        finish();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(int code, String msg) {
//                LogUtils.e(TAG, "EMClient login code: " + code + "  msg: " + msg);
//                toast(msg);
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });
//    }

    //获取短信验证码
    private void getVerifyCode() {
        String phone = edtLoginAccount.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            toast(getString(R.string.enter_the_phone_number));
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
                LogUtils.e(TAG, "登录获取验证码----onSuccess:" + result);
                toast(msg);
                mVerifyCode = JSONUtils.jsonString2Bean(result, VerifyCodeBean.class);
                LogUtils.e(TAG, "current thread = "+Thread.currentThread().getName() );

                btnLoginGetVerifyCode.startTimer();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "登录获取验证码----onError:" + msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "登录获取验证码----onFailure:" + e.getMessage());

            }
        });
    }



    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuilder retBuf = new StringBuilder();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5)
                        && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
                        .charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();

    }
}
