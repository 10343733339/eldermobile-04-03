package com.hooro.ri.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.eryansky.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <p>
 * 
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */

@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler","mobile","appUser"})

@Table(name="app_user_mobile")
public class AppUserMobile implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
    /**
     * 终端id
     */
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name = "mobile_id")
	private Mobile mobile;
    /**
     * App用户id
     */
	
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;
    /**
     * 是否通过审核
     */
	@Column(name = "is_examine")
	private Integer isExamine;

    /**
     * 终端名称
     */
	@Column(name = "mobile_name")
	private String mobileName;
	
	/**
	 * 是否自动接听
	 */
	@Column(name = "is_auto_answer")
	private Integer isAutoAnswer;
    /**
     * 终端卡号
     */
	@Transient
	private String simMark;
	
    /**
     * appAccount
     */
	@Transient
	private String appAccount;
    /**
     * appNcikName
     */
	@Transient
	private String appNickName;
	
    /**
     * 是否在线0 不在线 1在线
     */
	@Transient
	private Integer isOnLine = 0;
	
	/**
     * 经度
     */
	@Transient
	private Double longitude;
    /**
     * 纬度
     */
	@Transient
	private Double latitude;

	/**
	 * 电量
	 */
	@Transient
	private Integer electricQuantity;
	
	
	
	
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getElectricQuantity() {
		return electricQuantity;
	}

	public void setElectricQuantity(Integer electricQuantity) {
		this.electricQuantity = electricQuantity;
	}

	public Integer getIsOnLine() {
		return isOnLine;
	}

	public void setIsOnLine(Integer isOnLine) {
		this.isOnLine = isOnLine;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Mobile getMobile() {
		return mobile;
	}

	public void setMobile(Mobile mobile) {
		this.mobile = mobile;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	



	public Integer getIsExamine() {
		return isExamine;
	}

	public void setIsExamine(Integer isExamine) {
		this.isExamine = isExamine;
	}

	public String getMobileName() {
		return mobileName;
	}

	public void setMobileName(String mobileName) {
		this.mobileName = mobileName;
	}

	public String getSimMark() {
		if(mobile!=null)
			this.simMark=mobile.getSimMark();
		return simMark;
	}

	public void setSimMark(String simMark) {
		this.simMark = simMark;
	}

	public String getAppAccount() {
		if(appUser!=null&&StringUtils.isNotBlank(appUser.getAccount()))
			this.appAccount=appUser.getAccount();
		return appAccount;
	}

	public void setAppAccount(String appAccount) {
		this.appAccount = appAccount;
	}

	public String getAppNickName() {
		if(appUser!=null)
			this.appNickName=appUser.getNickName();
		return appNickName;
	}

	public void setAppNickName(String appNickName) {
		this.appNickName = appNickName;
	}

	public Integer getIsAutoAnswer() {
		return isAutoAnswer;
	}

	public void setIsAutoAnswer(Integer isAutoAnswer) {
		this.isAutoAnswer = isAutoAnswer;
	}



}
