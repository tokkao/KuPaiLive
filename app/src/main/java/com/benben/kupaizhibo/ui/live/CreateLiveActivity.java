package com.benben.kupaizhibo.ui.live;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AutoSpinnerAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.LiveRoomBean;
import com.benben.kupaizhibo.bean.LiveTopicTagBean;
import com.benben.kupaizhibo.bean.UploadPictureBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.utils.LocationUtils;
import com.bumptech.glide.Glide;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hyphenate.helper.StatusBarUtils;
import com.suke.widget.SwitchButton;

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
 * Created by: wanghk 2019-07-02.
 * Describe: 开启直播页面
 */
public class CreateLiveActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "CreateLiveActivity_log";

    private static final String EXTRA_KEY_ACTIVE_ID = "EXTRA_KEY_ACTIVE_ID";
    @BindView(R.id.sb_pub_location)
    SwitchButton sbPubLocation;
    @BindView(R.id.btn_create_live)
    Button btnCreateLive;
    private String imgPath;
    private String mCity;


    /**
     * 活动详情页参与活动时使用
     *
     * @param context
     * @param active_id
     */
    public static void startWithActiveId(Context context, int active_id) {
        Intent starter = new Intent(context, CreateLiveActivity.class);
        starter.putExtra(EXTRA_KEY_ACTIVE_ID, active_id);
        context.startActivity(starter);
    }

    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.edt_biaoti)
    EditText edtBiaoti;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;

    @BindView(R.id.auto_tv_biaoqian)
    AutoCompleteTextView autoTvTime;
    @BindView(R.id.auto_tv_huati)
    AutoCompleteTextView autoTvCate;
    @BindView(R.id.iv_fengmian)
    ImageView ivFengmian;

    //活动id，默认-1时表示没有活动
    private int mActiveId = -1;
    private String topicId = "0";
    private String tagId = "";

    //权限参数
    InvokeParam invokeParam;
    //图片选择实例
    TakePhoto takePhoto;
    //图片属性配置
//    private CropOptions mCropOptions;
    //图片路径
    private Uri mImageUri;

    //直播封面
//    private LocalMedia mLiveFengmianMedia;

    //上传封面结果信息
    private UploadPictureBean mUploadFengmianResult;

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_live;
    }

    @Override
    protected void initData() {
        centerTitle.setText(getString(R.string.live_create_live));
        //mCropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();

        //获取活动id
        mActiveId = getIntent().getIntExtra(EXTRA_KEY_ACTIVE_ID, -1);

        //获取分类
        getTopicTagList();

        getLocation();
        initAutoTopic();
    }

    //获取地理位置
    private void getLocation() {
        XXPermissions.with(mContext)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {

                        LocationUtils.registerLocation(mContext, new LocationUtils.OnLocationResultListener() {
                            @Override
                            public void cannotLocation(String msg) {
                                if (!StringUtils.isEmpty(msg)) {
                                    toast(msg);
                                }
                            }

                            @Override
                            public void getLastKnownLocation(Location location) {
                                String city = LocationUtils.getLocality(mContext, location.getLatitude(), location.getLongitude());
                                LogUtils.e(TAG, city);
                            }

                            @Override
                            public void onLocationChanged(Location location) {
                                mCity = LocationUtils.getLocality(mContext, location.getLatitude(), location.getLongitude());
                                LogUtils.e(TAG, "当前城市：" + mCity);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }
                        });
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }


    private void getTopicTagList() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.LIVES_GET_TOPIC_TAGS)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "获取开启直播分类----onSuccess:" + result);
                if (!StringUtils.isEmpty(result)) {
                    String topicData = JSONUtils.getNoteJson(result, "topic");
                    String tagsData = JSONUtils.getNoteJson(result, "tags");
                    //  List<LiveTopicTagBean> mLiveTopicBean = JSONUtils.jsonString2Beans(topicData, LiveTopicTagBean.class);
                    List<LiveTopicTagBean> mLiveTagBean = JSONUtils.jsonString2Beans(tagsData, LiveTopicTagBean.class);

                    initAutoTag(mLiveTagBean);
                }
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取开启直播分类----onError:" + msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取开启直播分类----onFailure:" + e.getMessage());
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });
    }

    private void initAutoTag(List<LiveTopicTagBean> liveTagBean) {
        if (liveTagBean == null || liveTagBean.isEmpty()) {
            ToastUtils.show(mContext, "获取标签失败");
            return;
        }
        AutoSpinnerAdapter<LiveTopicTagBean> adapter = new AutoSpinnerAdapter(mContext);
        autoTvCate.setAdapter(adapter);
        adapter.appendToList(liveTagBean);

        autoTvCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoTvCate.setText(liveTagBean.get(position).getFilterString());
                tagId = liveTagBean.get(position).getId();
            }
        });
    }


    private void initAutoTopic() {
        ArrayList<LiveTopicTagBean> liveTopicTagBeans = new ArrayList<>();

        liveTopicTagBeans.add(new LiveTopicTagBean("0", mContext.getResources().getString(R.string.live_start_now), false));
        liveTopicTagBeans.add(new LiveTopicTagBean("1", mContext.getResources().getString(R.string.live_three_min_later), false));
        liveTopicTagBeans.add(new LiveTopicTagBean("2", mContext.getResources().getString(R.string.live_five_min_later), false));
        liveTopicTagBeans.add(new LiveTopicTagBean("3", mContext.getResources().getString(R.string.live_ten_min_later), false));

        AutoSpinnerAdapter<LiveTopicTagBean> adapter = new AutoSpinnerAdapter(mContext);
        autoTvTime.setAdapter(adapter);
        adapter.appendToList(liveTopicTagBeans);

        autoTvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoTvTime.setText(liveTopicTagBeans.get(position).getFilterString());
                topicId = liveTopicTagBeans.get(position).getId();
            }
        });
    }

    @OnClick({R.id.rl_back, R.id.auto_tv_biaoqian, R.id.auto_tv_huati, R.id.btn_create_live, R.id.iv_fengmian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                onBackPressed();
                break;
            case R.id.auto_tv_biaoqian:
                clearFocus();
                autoTvTime.showDropDown();
                break;
            case R.id.auto_tv_huati:
                clearFocus();
                autoTvCate.showDropDown();
                break;
            case R.id.btn_create_live:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    createLiveRoom();
                } else {
                    XXPermissions.with(mContext)
                            .permission(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION})
                            .request(new OnPermission() {
                                @Override
                                public void hasPermission(List<String> granted, boolean isAll) {
                                    createLiveRoom();
                                }

                                @Override
                                public void noPermission(List<String> denied, boolean quick) {

                                }
                            });

                }
                break;
            case R.id.iv_fengmian:
                selectLiveFengmian();
                break;
        }
    }

    private void selectLiveFengmian() {
//        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
//        if (!file.getParentFile().exists()) {
//            file.getParentFile().mkdirs();
//        }
//        mImageUri = Uri.fromFile(file);
        takePhoto.onPickFromGallery();
//        takePhoto.onPickFromGalleryWithCrop(mImageUri, mCropOptions);
    }

    /**
     * 功能:创建直播
     */
    private void createLiveRoom() {
        String title = edtBiaoti.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();
        if (StringUtils.isEmpty(title)) {
            toast(getString(R.string.live_pls_enter_title));
            return;
        }

        if (StringUtils.isEmpty(topicId)) {
            toast(getString(R.string.live_pls_select_time));
            return;
        }

        if (StringUtils.isEmpty(tagId)) {
            toast(getString(R.string.live_pls_select_cate));
            return;
        }
        if (!StringUtils.isEmpty(pwd) && pwd.length() != 6) {
            toast("请输入6位房间密码");
            return;
        }

        if (StringUtils.isEmpty(imgPath)) {
            toast(getString(R.string.live_pls_select_cover));
            return;
        }

        if (mUploadFengmianResult == null || mUploadFengmianResult.getId() == 0) {
            uploadLiveFengmian();
        } else {
            submitOtherInfo();
        }

    }

    //第一步上传封面照片
    private void uploadLiveFengmian() {
        File file = new File(imgPath);

        BaseOkHttpClient.newBuilder()
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
                mUploadFengmianResult = results.get(0);
                submitOtherInfo();
            }

            @Override
            public void onError(int code, String msg) {
                toast("onError: " + msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }

    private void submitOtherInfo() {

        String title = edtBiaoti.getText().toString().trim();
        int userType = KuPaiLiveApplication.mPreferenceProvider.getUserType();
        BaseOkHttpClient.Builder build = BaseOkHttpClient.newBuilder()
                .addParam("live_type", userType)
                .addParam("title", title)
                .addParam("tags_id", tagId)
                .addParam("passwd", edtPwd.getText().toString().trim());
        // .addParam("topic_id", topicId);
        if (mActiveId != -1) {
            build.addParam("activity_id", mActiveId);
        }
        if (mUploadFengmianResult != null) {
            build.addParam("thumb", mUploadFengmianResult.getId());
        }
        if (!StringUtils.isEmpty(mCity) && sbPubLocation.isChecked()) {
            build.addParam("city", mCity);
        }
        build.url(NetUrlUtils.LIVES_CREATE_ROOM)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG, "开启直播----onSuccess:" + result);
                LiveRoomBean mLiveRoomBean = JSONUtils.jsonString2Bean(result, LiveRoomBean.class);
                Intent intent = new Intent(mContext, OpenLiveActivity.class);
                intent.putExtra("liveInfo", mLiveRoomBean);
                intent.putExtra("time_later", topicId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "开启直播----onError:" + msg);

                ToastUtils.show(mContext, "" + msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "开启直播----onFailure:" + e.getMessage());

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (ScreenUtils.isShowKeyboard(mContext, edtBiaoti))
            ScreenUtils.closeKeybord(edtBiaoti, mContext);
    }


    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        LogUtils.e(TAG, "takeSuccess: getOriginalPath =" + image.getOriginalPath());
        imgPath = image.getOriginalPath();
        Glide.with(mContext).load(new File(image.getOriginalPath()))
                .into(ivFengmian);
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


}
