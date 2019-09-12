package com.benben.kupaizhibo.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.utils.LoginCheckUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Description:OkHttpMange管理类
 *
 * @author zjn
 * Email：168455992@qq.com
 * @date 2019/1/11
 */
public class OkHttpManager {
    private static OkHttpManager mInstance;
    private OkHttpClient mClient;
    private Handler mHandler;

    /**
     * 单例
     *
     * @return
     */
    public static synchronized OkHttpManager getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpManager();
        }
        return mInstance;
    }

    /**
     * 构造函数
     */
    private OkHttpManager() {
        initOkHttp();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 初始化OkHttpClient
     */
    private void initOkHttp() {
        mClient = new OkHttpClient().newBuilder()
                .readTimeout(30000, TimeUnit.SECONDS)
                .connectTimeout(30000, TimeUnit.SECONDS)
                .writeTimeout(30000, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 请求
     *
     * @param client
     * @param callBack
     */
    public void request(Activity activity, BaseOkHttpClient client, final BaseCallBack callBack) {
        if (callBack == null) {
            throw new NullPointerException(" callback is null");
        }
        StyledDialogUtils.getInstance().loading(activity);
        mClient.newCall(client.buildRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                sendOnFailureMessage(callBack, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                StyledDialogUtils.getInstance().dismissLoading();
                if (response.isSuccessful()) {
                    String result = response.body().string().trim();
                    Log.e("TAG", "result=" + result);
                    try {
                        if (!StringUtils.isEmpty(result)) {
                            String code = JSONUtils.getNoteJson(result,"code");
                            String msg = JSONUtils.getNoteJson(result,"info");
                            String data = JSONUtils.getNoteJson(result,"data");
                            if("1".equals(code)) {
                                sendOnSuccessMessage(callBack, data,msg);
                            }else if("-201".equals(code) || "-202".equals(code) ||
                                    "-203".equals(code) || "-300".equals(code)){
                                //需要验证用户登录
                                LoginCheckUtils.showLoginDialog(activity,true);
                                sendOnErrorMessage(callBack,Integer.parseInt(code),msg);
                            }else{
                                sendOnErrorMessage(callBack,Integer.parseInt(code),msg);
//                                sendOnErrorMessage(callBack,response.code(),msg);
                            }
                        }
                    } catch (Exception e) {
                        sendOnErrorMessage(callBack, response.code(), activity.getResources().getString(R.string.server_exception));
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    Log.e("TAG", "response.code=" + response.code());
                    sendOnErrorMessage(callBack, response.code(), activity.getResources().getString(R.string.server_exception));
                }
            }
        });
    }

    /**
     * 成功信息
     *
     * @param callBack
     * @param result
     */
    private void sendOnSuccessMessage(final BaseCallBack callBack, final Object result, final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result,msg);
            }
        });
    }

    /**
     * 失败信息
     *
     * @param callBack
     * @param call
     * @param e
     */
    private void sendOnFailureMessage(final BaseCallBack callBack, final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFailure(call, e);
            }
        });
    }

    /**
     * 错误信息
     *
     * @param callBack
     * @param code
     */
    private void sendOnErrorMessage(final BaseCallBack callBack, final int code, final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(code, msg);
            }
        });
    }
}
