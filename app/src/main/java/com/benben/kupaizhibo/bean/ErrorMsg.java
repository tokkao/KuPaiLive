package com.benben.kupaizhibo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 *
 * 类名：ErrorMsg 说明：错误信息实体类
 *
 */
@Entity
public class ErrorMsg {

	// 主键
	@Id(autoincrement = true)
	private Long id;

	/** 错误详情 */
	private String errorinfo;
	/** 系统版本 */
	private String versioninfo;
	/** 手机硬件信息 */
	private String mobileInfo;
	/** 当前用户名 */
	private String userName;
	/** 当前用户手机号 */
	private String phoneNum;
	/** 当前用户地址 */
	private String userAddress;
	/** 时间 */
	private String time;
	public String getTime() {
		return this.time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUserAddress() {
		return this.userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getPhoneNum() {
		return this.phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileInfo() {
		return this.mobileInfo;
	}
	public void setMobileInfo(String mobileInfo) {
		this.mobileInfo = mobileInfo;
	}
	public String getVersioninfo() {
		return this.versioninfo;
	}
	public void setVersioninfo(String versioninfo) {
		this.versioninfo = versioninfo;
	}
	public String getErrorinfo() {
		return this.errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Generated(hash = 582481450)
	public ErrorMsg(Long id, String errorinfo, String versioninfo,
                    String mobileInfo, String userName, String phoneNum, String userAddress,
                    String time) {
		this.id = id;
		this.errorinfo = errorinfo;
		this.versioninfo = versioninfo;
		this.mobileInfo = mobileInfo;
		this.userName = userName;
		this.phoneNum = phoneNum;
		this.userAddress = userAddress;
		this.time = time;
	}
	@Generated(hash = 1532244802)
	public ErrorMsg() {
	}
}
