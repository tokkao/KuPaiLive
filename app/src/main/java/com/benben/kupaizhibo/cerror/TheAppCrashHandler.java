package com.benben.kupaizhibo.cerror;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.kupaizhibo.bean.ErrorMsg;
import com.benben.kupaizhibo.db.ErrorMsgDao;
import com.benben.kupaizhibo.db.GreenDaoUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 *
 * 类名：MyCrashHandler 说明：错误日志收集类
 *
 */
@SuppressLint("SimpleDateFormat") public class TheAppCrashHandler implements UncaughtExceptionHandler {
	// 单例
	private static TheAppCrashHandler myCrashHandler;
	// 上下文
	private Context context;
	// log标记
	private final String TAG = "MyCrashHandler";
	// UncaughtExceptionHandler
	private UncaughtExceptionHandler mDefaultHandler;
	//错误信息数据库
	private ErrorMsgDao mErrorMsgDao;
	// 1.私有化构造方法
	private TheAppCrashHandler() {
	}

	// 单例
	public static TheAppCrashHandler getInstance() {
		if (myCrashHandler == null) {
			synchronized (TheAppCrashHandler.class) {
				if (myCrashHandler == null) {
					myCrashHandler = new TheAppCrashHandler();
				}
				return myCrashHandler;
			}
		}
		return myCrashHandler;
	}

	// 初始化
	public void init(Context context) {
		this.context = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleep一会后结束程序
//			ActivityManagerUtils.stopAll();
//			try {
//				Log.e(TAG, "sleep");
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//			}
			resetApp();
		}
	}

	/**
	 * 功能：捕捉错误信息，并写入日志
	 *
	 * @param ex
	 * @return
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			Log.e(TAG, "handleException --- ex==null");
			return true;
		}
		final String msg = ex.getLocalizedMessage();
		if (msg == null) {
			return false;
		}
//		// 使用Toast来显示异常信息
//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				Toast.makeText(context, "系统异常，正在启动...",
//						Toast.LENGTH_LONG).show();
//				Looper.loop();
//			}
//		}.start();

		try {
			// 1.获取当前程序的版本号. 版本的id
			String versioninfo = getVersionInfo();
			// 2.获取手机的硬件信息.
			String mobileInfo = getMobileInfo();
			// 3.把错误的堆栈信息 获取出来
			String errorinfo = getErrorInfo(ex);
			// 4.把所有的信息 还有信息对应的时间 提交到服务器
			ErrorMsg errorMsg = new ErrorMsg();
			errorMsg.setVersioninfo(versioninfo);
			errorMsg.setErrorinfo(errorinfo);
			errorMsg.setMobileInfo(mobileInfo);
			errorMsg.setTime(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
					.format(new Date()));
			mErrorMsgDao = GreenDaoUtils.getSingleTon(context).getDaoSession(context).getErrorMsgDao();
			mErrorMsgDao.save(errorMsg);
			String strErrorMsg = JSONUtils.toJsonString(errorMsg);
			ErrorWriteFieTool.writeToFile(strErrorMsg);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return true;
	}

	/**
	 * 功能：发现错误之后就重启程序
	 */
	private void resetApp() {
//		setSartActivity();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> mRunningProcess = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
			if (amProcess.processName.equals("com.xhot.assess")) {
				Process.killProcess(amProcess.pid);
			}
		}
		System.exit(1);
	}

//	private void setSartActivity() {
//		Intent intent = new Intent(context, SplashActivity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//		AlarmManager alarmManager = (AlarmManager) context
//				.getSystemService(Context.ALARM_SERVICE);
//		alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 4000,
//				pendingIntent);
//	}

	/**
	 * 获取错误的信息
	 *
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		Log.e("顶级错误", error);
		return error;
	}

	/**
	 * 获取手机的硬件信息
	 *
	 * @return
	 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		// 通过反射获取系统的硬件信息
		try {
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				// 暴力反射 ,获取私有的信息
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取手机的版本信息
	 *
	 * @return
	 */
	private String getVersionInfo() {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

}
