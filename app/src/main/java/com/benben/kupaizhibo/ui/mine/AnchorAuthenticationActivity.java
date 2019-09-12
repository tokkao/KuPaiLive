package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.benben.commoncore.utils.InputCheckUtil;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.GridImageListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.AuthImageBean;
import com.benben.kupaizhibo.bean.AuthStatusBean;
import com.benben.kupaizhibo.bean.UploadPictureBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.utils.FullyGridLayoutManager;
import com.benben.kupaizhibo.widget.CustomRecyclerView;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 主播认证
 */
public class AnchorAuthenticationActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "AnchorAuthenticationActivity";
    @BindView(R.id.auto_tv_identity_type)
    AutoCompleteTextView tvIdentityType;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_id_number)
    EditText edtIdNumber;
    @BindView(R.id.rlv_image)
    CustomRecyclerView rlvImage;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_authentication_description)
    TextView tvAuthenticationDescription;

    private boolean isShow = true;
    private boolean isPreviewVideo = true;
    private boolean isCompress = true;
    private boolean isCheckNumMode = false;
    private GridImageListAdapter mAdapter;
    private File[] mFiles;//需要上传的文件
    //权限参数
    InvokeParam invokeParam;
    //图片选择实例
    TakePhoto takePhoto;
    private StringBuilder imageArray = new StringBuilder();//上传到服务器图片地址
    private List<AuthImageBean> imageArrayList = new ArrayList<>();
    private int limit = 9;
    private AuthStatusBean authStatusBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_anchor_authentication;
    }

    @Override
    protected void initData() {
        authStatusBean = (AuthStatusBean) getIntent().getSerializableExtra("auth_info");
        if (authStatusBean != null) {
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
        } else {
            initTitle(getString(R.string.anchor_authentication));

        }
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        rlvImage.setLayoutManager(fullyGridLayoutManager);
        rlvImage.canScrollVertically(0);
        mAdapter = new GridImageListAdapter(this, onAddPicClickListener);
        rlvImage.setAdapter(mAdapter);

        refreshData();
    }

    private void refreshData() {
        if (authStatusBean != null) {
            edtName.setText(authStatusBean.getName());
            edtIdNumber.setText(authStatusBean.getSfz());
            if(authStatusBean.getImg() == null)return;
            for (int i = 0; i < authStatusBean.getImg().size(); i++) {
                AuthImageBean authImageBean = new AuthImageBean();
                authImageBean.setImg_id(authStatusBean.getImg().get(i).getId());
                authImageBean.setPath(authStatusBean.getImg().get(i).getPath());
                imageArrayList.add(authImageBean);
            }
            limit = 9 - imageArrayList.size();
            mAdapter.setList(imageArrayList, true, authStatusBean.getType());
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 提交信息
     */
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
        //图片 不能为空
        if (imageArrayList.size() > 0) {
            uploadPhoto();
        } else {
            Toast.makeText(mContext, getResources().getString(R.string.please_select_image), Toast.LENGTH_SHORT).show();
        }


    }

    public void authentication() {
        if (authStatusBean != null && !StringUtils.isEmpty(authStatusBean.getFile_id())) {
            for (int i = 0; i < imageArrayList.size(); i++) {
                if(!StringUtils.isEmpty(imageArrayList.get(i).getImg_id())){
                    imageArray.append(",").append(imageArrayList.get(i).getImg_id());
                }
            }
        }
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.ANCHOR_AUTHENTICATION)
                .addParam("sfz_id", "" + edtIdNumber.getText().toString().trim())
                .addParam("name", "" + edtName.getText().toString().trim())
                .addParam("file", imageArray)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "主播认证----onSuccess:" + result);
                toast(msg);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "主播认证----onError:" + msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "主播认证----onFailure:" + e.getMessage());
            }
        });
    }

    /**
     * 上传图片
     */
    private void uploadPhoto() {
//        需要上传的list
        ArrayList<AuthImageBean> uploadImageList = new ArrayList<>();
        for (int i = 0; i < imageArrayList.size(); i++) {
            if (StringUtils.isEmpty(imageArrayList.get(i).getImg_id())) {
                uploadImageList.add(imageArrayList.get(i));
            }
        }
        mFiles = new File[uploadImageList.size()];
        BaseOkHttpClient.Builder builder = BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.PICTURE_UPLOAD);
        for (int i = 0; i < uploadImageList.size(); i++) {
            mFiles[i] = new File(uploadImageList.get(i).getPath());
            builder.addFile("file[]", mFiles[i].getName(), mFiles[i]);
        }
        builder.post()
                .build()
                .enqueue(this, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        LogUtils.e(TAG, "上传认证图片----onSuccess:" + result);
                        List<UploadPictureBean> results = JSONUtils.jsonString2Beans(result, UploadPictureBean.class);
                        if (results == null || results.size() == 0) {
                            toast("上传失败！");
                            return;
                        }

                        for (int i = 0; i < results.size(); i++) {
                            if (results.size() > 1) {
                                if (i == (results.size() - 1)) {
                                    imageArray.append(results.get(i).getId());
                                } else {
                                    imageArray.append(results.get(i).getId()).append(",");
                                }

                            } else {
                                imageArray.append(results.get(i).getId());
                            }


                        }
                        authentication();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, "上传认证图片----onError:" + msg);
                        toast(msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, "上传认证图片----onFailure:" + e.getMessage());
                        toast(getResources().getString(R.string.server_exception));
                    }
                });
    }

    /**
     * 删除图片回调接口
     */
    private GridImageListAdapter.onAddPicClickListener onAddPicClickListener = new GridImageListAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    if (imageArrayList.size() >= 9) {
                        toast(mContext.getResources().getString(R.string.most_choose_nine_photo));
                        return;
                    }
                    LogUtils.e(TAG, "onAddPicClick: limit=" + limit);
                    takePhoto.onPickMultiple(limit);
                    break;
                case 1:
                    // 删除图片
                    imageArrayList.remove(position);
                    limit = 9 - imageArrayList.size();
                    mAdapter.notifyItemRemoved(position);
                    break;
            }
        }
    };


    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> images = result.getImages();
        for (int i = 0; i < images.size(); i++) {
            AuthImageBean authImageBean = new AuthImageBean();
            authImageBean.setPath(images.get(i).getOriginalPath());
            imageArrayList.add(authImageBean);
        }
        limit = (9 - imageArrayList.size());
        mAdapter.setList(imageArrayList, false, -1);
        mAdapter.notifyDataSetChanged();
        LogUtils.e(TAG, "takeSuccess: imageArrayList =" + imageArrayList.size());
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

    @OnClick({R.id.btn_confirm, R.id.auto_tv_identity_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                confirmInfo();
                break;
            case R.id.auto_tv_identity_type: //身份类别
                clearFocus();
                tvIdentityType.showDropDown();
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (ScreenUtils.isShowKeyboard(mContext, edtIdNumber))
            ScreenUtils.closeKeybord(edtIdNumber, mContext);
    }

}
