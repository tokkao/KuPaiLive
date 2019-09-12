package com.benben.kupaizhibo.wxapi;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.benben.kupaizhibo.config.Constants;
import com.benben.kupaizhibo.utils.PayListenerUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private String TAG = WXPayEntryActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);//微信的appkey需更换成自己的
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("TAG", "onPayFinish, errCode = " + baseResp.errCode);

        switch (baseResp.errCode) {
            case 0:
                //成功
                PayListenerUtils.getInstance(this).addSuccess();
                break;
            case -1:
                //失败
                PayListenerUtils.getInstance(this).addError();
                break;
            case -2:
                //用户取消
                PayListenerUtils.getInstance(this).addCancel();
                break;
        }
        finish();
    }
}
