package com.benben.kupaizhibo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by caojieting on 2017/12/15.
 */

public class PreferenceProvider {
    private Context context;
    /**
     * 保存在手机里面的文件名
     */
    public final String SP_NAME = "config";
    public final int SP_MODE = Context.MODE_PRIVATE;
    private SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public PreferenceProvider(Context context) {
        this.context = context;
    }

    public void setThirdLoginType(String third_login_type) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("third_login_type", third_login_type);
        editor.commit();
    }

    public String getThirdLoginType() {//三方登录的类型
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("third_login_type", "");
    }
    public void setOpenId(String open_id) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("open_id", open_id);
        editor.commit();
    }

    public String getOpenId() {//头像
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("open_id", "");
    }
    public void setPhoto(String photo) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("photo", photo);
        editor.commit();
    }

    public String getPhoto() {//头像
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("photo", "");
    }

    public void setUserName(String username) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("username", username);
        editor.commit();

    }

    public String getUserLevel() {//等级
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("user_level", "");
    }

    public void setUserLevel(String userLevel) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("user_level", userLevel);
        editor.commit();

    }

    public String getUserName() {//昵称
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("username", "");
    }

    public void setLatitude(String username) {//纬度
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("Latitude", username);
        editor.commit();

    }

    public String getLatitude() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("Latitude", "");
    }

    public void setAddress(String address) {//纬度
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("address", address);
        editor.commit();

    }

    public String getAddress() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("address", "");
    }

    public void setLongitude(String username) {//经度
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("Longitude", username);
        editor.commit();

    }

    public String getLongitude() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("Longitude", "");
    }

    public void setId(String id) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("id", id);
        editor.commit();

    }

    public String getId() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("id", "");
    }

    public void setUId(String uid) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("uid", uid);
        editor.commit();

    }

    public String getUId() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("uid", "");
    }

    public void setUserType(int userType) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("userType", userType);
        editor.commit();

    }

    public int getUserType() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getInt("userType", 0);
    }

    public void setMobile(String mobile) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("mobile", mobile);
        editor.commit();

    }

    public String getMobile() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("mobile", "");
    }


    public void setToken(String token) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("token", token);
        editor.commit();

    }

    public String getToken() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("token", "0");
    }

    public void setEasemob(String token) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("easemob", token);
        editor.commit();

    }

    public String getEasemob() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("easemob", "0");
    }

    public void setSex(int sex) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("sex", sex);
        editor.commit();

    }

    public int getSex() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getInt("sex", 0);
    }

    public void setBoBi(String bobi) {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("bobi", bobi);
        editor.commit();

    }

    public String getBobi() {
        SharedPreferences spf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spf.getString("bobi", "0");
    }
    /**
     * 根据类型调用不同的保存方法
     *
     * @param context 上下文
     * @param key     添加的键
     * @param value   添加的值
     * @return 是否添加成功（可以使用apply提交）
     */
    public boolean put(Context context, String key, Object value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, SP_MODE);
        }
        editor = sp.edit();
        if (value == null) {
            editor.putString(key, null);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        return editor.commit();
    }


    public Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,
                SP_MODE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }


}
