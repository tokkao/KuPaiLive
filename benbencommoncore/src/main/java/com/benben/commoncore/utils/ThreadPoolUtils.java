package com.benben.commoncore.utils;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能:线程池的工具类
 *
 * zjn
 * 2015-11-4
 */
public class ThreadPoolUtils {
	private static final int THREAD_SIZE = 1;

	private static ThreadPoolUtils m_ThreadPool;

	private static Object object = new Object();

	private static ExecutorService m_ExecutorService;
	private WeakReference<ExecutorService> m_WeakReference_exs;
	private ThreadPoolUtils() {
		m_ExecutorService = Executors.newFixedThreadPool(THREAD_SIZE);
	}

	public static ThreadPoolUtils newInstance() {
		if (m_ThreadPool == null){
			synchronized (object) {
				if (m_ThreadPool == null) {
					m_ThreadPool = new ThreadPoolUtils();
				}
			}
		}
		return m_ThreadPool;
	}

	/**
	 * 线程池执行的方法
	 *
	 * @param runnable
	 */
	public  void execute(Runnable runnable) {
		m_WeakReference_exs=new WeakReference<ExecutorService>(m_ExecutorService);
		m_WeakReference_exs.get().execute(runnable);
	}

	public void clearThreadPoolUtils(){
//		if(m_WeakReference_exs != null){
//			m_WeakReference_exs.get().shutdownNow();
//		}
	}

}
