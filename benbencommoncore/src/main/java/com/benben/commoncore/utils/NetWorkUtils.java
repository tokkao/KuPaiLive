package com.benben.commoncore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * 类名：NetWorkHelper 说明: 网络工作的工具类
 *
 * @author zjn
 */
public class NetWorkUtils {
	private static String LOG_TAG = "NetWorkHelper";

	public static Uri uri = Uri.parse("content://telephony/carriers");

	public static final String NETWORK_TYPE_WIFI = "wifi";
	public static final String NETWORK_TYPE_3G = "eg";
	public static final String NETWORK_TYPE_2G = "2g";
	public static final String NETWORK_TYPE_WAP = "wap";
	public static final String NETWORK_TYPE_UNKNOWN = "unknown";
	public static final String NETWORK_TYPE_DISCONNECT = "disconnect";

	/**
	 * 判断wifi是否连通
	 *
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断本地网络是否连接
	 *
	 * @param paramContext
	 * @return
	 */
	public static boolean isConnect(Context paramContext) {
		@SuppressLint("WrongConstant") ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
				.getSystemService("connectivity");
		if (localConnectivityManager != null) {
			NetworkInfo localNetworkInfo = localConnectivityManager
					.getActiveNetworkInfo();
			if ((localNetworkInfo != null)
					&& (localNetworkInfo.isConnected())
					&& (localNetworkInfo.getState() == NetworkInfo.State.CONNECTED))
				return true;
		}
		return false;
	}

	/**
	 * 判断是否有网络连接
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isAvailable()) {
						Log.d(LOG_TAG, "network is available");
						return true;
					}
				}
			}
		}
		Log.d(LOG_TAG, "network is not available");
		return false;
	}

	/**
	 * 检查网络状态
	 *
	 * @param context
	 * @return
	 */

	public static boolean checkNetState(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	/**
	 * 判断网络是否为漫游
	 */
	public static boolean isNetworkRoaming(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null
					&& info.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				if (tm != null && tm.isNetworkRoaming()) {
					Log.d(LOG_TAG, "network is roaming");
					return true;
				} else {
					Log.d(LOG_TAG, "network is not roaming");
				}
			} else {
				Log.d(LOG_TAG, "not using mobile network");
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isMobileDataEnable(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isMobileDataEnable = false;

		isMobileDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

		return isMobileDataEnable;
	}

	/**
	 * 判断wifi 是否可用
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isWifiDataEnable(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isWifiDataEnable = false;
		isWifiDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		return isWifiDataEnable;
	}

	/**
	 * Get network type
	 *
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager == null ? null
				: connectivityManager.getActiveNetworkInfo();
		return networkInfo == null ? -1 : networkInfo.getType();
	}

	/**
	 * Get network type name
	 *
	 * @param context
	 * @return
	 */
	public static String getNetworkTypeName(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo;
		String type = NETWORK_TYPE_DISCONNECT;
		if (manager == null
				|| (networkInfo = manager.getActiveNetworkInfo()) == null) {
			return type;
		}
		;

		if (networkInfo.isConnected()) {
			String typeName = networkInfo.getTypeName();
			if ("WIFI".equalsIgnoreCase(typeName)) {
				type = NETWORK_TYPE_WIFI;
			} else if ("MOBILE".equalsIgnoreCase(typeName)) {
				String proxyHost = android.net.Proxy.getDefaultHost();
				type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORK_TYPE_3G
						: NETWORK_TYPE_2G)
						: NETWORK_TYPE_WAP;
			} else {
				type = NETWORK_TYPE_UNKNOWN;
			}
		}
		return type;
	}

	/**
	 * Whether is fast mobile network
	 *
	 * @param context
	 * @return
	 */
	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager == null) {
			return false;
		}

		switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false;
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return true;
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return true;
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return true;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true;
			case TelephonyManager.NETWORK_TYPE_EHRPD:
				return true;
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				return true;
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return true;
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return false;
			case TelephonyManager.NETWORK_TYPE_LTE:
				return true;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;
			default:
				return false;
		}
	}

	/**
	 * 判断WiFi是否打开并且可以联网
	 *
	 * @return
	 */
	public static boolean isWiFiAvailable(Context context) {
		// TODO Auto-generated method stub
		boolean isWiFiAvailable = false;
		// 判断WiFi是否打开
		try {
			if (isWifiDataEnable(context)) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isWiFiAvailable;
	}


	public static String getIPAddress(Context context) {
		NetworkInfo info = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
				try {
					//Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
					for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
						NetworkInterface intf = en.nextElement();
						for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
							InetAddress inetAddress = enumIpAddr.nextElement();
							if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress();
							}
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}

			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
				return ipAddress;
			}
		} else {
			//当前无网络连接,请在设置中打开网络
		}
		return null;
	}

	/**
	 * 将得到的int类型的IP转换为String类型
	 *
	 * @param ip
	 * @return
	 */
	private static String intIP2StringIP(int ip) {
		return (ip & 0xFF) + "." +
				((ip >> 8) & 0xFF) + "." +
				((ip >> 16) & 0xFF) + "." +
				(ip >> 24 & 0xFF);
	}
}