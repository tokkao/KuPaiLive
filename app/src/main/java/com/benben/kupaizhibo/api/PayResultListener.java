package com.benben.kupaizhibo.api;

/**
 * Created by Administrator on 2019/4/7.
 */

/**
 * 微信支付的回调
 */
public interface PayResultListener {
    public void onPaySuccess();

    public void onPayError();

    public void onPayCancel();

}
