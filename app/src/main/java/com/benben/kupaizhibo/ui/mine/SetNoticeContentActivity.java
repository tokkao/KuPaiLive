package com.benben.kupaizhibo.ui.mine;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.hyphenate.helper.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 公告内容
 */
public class SetNoticeContentActivity extends BaseActivity {
    private static final String TAG = "SetNoticeContentActivity";
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.edt_notice)
    EditText edtNotice;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //0 发布公告  1 意见反馈
    private int type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_notice_content;
    }

    @Override
    protected void initData() {

        type = getIntent().getIntExtra("type", 0);
        initTitle(type == 0 ? getString(R.string.notice) : getString(R.string.complain_suggestion));
        edtNotice.setHint(type == 0 ? R.string.please_enter : R.string.complain_suggestion);
        tvTitle.setText(type == 0 ? R.string.notice_content : R.string.suggestion_description);
        btnSave.setText(type == 0 ? R.string.save : R.string.confirm);
        edtNotice.requestFocus();
        edtNotice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvLength.setText(charSequence.length() + "/250");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getNoticeContent();
    }

    //获取公告详情
    private void getNoticeContent() {
        if(type!=0)return;

        BaseOkHttpClient.newBuilder()
                .addParam("uid", KuPaiLiveApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        LogUtils.e(TAG, "设置公告-获取用户信息----onSuccess:" + json);
                        UserInfoBean mUserInfo = JSONUtils.jsonString2Bean(json, UserInfoBean.class);
                        edtNotice.setText(mUserInfo.getNotice());
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, "设置公告-获取用户信息----onError:" + msg);

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, "设置公告-获取用户信息----onFailure:" + e.getMessage());

                    }
                });
    }


    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                confirmInfo();
                break;
        }
    }

    private void confirmInfo() {
        if (StringUtils.isEmpty(edtNotice.getText().toString().trim())) {
            toast(mContext.getResources().getString(R.string.enter_the_content));
            return;
        }
        if (type == 0) {//发布公告


            BaseOkHttpClient.newBuilder()
                    .addParam("notice", edtNotice.getText().toString())
                    .url(NetUrlUtils.SET_NOTICE_CONTENT)
                    .post().json()
                    .build().enqueue(mContext, new BaseCallBack<String>() {
                @Override
                public void onSuccess(String json, String msg) {
                    LogUtils.e(TAG, "发布公告----onSuccess:" + json);
                    toast(msg);
                    finish();
                }

                @Override
                public void onError(int code, String msg) {
                    LogUtils.e(TAG, "发布公告----onError:" + msg);
                    ToastUtils.show(mContext, msg);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.e(TAG, "发布公告----onFailure:" + e.getMessage());
                }
            });


        } else {//反馈建议

            String uid = KuPaiLiveApplication.mPreferenceProvider.getUId();

            BaseOkHttpClient.newBuilder()
                    .addParam("title", "")
                    .addParam("body", edtNotice.getText().toString())
                    .addParam("uid", StringUtils.isEmpty(uid) ? "" : uid)
                    .url(NetUrlUtils.SUGGESTIONS)
                    .json().post().build()
                    .enqueue(mContext, new BaseCallBack<String>() {
                        @Override
                        public void onSuccess(String result, String msg) {
                            toast("提交成功！");
                            finish();
                        }

                        @Override
                        public void onError(int code, String msg) {
                            LogUtils.e(TAG, msg);
                            toast(msg);
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtils.e(TAG, e.getLocalizedMessage());
                        }
                    });

        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenUtils.closeKeybord(edtNotice, mContext);
    }
}
