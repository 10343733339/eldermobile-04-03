package com.hooro.ri.vo;

public class AppUserMobileVo {
	/**
	 * 账号名
	 */
	private String account;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 是否在线  0不在线  1在线
	 */
	private Integer isOnLine = 0;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getIsOnLine() {
		return isOnLine;
	}
	public void setIsOnLine(Integer isOnLine) {
		this.isOnLine = isOnLine;
	}
	
	

}
