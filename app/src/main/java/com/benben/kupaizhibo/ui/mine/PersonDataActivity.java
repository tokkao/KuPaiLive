package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.RxBus;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.AuthStatusBean;
import com.benben.kupaizhibo.bean.UploadPictureBean;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.config.Constants;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.utils.LoginCheckUtils;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bumptech.glide.Glide;
import com.hyphenate.helper.StatusBarUtils;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-12.
 * Describe: 个人资料
 */
public class PersonDataActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "PersonDataActivity";
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    @BindView(R.id.edt_user_nickname)
    EditText edtUserNickname;
    @BindView(R.id.edt_user_autograph)
    EditText edtUserAutograph;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R.id.llyt_sex)
    LinearLayout llytSex;
    @BindView(R.id.tv_user_birthday)
    TextView tvUserBirthday;
    @BindView(R.id.llyt_birthday)
    LinearLayout llytBirthday;
    @BindView(R.id.edt_user_hobby)
    EditText edtUserHobby;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    //头像路径
    private String avatarPath;
    //用户信息
    private UserInfoBean mUserInfo;
    //性别
    private int sex = 1;
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    //生日
    private String birthday;
    private UploadPictureBean mUploadUserAvatar;
    //权限参数
    InvokeParam invokeParam;
    //图片选择实例
    TakePhoto takePhoto;
    //图片属性配置
    private CropOptions mCropOptions;
    //图片路径
    private Uri mImageUri;
    private String avatar;

    //选择的图片
    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.personal_data));
        rightTitle.setText(R.string.save);
        mCropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();

        getUserInfo();
    }


    //获取用户详情
    private void getUserInfo() {
        LoginCheckUtils.checkLoginShowDialog(mContext);

        BaseOkHttpClient.newBuilder()
                .addParam("uid", KuPaiLiveApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        mUserInfo = JSONUtils.jsonString2Bean(json, UserInfoBean.class);
                        refreshUI();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private void refreshUI() {
        ImageUtils.getPic(mUserInfo.getAvatar(), civUserAvatar, mContext, R.mipmap.icon_default_avatar);
        edtUserNickname.setText(mUserInfo.getNickname());
        tvUserSex.setText(mUserInfo.getSex() == 1 ? mContext.getResources().getString(R.string.mine_man) : mContext.getResources().getString(R.string.mine_woman));
        sex = mUserInfo.getSex();
        Date mSelectBirthday = new Date(mUserInfo.getBirthday() * 1000);
        birthday = mSdf.format(mSelectBirthday);
        tvUserBirthday.setText(birthday);
        edtUserHobby.setText(mUserInfo.getHobby());
    }

    //保存用户资料
    private void saveUserInfo() {
        //第一步验证输入是否合法
        String nickName = edtUserNickname.getText().toString().trim();
        if (StringUtils.isEmpty(nickName)) {
            toast(mContext.getResources().getString(R.string.please_enter_the_nickname));
            return;
        }
        String hobby = edtUserHobby.getText().toString().trim();
        if (nickName.length() > 8) {
            toast(getString(R.string.nickname_length));
            return;
        }
        if (hobby.length() > 12) {
            toast(getString(R.string.hobby_length));
            return;
        }
        //第二步 检查上传头像
        if (avatarPath != null) {
            uploadUserAvatar();
        } else {
            submitUserInfo();
        }
    }

    //上传头像
    private void uploadUserAvatar() {
        File file = new File(avatarPath);
        BaseOkHttpClient.newBuilder()
                // .addParam("dir", "user_avatar")
                // .addParam("module", "user")
                .addFile("file[]", file.getName(), file)
                .url(NetUrlUtils.PICTURE_UPLOAD)
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                List<UploadPictureBean> results = JSONUtils.jsonString2Beans(json, UploadPictureBean.class);
                if (results == null || results.size() == 0) {
                    toast("上传失败！");
                    return;
                }
                mUploadUserAvatar = results.get(0);
                //第三步 提交用户信息
                submitUserInfo();
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });
    }

    //上传用户信息
    private void submitUserInfo() {
        avatar = mUserInfo.getAvatar();

        if (mUploadUserAvatar != null) {
            avatar = mUploadUserAvatar.getPath();
        }
        String nickname = edtUserNickname.getText().toString().trim();

        String hobby = edtUserHobby.getText().toString().trim();

        String birthday = tvUserBirthday.getText().toString().trim();

        // String autograph = edtUserAutograph.getText().toString().trim();

        BaseOkHttpClient.newBuilder()
                .addParam("avatar", avatar)
                .addParam("nickname", nickname)
                .addParam("sex", sex)
                .addParam("birthday", birthday)
                .addParam("hobby", hobby)
                .addParam("autograph", "")
                .url(NetUrlUtils.EDIT_USER_INFO)
                .json().post()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                toast(msg);
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setAvatar(avatar);
                LoginCheckUtils.updateUserInfo(userInfoBean);
                finish();
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });
    }


    @OnClick({R.id.right_title, R.id.rlyt_user_avatar, R.id.tv_user_sex, R.id.tv_user_birthday, R.id.llyt_phone_number, R.id.llyt_authentication, R.id.llyt_background, R.id.llyt_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_title://保存
                saveUserInfo();
                break;
            case R.id.tv_user_sex://性别
                ArrayList<String> sexList = new ArrayList<>();
                sexList.add(mContext.getResources().getString(R.string.mine_man));
                sexList.add(mContext.getResources().getString(R.string.mine_woman));
                BottomMenu.show(mContext, sexList, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        sex = 0 == index ? 1 : 2;
                        tvUserSex.setText(sex == 1 ? mContext.getResources().getString(R.string.mine_man) : mContext.getResources().getString(R.string.mine_woman));
                    }
                });
                break;
            case R.id.tv_user_birthday://生日
                selectBirthday();
                break;
            case R.id.rlyt_user_avatar://修改头像
                File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                mImageUri = Uri.fromFile(file);
                takePhoto.onPickFromGalleryWithCrop(mImageUri, mCropOptions);
                break;
            case R.id.llyt_phone_number://更换手机
                if(StringUtils.isEmpty(KuPaiLiveApplication.mPreferenceProvider.getMobile())){
                    //绑定手机号
                    BindMobileActivity.startWithData(mContext,0,Integer.parseInt(KuPaiLiveApplication.mPreferenceProvider.getThirdLoginType()),KuPaiLiveApplication.mPreferenceProvider.getOpenId(),mUserInfo.getNickname(),mUserInfo.getAvatar(),mUserInfo.getSex(),0);
                }else {
                    //修改手机号
                    Intent phoneIntent = new Intent(mContext, BindMobileActivity.class);
                    phoneIntent.putExtra("enter_type",1);
                    startActivity(phoneIntent);
                }

                break;
            case R.id.llyt_authentication://认证
                getRealNameAuthStatus();
                break;
            case R.id.llyt_background://修改背景
                Intent bgIntent = new Intent(mContext, ModifyBackGroundActivity.class);
                startActivity(bgIntent);
                break;
            case R.id.llyt_notice://公告
                Intent noticeIntent = new Intent(mContext, SetNoticeContentActivity.class);
                noticeIntent.putExtra("type", 0);
                startActivity(noticeIntent);
                break;
            case R.id.rl_back:
                finish();
                break;
        }
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
                startActivity(new Intent(mContext, RealNameAuthenticationActivity.class).putExtra("auth_info", lstAuthStatus));
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, "实名认证状态----onError:" + msg);
                startActivity(new Intent(mContext, RealNameAuthenticationActivity.class));
            }

            @Override
            public void onFailure(Call call, IOException e) {
                toast(e.getMessage());
                LogUtils.e(TAG, "实名认证状态----onFailure:" + e.getMessage());
            }


        });
    }

    //选择生日
    private void selectBirthday() {

        new TimePickerBuilder(mContext, (date, v) -> {
            if (date.getTime() <= new Date().getTime()) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                int year = instance.get(Calendar.YEAR);
                int month = instance.get(Calendar.MONTH) + 1;
                int day = instance.get(Calendar.DAY_OF_MONTH);
                birthday = year + "-" + month + "-" + day;
                tvUserBirthday.setText(birthday);

            } else {
                toast("选择的日期有误！");
            }

        }).setType(new boolean[]{true, true, true, false, false, false}).build().show();
    }


    @Override
    public void takeSuccess(TResult result) {
        result.getImage().getOriginalPath();
        avatarPath = result.getImage().getOriginalPath();
        LogUtils.e(TAG, "takeSuccess: getOriginalPath =" + avatarPath);
        Glide.with(mContext).load(new File(avatarPath))
                .into(civUserAvatar);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        toast(getString(R.string.select_image_fail));
    }

    @Override
    public void takeCancel() {
        toast(getString(R.string.select_image_cancel));
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        CompressConfig compressConfig = CompressConfig.ofDefaultConfig();
        takePhoto.onEnableCompress(compressConfig,true);

        return takePhoto;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().post(Constants.RXBUS_KEY_REFRESH_MINEFRAGMENT);
    }
}
