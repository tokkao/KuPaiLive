package com.benben.kupaizhibo.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
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
import com.benben.kupaizhibo.utils.LoginCheckUtils;
import com.benben.kupaizhibo.widget.VerifyCodeButton;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.helper.StatusBarUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Author:zhn
 * Time:2019/7/2 0002 10:29
 * <p>
 * 三方登录绑定手机号
 */
public class BindMobileActivity extends BaseActivity {

    private static final String TAG = "BindMobileActivity";

    private static final String EXTRA_KEY_TYPE = "EXTRA_KEY_TYPE";
    private static final String EXTRA_KEY_OPEN_ID = "EXTRA_KEY_OPEN_ID";
    private static final String EXTRA_KEY_NICKNAME = "EXTRA_KEY_NICKNAME";
    private static final String EXTRA_KEY_AVATAR = "EXTRA_KEY_AVATAR";
    private static final String EXTRA_KEY_SEX = "EXTRA_KEY_SEX";

    //微信登录
    public static final int TYPE_WEI_XIN = 1;
    //QQ登录
    public static final int TYPE_QQ = 2;
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
    @BindView(R.id.edt_new_phone_number)
    EditText edtNewPhoneNumber;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_input_title)
    TextView tvInputTitle;
    private int enterType;  //0 三方登录后 绑定手机号 1 修改手机号

    /**
     * 带参数启动
     *
     * @param context
     */
    public static void startWithData(Activity context, int req_code, int type, String openid,
                                     String nickname, String avatar, int sex, int enter_type) {
        Intent starter = new Intent(context, BindMobileActivity.class);
        starter.putExtra(EXTRA_KEY_TYPE, type);
        starter.putExtra(EXTRA_KEY_OPEN_ID, openid);
        starter.putExtra(EXTRA_KEY_NICKNAME, nickname);
        starter.putExtra(EXTRA_KEY_AVATAR, avatar);
        starter.putExtra(EXTRA_KEY_SEX, sex);
        starter.putExtra("enter_type", enter_type);
        context.startActivityForResult(starter, req_code);
    }


    //获取验证返回结果
    private VerifyCodeBean mVerifyCode;

    //参数
    private int mType;
    private String mOpenId;
    private String mNickName;
    private String mAvatar;
    private int mSex;

    @Override
    protected void initData() {
        enterType = getIntent().getIntExtra("enter_type", 0);
        initTitle(enterType == 0 ? getString(R.string.bind_phone_number) : getString(R.string.update_phone_number));
        tvInputTitle.setText(enterType == 0 ? getString(R.string.password) : getString(R.string.new_phone_number));
        edtNewPhoneNumber.setHint(enterType == 0 ? getString(R.string.enter_the_password) : getString(R.string.enter_the_new_phone_number));
        edtNewPhoneNumber.setInputType(enterType == 0 ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_PHONE);
        mType = getIntent().getIntExtra(EXTRA_KEY_TYPE, 0);
        mOpenId = getIntent().getStringExtra(EXTRA_KEY_OPEN_ID);
        mNickName = getIntent().getStringExtra(EXTRA_KEY_NICKNAME);
        mAvatar = getIntent().getStringExtra(EXTRA_KEY_AVATAR);
        mSex = getIntent().getIntExtra(EXTRA_KEY_SEX, 0);


        //edtNewPhoneNumber.requestFocus();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_mobile;
    }


    @OnClick({R.id.rl_back, R.id.btn_get_verify_code, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                onBackPressed();
                break;
            case R.id.btn_get_verify_code:
                getVerifyCode();
                break;
            case R.id.btn_confirm:
                XXPermissions.with(mContext)
                        .permission(Manifest.permission.READ_PHONE_STATE)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                doBindMobile();
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                toast(getString(R.string.permission_authorize));
                            }
                        });
                break;
        }
    }

    //提交绑定手机号
    private void doBindMobile() {
        String phone = edtPhone.getText().toString().trim();
        String code = edtVerifyCode.getText().toString().trim();
        String newPhoneNumber = edtNewPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            toast(getString(R.string.enter_the_phone_number));
            return;
        }
        if (!InputCheckUtil.checkPhoneNum(phone)) {
            toast(mContext.getResources().getString(R.string.enter_the_right_mobile));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            toast(getString(R.string.enter_the_verification_code));
            return;
        }
        if (edtNewPhoneNumber.getText().toString().contains(" ")) {
            toast(enterType == 0 ? getString(R.string.pwd_cant_include_blank) : getString(R.string.mobile_cant_include_blank));
            return;
        }
        if (TextUtils.isEmpty(newPhoneNumber)) {
            toast(enterType == 0 ? getString(R.string.enter_the_password) : getString(R.string.enter_the_new_phone_number));
            return;
        }
        if (enterType == 0) {
            if (newPhoneNumber.length() < 6 || newPhoneNumber.length() > 12) {
                toast(getString(R.string.password_length));
                return;
            }
        }
        if (mVerifyCode == null) {
            toast(getString(R.string.get_verification_code_first));
            return;
        }
        String deviceId = DeviceUtils.getIMEI(mContext);
        if (TextUtils.isEmpty(deviceId)) {
            toast(getString(R.string.get_device_number_failed));
            return;
        }
        BaseOkHttpClient.Builder builder = BaseOkHttpClient.newBuilder();
        if (enterType == 0) { //绑定手机号
            builder.addParam("type", mType)  //1 微信 2 QQ 3 微博
                    .addParam("unionid", mOpenId)  //微信 openid  QQ openid
                    .addParam("nickname", mNickName)//昵称
                    .addParam("avatar", mAvatar)//头像
                    .addParam("sex", mSex)//性别
                    .addParam("uuid", deviceId)//imei号
                    .addParam("scene", "binding")//场景
                    .addParam("mobile", phone)//手机号
                    .addParam("password", newPhoneNumber)//密码
                    .addParam("code", code)//验证码
                    .addParam("code_id", mVerifyCode.getCode_id());//验证码id
        } else {//修改手机号
            builder
                    .addParam("scene", "0")
                    .addParam("mobile", phone)
                    .addParam("new_mobile", newPhoneNumber)
                    .addParam("code", code)
                    .addParam("code_id", mVerifyCode.getCode_id());
        }

        builder.url(enterType == 0 ? NetUrlUtils.BINDING_MOBILE : NetUrlUtils.MODIFY_MOBILE)
                .json().post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "绑定/修改手机 ----onSuccess:" + result);
                UserLoginSuccessBean data = JSONUtils.jsonString2Bean(result, UserLoginSuccessBean.class);
                if (data != null) {
                    //保存用户信息
                    LoginCheckUtils.saveLoginInfo(data, phone);
                    toast(getString(R.string.bind_successful));
                    setResult(RESULT_OK);
                    //登录环信客服
                    //loginChatClient(data, account);
                    //登录环信IM
                    loginEmClient(data);
                } else {
                    toast(getString(R.string.bind_failed));
                }
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, "绑定/修改手机 ----onError:" + msg);

            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "绑定/修改手机 ----onFailure:" + e.getMessage());

            }
        });
    }

    //登录环信IM
    private void loginEmClient(UserLoginSuccessBean data) {
        if (StringUtils.isEmpty(data.getEasemob())) {
            //关闭登录页
            finish();
            return;
        }
        EMClient.getInstance().login("kpzb" + data.getId(), data.getEasemob(), new EMCallBack() {
            @Override
            public void onSuccess() {
                // 加载所有会话到内存
                EMClient.getInstance().chatManager().loadAllConversations();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此处应跳转主页
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

    //获取验证码
    private void getVerifyCode() {
        String phone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            toast(getString(R.string.enter_the_phone_number));
            return;
        }
        if (!InputCheckUtil.checkPhoneNum(phone)) {
            toast(mContext.getResources().getString(R.string.enter_the_right_mobile));
            return;
        }

        BaseOkHttpClient.newBuilder()
                .addParam("mobile", phone)
                .addParam("scene", enterType == 0 ? "binding" : "0")
                .url(NetUrlUtils.GET_CODE)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                toast(msg);
                mVerifyCode = JSONUtils.jsonString2Bean(result, VerifyCodeBean.class);
                btnGetVerifyCode.startTimer();
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getMessage());
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
