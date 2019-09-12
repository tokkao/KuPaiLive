package com.benben.kupaizhibo.cerror;

import android.annotation.SuppressLint;

import com.benben.kupaizhibo.config.SystemDir;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 类名：ErrorWriteFieTool
 * 说明：文件读写类
 *
 */
@SuppressLint("SimpleDateFormat")
public class ErrorWriteFieTool {

	/**
	 * 功能：错误日志写进文件中
	 * @param strErrorMsg
	 * @throws Exception
	 */
	public static void writeToFile(String strErrorMsg) throws Exception {

		String path = SystemDir.DIR_ERROR_MSG;
		File rootFile = new File(path);
		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String FileName = path + File.separator + sf.format(new Date())
				+ ".txt";
		File file = new File(FileName);
		if (file.exists() == false) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(strErrorMsg.getBytes());
		fos.flush();
		fos.close();
	}
}
