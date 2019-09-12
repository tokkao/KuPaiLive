package com.benben.kupaizhibo.utils;

import android.util.Log;

import com.benben.commoncore.utils.LogUtils;
import com.benben.kupaizhibo.config.Constants;
import com.benben.kupaizhibo.ui.live.SocketCallbackListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import io.socket.engineio.client.transports.WebSocket;

/**
 * 功能:socket.io 工具类封装
 * create by zjn on 2019/5/16 0016
 * email:168455992@qq.com
 */
public class SocketIoUtils {

    private static final String TAG = "Socket.io";
    private static SocketIoUtils mInstance;
    private Socket mSocket;
    private SocketCallbackListener mSocketCallbackListener;
    private SocketIoUtils() {
    }

    public static SocketIoUtils getInstance() {
        if(mInstance == null){
            synchronized (SocketIoUtils.class){
                if(mInstance == null){
                    mInstance = new SocketIoUtils();
                }
            }
        }
        return mInstance;
    }

    public void sendMsg(String event,String msg){
        if(mSocket != null){
            mSocket.emit(event,msg);
        }
    }

    public SocketIoUtils setSocketCallbackListener(SocketCallbackListener callbackListener){
        this.mSocketCallbackListener = callbackListener;
        return mInstance;
    }


    public void connect(String url){
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.transports = new String[]{WebSocket.NAME};
            mSocket = IO.socket(url,options);
            mSocket.on(Socket.EVENT_CONNECT, connect);
            mSocket.on(Socket.EVENT_DISCONNECT, disConnect);
            mSocket.on(Socket.EVENT_RECONNECT, reconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeOut);
            mSocket.on(Constants.EVENT_BROADCASTINGLISTENER,broadCastingListener);
            mSocket.on(Constants.EVENT_HEARTBEAT,heartBeat);
            mSocket.on(Constants.EVENT_HANDSHAKE,handShake);
            // Adding authentication headers when encountering EVENT_TRANSPORT
            mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Transport transport = (Transport) args[0];
                    // Adding headers when EVENT_REQUEST_HEADERS is called
                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.v(TAG, "Caught EVENT_REQUEST_HEADERS after EVENT_TRANSPORT, adding headers");
                            Map<String, List<String>> mHeaders = (Map<String, List<String>>)args[0];
                            mHeaders.put("Authorization", Arrays.asList("Basic bXl1c2VyOm15cGFzczEyMw=="));
                        }
                    });
                }
            });
            mSocket.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onDestory(){
        if(mSocket != null) {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_CONNECT, connect);
            mSocket.off(Socket.EVENT_DISCONNECT, disConnect);
            mSocket.off(Socket.EVENT_RECONNECT, reconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeOut);
            mSocket.off(Constants.EVENT_BROADCASTINGLISTENER,broadCastingListener);
            mSocket.off(Constants.EVENT_HEARTBEAT,heartBeat);
            mSocket.off(Constants.EVENT_HANDSHAKE,handShake);
        }
    }

    //监听是否连接成功
    private Emitter.Listener connect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e(TAG,"连接成功");

            if (mSocketCallbackListener == null) {
                return;
            }
            mSocketCallbackListener.onConnect(args);
        }
    };
    //监听是否断开
    private Emitter.Listener disConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e(TAG,"断开连接");
            //执行重连相关处理
            //SOCKET会自动重连，此处处理逻辑是直播其他业务
            if (mSocketCallbackListener == null) {
                return;
            }
            mSocketCallbackListener.onDisConnect(args);
        }
    };
    //监听重连
    private Emitter.Listener reconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e(TAG,"重连成功");
            //SOCKET会自动重连，此处处理逻辑是直播其他业务

        }
    };
    //监听服务器广播消息
    private Emitter.Listener broadCastingListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocketCallbackListener == null) {
                return;
            }
            mSocketCallbackListener.onBroadCastingListener(args);
        }
    };
    //监听连接性检测 心跳包
    private Emitter.Listener heartBeat = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            LogUtils.e(TAG,"连接成功");
            //发送心跳包
        }
    };
    //监听握手是否成功
    private Emitter.Listener handShake = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocketCallbackListener == null) {
                return;
            }
            mSocketCallbackListener.onHandShakeListener(args);
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "Error connecting");
        }
    };

    private Emitter.Listener onConnectTimeOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "Error connect time out");
        }
    };

}
