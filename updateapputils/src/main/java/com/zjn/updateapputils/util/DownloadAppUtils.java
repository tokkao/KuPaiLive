package com.zjn.updateapputils.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;


/**
 *Created by Teprinciple on 2016/12/13.
 */
public class DownloadAppUtils {
    private static final String TAG = DownloadAppUtils.class.getSimpleName();
    public static long downloadUpdateApkId = -1;//下载更新Apk 下载任务对应的Id
    public static String downloadUpdateApkFilePath;//下载更新Apk 文件路径

    /**
     * 安装APK
     * @param context
     * @param apkPath 安装包的路径
     */
    public static void installApk(Context context, Uri apkPath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(apkPath, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    /**
     * 通过浏览器下载APK包
     * @param context
     * @param url
     */
    public static void downloadForWebView(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 下载更新apk包(通知栏下载)
     * 权限:1,<uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />
     * @param context
     * @param url
     */
    public static long downloadForAutoInstall(Context context, String url,String filePath, String fileName, String title) {
        if (TextUtils.isEmpty(url)) {
            return -1;
        }
        try {
            Uri uri = Uri.parse(url);
            DownloadManager downloadManager = (DownloadManager) context
                    .getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            /**设置用于下载时的网络状态*/
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            //在通知栏中显示
            request.setVisibleInDownloadsUi(true);
            /**设置漫游状态下是否可以下载*/
            request.setAllowedOverRoaming(false);
            request.setTitle(title);
            downloadUpdateApkFilePath = filePath + File.separator + fileName;
            // 若存在，则删除
            deleteFile(downloadUpdateApkFilePath);
            Uri fileUri = Uri.parse("content://" + downloadUpdateApkFilePath);
            request.setDestinationUri(fileUri);
            downloadUpdateApkId = downloadManager.enqueue(request);
//            downloadManager.openDownloadedFile(downloadUpdateApkId);
        } catch (Exception e) {
            e.printStackTrace();
            downloadForWebView(context, url);
        }finally {
        }
        return downloadUpdateApkId;
    }

    public static boolean deleteFile(String fileStr) {
        File file = new File(fileStr);
        return file.delete();
    }
}
