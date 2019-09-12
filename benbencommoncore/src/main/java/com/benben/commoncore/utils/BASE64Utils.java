package com.benben.commoncore.utils;

/**
 * 功能:base64 解密工具类
 *
 * zjn 2017-05-27
 */
public class BASE64Utils {

	//黑熊搏击乐视上传专用解密方法
	public static String decode(String value){
		String parseValue = "";
		byte[] bytes = Base64.decode(value);
		parseValue = new String(bytes);
		//去除首4 尾5 字符
		parseValue = parseValue.substring(4,parseValue.length()-5);
		//重新进行 解码
		bytes = Base64.decode(parseValue);
		parseValue = new String(bytes);
		//去除  首 3 尾 2 字符其
		parseValue = parseValue.substring(3,parseValue.length() -2);
		return parseValue;
	}

}
