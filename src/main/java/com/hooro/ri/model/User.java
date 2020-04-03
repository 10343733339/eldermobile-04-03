package com.hooro.ri.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 用户表
 * @ClassName: User
 * @author ty
 * @date 2018-3-16 下午02:12:55
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "operatorinfo")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler","role","organizations","channel",})
//@Cacheable
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
//标识是一个实体Bean
//表名
//设置springMvc转换Json 过滤字段、设置懒加载不设置这里还会出现1+N问题
//开启二级缓存
//设置读写级别缓存、可更改缓存级别
public class User implements Serializable {
	
	private Integer id; 
	/**
     *用户名
     */
	private String account;  
	/**
     * 密码
     */
	private String password; 
	/**
     * 昵称
     */
	private String nickname; 
	/**
     * 电话
     */
	private String telephone; 
	/**
     * 邮箱
     */
	private String email; 
	/**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime; 
	/**
     * 身份证号
     */
	private String idcard; 
	/**
     * 级别
     */
	private Integer rank; 
	/**
     * 状态
     */
	private Integer status; 
	/**
     * 关联角色
     */
	private Role role;
	/**
     * 角色id
     */
	private Integer roleId; 
	/**
     * 角色名称
     */
	private String roleName; 
	/**
     * 验证码
     */
	private String code; 
	/**
	 * 登录次数
	 */
	private Integer loginCount; 
	/**
	 * ip
	 */
	private String ip; 
	/**
	 * last_ip
	 */
	private String lastIp;
	/**
	 * lastTime
	 */
	private Date lastTime;
	/**
	 * atTime
	 */
	private Date atTime;
	
	private String lastTimeStrin;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "account",length = 50)
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	@Column(name = "password",length = 50)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "nickname",length = 50)
	public String getNickname() {
	
		return nickname;
	}
	
	public void setNickname(String nickname) {
	
		this.nickname = nickname;
	}
	
	@Column(name = "telephone",length = 50)
	public String getTelephone() {
	
		return telephone;
	}
	
	public void setTelephone(String telephone) {
	
		this.telephone = telephone;
	}
	
	@Column(name = "email",length = 50)
	public String getEmail() {
	
		return email;
	}
	
	public void setEmail(String email) {
	
		this.email = email;
	}
	
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "create_time",updatable=false)
	public Date getCreateTime() {
	
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
	
		this.createTime = createTime;
	}
	
	@Column(name = "idcard",length = 50)
	public String getIdcard() {
	
		return idcard;
	}
	
	public void setIdcard(String idcard) {
	
		this.idcard = idcard;
	}
	
	@Column(name = "rank")
	public Integer getRank() {
	
		return rank;
	}
	
	public void setRank(Integer rank) {
	
		this.rank = rank;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
	
		return status;
	}
	
	public void setStatus(Integer status) {
	
		this.status = status;
	}
	
	
	@ManyToOne(cascade = { CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
    @JoinColumn(name = "role_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public Role getRole() {
	
		return role;
	}
	
	public void setRole(Role role) {
	
		this.role = role;
	}
	

	@Transient
	public Integer getRoleId() {
		if(role!=null){
			roleId = role.getId();
		}
		return roleId;
	}
	
	public void setRoleId(Integer roleId) {
	
		this.roleId = roleId;
	}
	
	@Transient
	public String getRoleName() {
		if(role!=null){
			roleName = role.getName();
		}
		return roleName;
	}
	
	public void setRoleName(String roleName) {
	
		this.roleName = roleName;
	}
	 
	@Transient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "login_count")
	public Integer getLoginCount() {
		if(loginCount==null)
			loginCount=0;
		return loginCount;
	}
	
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	
	@Column(name = "ip")
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name = "last_ip")
	public String getLastIp() {
		return lastIp;
	}
	
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "last_time")
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	
	@Transient
	public String getLastTimeStrin() {
		if(lastTime!=null)
		{
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lastTimeStrin=sdf.format(lastTime);
		}
		return  lastTimeStrin;
	}
	public void setLastTimeStrin(String lastTimeStrin) {
		this.lastTimeStrin = lastTimeStrin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "at_time")
	public Date getAtTime() {
		return atTime;
	}
	public void setAtTime(Date atTime) {
		this.atTime = atTime;
	}


}
