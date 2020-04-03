package com.hooro.ri.config.netty;

import java.util.Date;

import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.model.Navigation;

public class ReceiveData {
	
	public static final String STATUS = "OK";
	/**
	 * 卡号
	 */
	private String simMark;
	
	/**
	 * 精度
	 */
	private String longitude;
	
	/**
	 * 维度
	 */
	private String latitude;
	
	/**
	 * 坐标类型
	 */
	private String type;
	
	/**
	 * 电量
	 */
	private String electricQuantity;
	
	
	/**
	 * 终端
	 */
	private Mobile mobile;
	
	/**
	 *app账号
	 */
	private String appUser;

	
	public Mobile getMobile() {
		return mobile;
	}

	public void setMobile(Mobile mobile) {
		this.mobile = mobile;
	}

	public String getSimMark() {
		return simMark;
	}

	public void setSimMark(String simMark) {
		this.simMark = simMark;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getElectricQuantity() {
		return electricQuantity;
	}

	public void setElectricQuantity(String electricQuantity) {
		this.electricQuantity = electricQuantity;
	}

    public final String checkData() {
    	if(StringUtils.isAllNotEmpty(simMark,longitude,latitude,type,electricQuantity)) {
    		return STATUS;
    	}
    	
    	if(StringUtils.isNotEmpty(appUser)) {
    		return STATUS;
    	}
    	return "";
    }
  
    public Navigation toNavigation() {
    	Navigation  navigation = new Navigation();
    	navigation.setLongitude(Double.valueOf(longitude));
    	navigation.setLatitude(Double.valueOf(latitude));
    	navigation.setMobile(mobile);
    	navigation.setSimMark(simMark);
    	navigation.setType(type);
    	navigation.setElectricQuantity(Integer.valueOf(electricQuantity));
    	return  navigation;
    }

	public String getAppUser() {
		return appUser;
	}

	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}
    
	
}
