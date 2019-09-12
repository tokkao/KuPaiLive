package com.benben.commoncore.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 功能:activity管理类 zjn
 * 
 * 2015-9-18
 */
public class ActivityManagerUtils {

	private static List<Object> activitys = new ArrayList<>();

	private static  List<Object> clearActivity = new ArrayList<>();

	public static List<Object> getClearActivity() {
		return clearActivity;
	}

	public static void setClearActivity(List<Object> clearActivity) {
		ActivityManagerUtils.clearActivity = clearActivity;

	}

	/**
	 * 根据activity的 名字获得activity的实例
	 * */
	@SuppressWarnings("unchecked")
	public static <T extends Activity> T getActivity(Class<T> cls){
		Activity act = null;
		for (int i = 0, size = activitys.size(); i < size; i++){
			if(act instanceof Activity){
				act = (Activity) activitys.get(i);
				if(act.getClass() == cls){
					return (T)act;
				}
			}
		}
		return null;
	}

	/**
	 * activity add
	 * 
	 * @param paramActivity
	 */
	public static void add(Object paramActivity) {
		synchronized (activitys) {
			activitys.add(paramActivity);
			return;
		}
	}

	public static Activity currentActivity() {
		if(activitys != null && activitys.size() > 0){
			Activity activity = (Activity)activitys.get(activitys.size() -1 );
			return activity;
		}
		return null ;
	}


	/**
	 * activity add
	 *
	 * @param paramActivity
	 */
	public static void addOneActivity(Object paramActivity) {
		synchronized (clearActivity) {
			clearActivity.add(paramActivity);
			return;
		}
	}

	/**
	 * activity remove
	 *
	 * @param paramActivity
	 */
	public static void remove(Object paramActivity) {
		synchronized (activitys) {
			if (activitys.contains(paramActivity)) {
				activitys.remove(paramActivity);
			}
		}
	}
	/**
	 * activity remove
	 *
	 *
	 */
	public static void clear() {
		synchronized (clearActivity) {
			Iterator<Object> iterator = clearActivity.iterator();
			while(iterator.hasNext()){
				((Activity) iterator.next()).finish();
			}
			clearActivity.clear();

		}
	}

	/**
	 * activity remove
	 *
	 * @param paramActivity
	 */
	public static void removeClear(Activity paramActivity) {
		synchronized (clearActivity) {
			if (clearActivity.contains(paramActivity)) {
				clearActivity.remove(paramActivity);
			}
		}
	}

	/**
	 * exit app
	 */
	public static void stopAll() {
		synchronized (activitys) {
			Iterator<Object> iterator = activitys.iterator();
//			if (!iterator.hasNext()) {
//				activitys.clear();
//				return;
//			}
			while(iterator.hasNext()){
				((Activity) iterator.next()).finish();
			}
			activitys.clear();
//			((Activity) iterator.next()).finish();
//			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 功能：发现错误之后就重启程序
	 */
	public static  void resetApp() {
//		setSartActivity();
//		ActivityManager mActivityManager = (ActivityManager) BlackBearsApplicationContext.context
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		List<ActivityManager.RunningAppProcessInfo> mRunningProcess = mActivityManager
//				.getRunningAppProcesses();
//		for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
//			if (amProcess.processName.equals("com.xhot.assess")) {
//				Process.killProcess(amProcess.pid);
//			}
//		}
//		System.exit(1);
	}

	/**
	 *
	 * @param className 不需要结束的activity的名字
	 */
	public static void finishAllActivitys(String className) {
		for (int i = 0, size = activitys.size(); i < size; i++) {
			if (null != activitys.get(i)&&!activitys.get(i).getClass().getName().equals(className)) {
				((Activity)activitys.get(i)).finish();
			}
		}
		activitys.clear();
	}

}
