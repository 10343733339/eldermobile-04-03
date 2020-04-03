package com.hooro.ri.model;

import java.io.Serializable;
import java.util.Date;

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
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler","appUser"})
@Table(name="login_info")
public class LoginInfo implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
    /**
     * App用户ID
     */
	@ManyToOne(cascade= {CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;
    /**
     * 登录令牌
     */
	private String token;
    /**
     * 创建时间
     */
	@Column(name = "create_time",updatable=false)
	private Date createTime=new Date();
    /**
     * 状态
     */
	private Integer status;
    /**
     * 信鸽token
     */
	@Column(name = "xg_token")
	private String xg_token;
    /**
     * 信鸽token类型  1android 2ios 
     */
	@Column(name = "xg_type")
    private Integer xgType;
	
	@Transient
	private  String  registrationId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.xg_token =  registrationId;
		this.registrationId = registrationId;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getXg_token() {
		return xg_token;
	}

	public void setXg_token(String xg_token) {
		this.xg_token = xg_token;
	}

	public Integer getXgType() {
		return xgType;
	}

	public void setXgType(Integer xgType) {
		this.xgType = xgType;
	}




}
