package com.benben.kupaizhibo.http;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.config.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Description:封装OkHttp
 *
 * @author zjn
 * Email：168455992@qq.com
 * @date 2019/1/11
 */
public class BaseOkHttpClient {

    private Builder mBuilder;
    private String mTimeStamp;
    private String mToken;
    private String mSignatureNonce;
    private Map<String, String> paramsMap;

    private BaseOkHttpClient(Builder builder) {
        this.mBuilder = builder;
    }

    public Request buildRequest() {
        paramsMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        Request.Builder builder = new Request.Builder();
        paramsMap.put("appid", Constants.APP_ID);
        mTimeStamp = String.valueOf((new Date().getTime() / 1000));
        paramsMap.put("timestamp", mTimeStamp);
        paramsMap.put("appsecret", Constants.APP_SECRET);
        if (!StringUtils.isEmpty(KuPaiLiveApplication.mPreferenceProvider.getToken())) {
            mToken = KuPaiLiveApplication.mPreferenceProvider.getToken();
        } else {
            mToken = "0";
        }
        mSignatureNonce = String.valueOf(UUID.randomUUID());
        paramsMap.put("signaturenonce", mSignatureNonce);
        paramsMap.put("usertoken", mToken);
        if ("GET".equals(mBuilder.method)) {
            builder.url(buildGetRequestParam(mTimeStamp, mToken, mSignatureNonce));
            builder.get();
        } else if (mBuilder.method == "POST") {
            builder.url(mBuilder.url);
            Log.e("BaseOkHttpClient", "post:url=" + mBuilder.url);
            try {
                builder.post(buildPostRequestParam(mTimeStamp, mToken, mSignatureNonce));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }

    private String jsonSort(Map<String, String> paramsMap) {
        String str = "";
        Set<String> keySet = paramsMap.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuffer sb = new StringBuffer("");
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(key).append("=").append(paramsMap.get(key)).append("&");
        }
        str = sb.toString();
        return str.substring(0, str.length() - 1);
    }

    /**
     * GET拼接参数
     *
     * @return
     */
    private String buildGetRequestParam(String timeStamp, String token, String signatureNonce) {
        if (mBuilder.params.size() > 0) {
            //有参数
            for (int i = 0; i < mBuilder.params.size(); i++) {
                paramsMap.put(mBuilder.params.get(i).getKey(), mBuilder.params.get(i) == null ? "" : mBuilder.params.get(i).getObj().toString());
            }
        }
        String objStr = jsonSort(paramsMap);
        String signature = null;
        try {
            signature = getSHA(objStr);
        } catch (Exception e) {
            e.printStackTrace();
            signature = objStr;
        }

        Uri.Builder builder = Uri.parse(mBuilder.url).buildUpon();
        builder.appendQueryParameter("appid", Constants.APP_ID);
        builder.appendQueryParameter("signaturenonce", signatureNonce);
        builder.appendQueryParameter("timestamp", timeStamp);
        builder.appendQueryParameter("usertoken", token);
        builder.appendQueryParameter("signature", signature);
        if (mBuilder.params.size() <= 0) {
            String url = builder.build().toString();
            return url;
        }
        for (RequestParameter p : mBuilder.params) {
            builder.appendQueryParameter(p.getKey(), p.getObj() == null ? "" : p.getObj().toString());
        }
        String url = builder.build().toString();
        return url;
    }

    /**
     * POST拼接参数
     *
     * @return
     */
    private RequestBody buildPostRequestParam(String timeStamp, String token, String signatureNonce) throws JSONException {
        if (mBuilder.isJsonParam) {
            if (mBuilder.params.size() > 0) {
                //有参数
                for (int i = 0; i < mBuilder.params.size(); i++) {
                    paramsMap.put(mBuilder.params.get(i).getKey(), mBuilder.params.get(i).getObj() == null ? "" : mBuilder.params.get(i).getObj().toString());
                }
            }
            String objStr = jsonSort(paramsMap);
            String signature = null;
            try {
                signature = getSHA(objStr);
            } catch (Exception e) {
                e.printStackTrace();
                signature = objStr;
            }
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("appid", Constants.APP_ID);
            jsonObj.put("signaturenonce", signatureNonce);
            jsonObj.put("timestamp", timeStamp);
            jsonObj.put("usertoken", token);
            jsonObj.put("signature", signature);
            for (RequestParameter p : mBuilder.params) {
                jsonObj.put(p.getKey(), p.getObj());
            }
            String json = jsonObj.toString();
            LogUtils.e("BaseOkHttpClient", "post:json="+json);
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        }
        MultipartBody.Builder builder = null;
        if (mBuilder.params.size() > 0) {
            builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (RequestParameter p : mBuilder.params) {
                if (p.getFile() != null) {
                    MediaType type = MediaType.parse("image/png");
                    builder.addFormDataPart(p.getKey(), p.getObj() == null ? "" : p.getObj().toString(), RequestBody.create(type, p.getFile()));
                } else {
                    builder.addFormDataPart(p.getKey(), p.getObj() == null ? "" : p.getObj().toString());
                }
            }
        }
        return builder.build();
    }

    /**
     * 回调调用
     *
     * @param callBack
     */
    public void enqueue(Activity activity, BaseCallBack callBack) {
        OkHttpManager.getInstance().request(activity, this, callBack);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String url;
        private String method;
        private List<RequestParameter> params;
        private boolean isJsonParam;

        public BaseOkHttpClient build() {
            return new BaseOkHttpClient(this);
        }

        private Builder() {
            method = "GET";
            params = new ArrayList<>();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * GET请求
         *
         * @return
         */
        public Builder get() {
            method = "GET";
            return this;
        }

        /**
         * POST请求
         *
         * @return
         */
        public Builder post() {
            method = "POST";
            return this;
        }

        /**
         * JSON参数
         *
         * @return
         */
        public Builder json() {
            isJsonParam = true;
            return post();
        }

        /**
         * Form请求
         *
         * @return
         */
        public Builder form() {
            return this;
        }

        /**
         * 添加参数
         *
         * @param key
         * @param value
         * @return
         */
        public Builder addParam(String key, Object value) {
            if (params == null) {
                params = new ArrayList<>();
            }
            params.add(new RequestParameter(key, value));
            return this;
        }

        public Builder addFile(String key, String fileName, File file) {
            if (params == null) {
                params = new ArrayList<>();
            }
            params.add(new RequestParameter(key, getValueEncoded(fileName), file));
            return this;
        }
    }

    public static String getSHA(String info) {
        byte[] digesta = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将摘要转为字符串
        String rs = byte2hex(digesta);
        return rs;
    }

    public String sha1(String data) throws NoSuchAlgorithmException {
        //加盐   更安全一些
        data += "lyz";
        //信息摘要器                                算法名称
        MessageDigest md = MessageDigest.getInstance("SHA1");
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //使用指定的字节来更新我们的摘要
        md.update(b);
        //获取密文  （完成摘要计算）
        byte[] b2 = md.digest();
        //获取计算的长度
        int len = b2.length;
        //16进制字符串
        String str = "0123456789abcdef";
        //把字符串转为字符串数组
        char[] ch = str.toCharArray();

        //创建一个40位长度的字节数组
        char[] chs = new char[len * 2];
        //循环20次
        for (int i = 0, k = 0; i < len; i++) {
            byte b3 = b2[i];//获取摘要计算后的字节数组中的每个字节
            // >>>:无符号右移
            // &:按位与
            //0xf:0-15的数字
            chs[k++] = ch[b3 >>> 4 & 0xf];
            chs[k++] = ch[b3 & 0xf];
        }

        //字符数组转为字符串
        return new String(chs);
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (byte aB : b) {
            stmp = (Integer.toHexString(aB & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    //wanghk add transform code format
    private static String getValueEncoded(String value) {
        if (value == null) return "null";
        String newValue = value.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    return URLEncoder.encode(newValue, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return newValue;
    }
}
