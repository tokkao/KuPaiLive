package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
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
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-11.
 * Describe: 修改背景
 */
public class ModifyBackGroundActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "ModifyBackGroundActivity";
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.btn_gallery)
    Button btnGallery;

    //权限参数
    InvokeParam invokeParam;
    //图片选择实例
    TakePhoto takePhoto;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    private String originalPath;
    private UploadPictureBean uploadPictureBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_background;
    }

    @Override
    protected void initData() {


        initTitle(getString(R.string.modify_background));
        //手机相册
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto.onPickFromGallery();
            }
        });

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getUserInfo();
    }

    private void getUserInfo() {
        BaseOkHttpClient.newBuilder()
                .addParam("uid", KuPaiLiveApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        UserInfoBean mUserInfo = JSONUtils.jsonString2Bean(json, UserInfoBean.class);
                        ImageUtils.getPic(mUserInfo.getBackground(), ivBackground, mContext, R.mipmap.img_user_center_top_bg);
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

    //上传背景图
    private void uploadUserBg() {
        File file = new File(originalPath);


        BaseOkHttpClient.newBuilder()
                //  .addParam("dir", "user_bg")
                // .addParam("module", "user")
                .addFile("file[]", file.getName(), file)
                .url(NetUrlUtils.PICTURE_UPLOAD)
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "上传背景图----onSuccess:" + json);

                List<UploadPictureBean> results = JSONUtils.jsonString2Beans(json, UploadPictureBean.class);
                if (results == null || results.size() == 0) {
                    toast("上传失败！");
                    return;
                }
                uploadPictureBean = results.get(0);
                //提交服务器
                submitUserBg();
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, "上传背景图----onError:" + msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "上传背景图----onFailure:" + e.getMessage());
            }
        });

    }

    //保存用户背景图片
    private void submitUserBg() {
        BaseOkHttpClient.newBuilder()
                .addParam("background", uploadPictureBean.getPath())
                .url(NetUrlUtils.USER_BACKGROUND)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "保存背景图----onSuccess:" + json);
                toast(msg);
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, "保存背景图----onError:" + msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "保存背景图----onFailure:" + e.getMessage());
            }
        });
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }


    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        LogUtils.e(TAG, "takeSuccess: getOriginalPath =" + image.getOriginalPath());
        originalPath = image.getOriginalPath();
        if (!StringUtils.isEmpty(originalPath)) {
            uploadUserBg();
        }
        Glide.with(mContext).load(new File(originalPath))
                .into(ivBackground);

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
}
