package com.benben.kupaizhibo.ui.live;

/**
 * socket回调接口
 * create by zjn on 2019/5/16 0016
 * email:168455992@qq.com
 */
public interface SocketCallbackListener {
    //连接成功
    void onConnect(Object... args);
    //断开连接
    void onDisConnect(Object... args);
    //发送的广播监听
    void onBroadCastingListener(Object... args);
    //握手包成功监听
    void onHandShakeListener(Object... args);
}
