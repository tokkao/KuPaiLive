package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.InvitationQRCodeBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.widget.CommonSharePopupWindow;
import com.hyphenate.helper.StatusBarUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 邀请
 */
public class MyInvitationActivity extends BaseActivity {

    private static final String TAG = "MyInvitationActivity";
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_invitation_code)
    TextView tvInvitationCode;
    @BindView(R.id.rlyt_content)
    RelativeLayout rlytContent;
    private int type; // 1 微信 2 朋友圈 3 QQ 4 QQ空间 5 微博

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_invitation;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.invite));
        type = getIntent().getIntExtra("type", 0);
        rightTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_my_sharing), null, null, null);

        tvName.setText(KuPaiLiveApplication.mPreferenceProvider.getUserName());
        ImageUtils.getPic(KuPaiLiveApplication.mPreferenceProvider.getPhoto(), ivAvatar, mContext, R.mipmap.icon_default_avatar);

        getMyQRCode();
    }

    private void getMyQRCode() {

        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.USER_QR_CODE)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "用户邀请二维码----onSuccess:" + json);
                InvitationQRCodeBean invitationQRCodeBean = JSONUtils.jsonString2Bean(json, InvitationQRCodeBean.class);
                ImageUtils.getPic(invitationQRCodeBean.getInvite_code_url(),ivQrCode,mContext,R.mipmap.banner_default);
                tvInvitationCode.setText(invitationQRCodeBean.getInvite_code());

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "用户邀请二维码----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "用户邀请二维码----onFailure:" + e.getMessage());
            }
        });


    }

    private void shareToPlatform(int type) {
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        switch (type) {
            case 1:
                platform = SHARE_MEDIA.WEIXIN;
                break;
            case 2:
                platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case 3:
                platform = SHARE_MEDIA.QQ;
                break;
            case 4:
                platform = SHARE_MEDIA.QZONE;
                break;
            case 5:
                platform = SHARE_MEDIA.SINA;
                break;
        }
        new ShareAction(mContext)
                .setPlatform(platform)//传入平台
                .withText(getString(R.string.app_name))
                .withMedia(new UMImage(mContext, loadBitmapFromView(rlytContent)))
                .setCallback(new UMShareListener() {//回调监听器
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        LogUtils.e(TAG, "share_start = "+share_media.getName() );

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        // 1=>微信好友 2=>微信朋友圈 3=>QQ好友 4=>QQ空间 5=>微博
                         LogUtils.e(TAG, "share_media = "+share_media.getName() );
                        if ("sina".equals(share_media.getName())) {
                            uploadSharingResult(5);
                        } else if ("wxsession".equals(share_media.getName())) {
                            uploadSharingResult(1);
                        } else if ("wxtimeline".equals(share_media.getName())) {
                            uploadSharingResult(2);
                        } else if ("qq".equals(share_media.getName())) {
                            uploadSharingResult(3);
                        } else {
                            uploadSharingResult(4);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        LogUtils.e(TAG, "share_error = "+throwable.getMessage() );

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        LogUtils.e(TAG, "share_cancel = "+share_media.getName() );

                    }
                }).share();
    }


    //上传分享结果
    private void uploadSharingResult(int type) {
        // 1=>微信好友 2=>微信朋友圈 3=>QQ好友 4=>QQ空间 5=>微博
        BaseOkHttpClient.newBuilder()
                .addParam("type", type)
                .url(NetUrlUtils.GO_SHARE)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "分享----onSuccess:" + json);
                toast(msg);
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "分享----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "分享----onFailure:" + e.getMessage());
            }
        });


    }


    /*打开分享弹窗*/
    private void openSelectSharePop() {

        CommonSharePopupWindow mCommonSharePopupWindow = new CommonSharePopupWindow(mContext);
        mCommonSharePopupWindow.setOnButtonClickListener(this::shareToPlatform);
        mCommonSharePopupWindow.showWindow(getWindow().getDecorView().getRootView());
    }

    @OnClick({R.id.right_title, R.id.llyt_invitation_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_title://分享
                openSelectSharePop();
                break;
            case R.id.llyt_invitation_code://复制邀请码
                //ivQrCode.setImageBitmap(loadBitmapFromView(rlytContent));
//                CrashReport.testJavaCrash();
                break;
        }
    }


    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX(), -v.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        v.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
