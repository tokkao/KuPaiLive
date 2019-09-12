package com.benben.commoncore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能:获取手机信息的工具类
 * <p>
 * zjn 2015-11-16
 */
@SuppressLint("NewApi")
public class DeviceUtils {

    /**
     * 生产商家
     *
     * @return
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获得固件版本
     *
     * @return
     */
    public static String getRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获得手机品牌
     *
     * @return
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机运营商
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        return tm.getSimOperatorName();
    }

    /**
     * 得到本机手机号码,未安装SIM卡或者SIM卡中未写入手机号，都会获取不到
     *
     * @return
     */
    public static String getThisPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getLine1Number();

        return number;
    }

    /**
     * 是否是电话号码
     *
     * @param phonenumber
     * @return
     */
    public static boolean isPhoneNumber(String phonenumber) {
        Pattern pa = Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$");
        Matcher ma = pa.matcher(phonenumber);
        return ma.matches();
    }

    /**
     * 打电话
     *
     * @param phone
     * @param context
     */
    public static void doPhone(Context context, String phone) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                + phone));
        context.startActivity(phoneIntent);
    }

    /**
     * 发短信
     *
     * @param phone
     * @param content
     * @param
     */
    public static void doSMS(Context context, String phone, String content) {
        Uri uri = null;
        if (!TextUtils.isEmpty(phone))
            uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

    /**
     * 得到屏幕信息 getScreenDisplayMetrics().heightPixels 屏幕高
     * getScreenDisplayMetrics().widthPixels 屏幕宽
     *
     * @return
     */
    public static DisplayMetrics getScreenDisplayMetrics(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = manager.getDefaultDisplay();
        display.getMetrics(displayMetrics);

        return displayMetrics;

    }

    /**
     * 屏幕分辨率
     *
     * @param context
     * @return
     */
    public static float getDip(Context context) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 安装apk
     */
    public static void instance(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 是否安装了
     *
     * @param packageName
     * @return
     */
    public static boolean isInstall(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> packs = packageManager
                .getInstalledApplications(PackageManager.GET_ACTIVITIES);
        for (ApplicationInfo info : packs) {
            if (info.packageName.equals(packageName))
                return true;
        }
        return false;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    /**
     * 将Toast放在屏幕上方
     *
     * @param message
     */
    public static void show(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0,
                (getScreenDisplayMetrics(context).heightPixels / 5));
        toast.show();
    }

    /**
     * 调用浏览器打开
     *
     * @param context
     * @param url
     */
    public static void openWeb(Context context, String url) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到sd卡路径
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 得到网络类型，0是未知或未连上网络，1为WIFI，2为2g，3为3g，4为4g
     *
     * @return
     */
    public static int getNetType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        int type = 0;
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return type;
        }

        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                type = 1;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                type = getNetworkClass(info.getSubtype());
                break;

            default:
                type = 0;
                break;
        }

        return type;
    }

    /**
     * 判断数据连接的类型
     *
     * @param networkType
     * @return
     */
    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:

                return 2;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return 3;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 4;
            default:
                return 0;
        }
    }

    /**
     * 获取应用程序的IMEI号
     */
    public static String getIMEI(Context context) {
        if (context == null) {
            Log.e("YQY", "getIMEI  context为空");
        }
        TelephonyManager telecomManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telecomManager.getDeviceId();
        Log.e("YQY", "IMEI标识：" + imei);
        return imei;
    }

    /**
     * 获取设备的系统版本号
     */
    public static int getDeviceSDK() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        return sdk;
    }

    /**
     * 获取设备的型号
     */
    public static String getDeviceName() {
        String model = android.os.Build.MODEL;
        return model;
    }

    /** 检测是否是中兴机器 */
    public static boolean isZte() {
        return getDeviceName().toLowerCase().indexOf("zte") != -1;
    }
}
