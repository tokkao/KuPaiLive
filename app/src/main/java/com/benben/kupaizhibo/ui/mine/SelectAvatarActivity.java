package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.MainActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.UploadPictureBean;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.bumptech.glide.Glide;
import com.hyphenate.helper.StatusBarUtils;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 选择头像
 */
public class SelectAvatarActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "SelectAvatarActivity";
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.tv_take_photo)
    TextView tvTakePhoto;
    @BindView(R.id.tv_select_photo)
    TextView tvSelectPhoto;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    //权限参数
    InvokeParam invokeParam;
    //图片选择实例
    TakePhoto takePhoto;
    //图片属性配置
    private CropOptions mCropOptions;
    //图片路径
    private Uri mImageUri;
    //头像
    private String avatarPath;
    //生日
    private String birthday;
    // 0 男 1女
    private int sex;
    private UploadPictureBean mUploadUserAvatar;
    //用户名
    private String nickname;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_avatar;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.select_avatar));
        rightTitle.setText(getString(R.string.confirm));
        birthday = getIntent().getStringExtra("birthday");
        sex = getIntent().getIntExtra("sex", 0);
        mCropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
        //mImageUri = Uri.fromFile(new File(SystemDir.DIR_IMAGE));
        getUserInfo();
    }


    @OnClick({R.id.right_title, R.id.tv_take_photo, R.id.tv_select_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_title:
                confirmInfo();
                break;
            case R.id.tv_take_photo://拍照
                takePhoto();
                break;
            case R.id.tv_select_photo://选择照片
                selectPhoto();
                break;
        }
    }

    // 进入相册选择头像
    private void selectPhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        mImageUri = Uri.fromFile(file);
        takePhoto.onPickFromGalleryWithCrop(mImageUri, mCropOptions);
    }

    //拍照
    private void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        mImageUri = Uri.fromFile(file);
        takePhoto.onPickFromCaptureWithCrop(mImageUri, mCropOptions);
    }


    //提交信息
    private void confirmInfo() {

        if (StringUtils.isEmpty(avatarPath)) {
            toast(mContext.getResources().getString(R.string.please_select_avatar));
            return;
        }
        saveUserInfo();
    }

    //保存用户资料
    private void saveUserInfo() {
        //第一步 检查上传头像
        if (avatarPath != null) {
            uploadUserAvatar();
        } else {
            submitUserInfo();
        }
    }

    //获取用户信息 （主要信息为用户名）
    private void getUserInfo() {
        BaseOkHttpClient.newBuilder()
                .addParam("uid", KuPaiLiveApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        UserInfoBean mUserInfo = JSONUtils.jsonString2Bean(json, UserInfoBean.class);
                        nickname = mUserInfo.getNickname();
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

    //上传头像
    private void uploadUserAvatar() {
        File file = new File(avatarPath);
        BaseOkHttpClient.newBuilder()
                //.addParam("dir", "user_avatar")
              //  .addParam("module", "user")
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

        // String autograph = edtUserAutograph.getText().toString().trim();

        BaseOkHttpClient.newBuilder()
                .addParam("avatar", mUploadUserAvatar == null ? "" : mUploadUserAvatar.getPath())
                .addParam("sex", sex)
                .addParam("birthday", birthday)
                .addParam("nickname", nickname)
                .url(NetUrlUtils.EDIT_USER_INFO)
                .json().post()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                toast(msg);
                startActivity(new Intent(mContext, MainActivity.class));
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


    @Override
    public void takeSuccess(TResult result) {
        avatarPath = result.getImage().getOriginalPath();
        LogUtils.e(TAG, "takeSuccess: getOriginalPath =" + avatarPath);
        Glide.with(mContext).load(new File(avatarPath))
                .into(ivAvatar);
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
}
