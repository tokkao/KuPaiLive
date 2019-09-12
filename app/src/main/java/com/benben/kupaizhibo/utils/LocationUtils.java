package com.benben.kupaizhibo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.NetWorkUtils;

import org.greenrobot.greendao.annotation.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Author:zhn
 * Time:2019/6/29 0029 9:49
 * 定位-仅基于网络
 */
public class LocationUtils {
    private static final String TAG = "LocationUtils";

    //位置管理
    private static LocationManager mLocationManager;
    //位置监听
    private static LocationListener mLocationListener;

    private LocationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    @SuppressLint("MissingPermission")
    public static boolean registerLocation(Context context, @NotNull final OnLocationResultListener listener) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!NetWorkUtils.checkNetState(context)) {
            if (listener!=null){
                listener.cannotLocation("请检查网络链接！");
            }
            return false;
        }
        //仅基于网络定位
        String provider = LocationManager.NETWORK_PROVIDER;
        if(!mLocationManager.isProviderEnabled(provider)){
            if (listener!=null){
                listener.cannotLocation("请开启定位服务！");
            }
            return false;
        }
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null) {
            listener.getLastKnownLocation(location);
        }
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    listener.onLocationChanged(location);
                    //仅定位一次
                    unRegisterLocation();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LogUtils.e(TAG, "onStatusChanged    " + provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                LogUtils.e(TAG, "onProviderEnabled    " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                LogUtils.e(TAG, "onProviderDisabled    " + provider);
            }
        };
        mLocationManager.requestLocationUpdates(provider, 1000, 0, mLocationListener);
        return true;
    }

    public static void unRegisterLocation() {
        LogUtils.e(TAG,"unRegisterLocation");
        if (mLocationManager != null) {
            if (mLocationListener != null) {
                mLocationManager.removeUpdates(mLocationListener);
                mLocationListener = null;
            }
            mLocationManager = null;
        }
    }

    public interface OnLocationResultListener {
        /**
         * 定位不可用
         * @param msg
         */
        void cannotLocation(String msg);

        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        void getLastKnownLocation(Location location);

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        void onLocationChanged(Location location);

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        void onStatusChanged(String provider, int status, Bundle extras);//位置状态发生改变
    }

    /**
     * 根据经纬度获取地理位置
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return {@link Address}
     */
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
                return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    public static String getCountryName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    public static String getLocality(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    public static String getStreet(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

}
