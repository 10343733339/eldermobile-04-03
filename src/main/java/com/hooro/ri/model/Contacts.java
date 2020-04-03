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
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler","mobile"})
public class Contacts implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
    /**
     * 联系人姓名
     */
	@Column(name = "contact_name")
	private String contactName;
    /**
     * 联系人电话
     */
	@Column(name = "contact_phone")
	private String contactPhone;
    /**
     * 创建时间
     */
	@Column(name = "create_time",updatable=false)
	private Date createTime=new Date();
    /**
     * 最后修改人账号
     */
	@Column(name = "update_by")
	private String updateBy;
    /**
     * 终端id
     */
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name = "mobile_id")
	private Mobile mobile;
    /**
     * 是否紧急联系人
     */
	@Column(name = "is_urgent")
	private Integer isUrgent;
    /**
     * 紧急联系人级别
     */
	@Column(name = "urgent_level")
	private Integer urgentLevel;
    /**
     * 是否能自动接听
     */
	@Column(name = "Is_auto_answer")
	private Integer IsAutoAnswer;
	
	@Transient
	private String simMark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}


	public Mobile getMobile() {
		return mobile;
	}

	public void setMobile(Mobile mobile) {
		this.mobile = mobile;
	}

	public Integer getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(Integer isUrgent) {
		this.isUrgent = isUrgent;
	}

	public Integer getUrgentLevel() {
		return urgentLevel;
	}

	public void setUrgentLevel(Integer urgentLevel) {
		this.urgentLevel = urgentLevel;
	}

	public Integer getIsAutoAnswer() {
		return IsAutoAnswer;
	}

	public void setIsAutoAnswer(Integer IsAutoAnswer) {
		this.IsAutoAnswer = IsAutoAnswer;
	}

	public String getSimMark() {
		if(mobile!=null)
			simMark=mobile.getSimMark();
		return simMark;
	}

	public void setSimMark(String simMark) {
		this.simMark = simMark;
	}
	

    public String notNullCheck()
    {
    	if(StringUtils.isBlank(contactName))
    	    return "请填写 联系人姓名";
    	
    	if(StringUtils.isBlank(contactPhone))
    	    return "请填写 联系人电话";
    	
    	if(isUrgent==null)
    	    return "请填写 是否紧急联系人";
    	
    	if(urgentLevel==null)
    	    return "请填写 紧急联系人级别";
    	
    	if(IsAutoAnswer==null)
    	    return "请填写 是否能自动接听";
    	
    	return null;
    }
}
