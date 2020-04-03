package com.hooro.ri.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.eryansky.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hooro.ri.util.FieldHeader;

/**
 * <p>
 * 
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */

/**
 * @author admin
 *
 */
@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler"})
@Table(name="app_user")
public class AppUser implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
    /**
     * 账号
     */
	@FieldHeader(fieldName="账号",order=1)
	private String account;
    /**
     * 密码
     */
	@Column(name = "pass_word")
	private String passWord;
    /**
     * 创建时间
     */
	@Column(name = "create_time",updatable=false)
	private Date createTime=new Date();
    /**
     * 头像
     */
	@FieldHeader(fieldName="头像",order=4)
	@Column(name = "head_portrait")
	private String headPortrait;
    /**
     * 联系电话
     */
	@FieldHeader(fieldName="联系电话",order=3)
	private String phone;
    /**
     * 昵称
     */
	@FieldHeader(fieldName="昵称",order=2)
	@Column(name = "nick_name")
	private String nickName;
    /**
     * 登录token  
     */
	@Transient
    private String token;
    /**
     * 终端卡号 
     */
	@Column(name = "sim_mark")
    private String simMark;
	
    /**
     * 未审核的绑定请求 
     */
	@Transient
    private List<AppUserMobile>  appUserMobiles;
	
	
	public List<AppUserMobile> getAppUserMobiles() {
		return appUserMobiles;
	}

	public void setAppUserMobiles(List<AppUserMobile> appUserMobiles) {
		this.appUserMobiles = appUserMobiles;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String checkDate()
	{
		if(StringUtils.isBlank(account))
			return "账号不能为空";
		
		if(StringUtils.isBlank(passWord))
			return "密码不能为空";
		
		if(StringUtils.isBlank(nickName))
			return "昵称不能为空";
		
        return null;
	}

	public String getSimMark() {
		return simMark;
	}

	public void setSimMark(String simMark) {
		this.simMark = simMark;
	}


	


	
}
