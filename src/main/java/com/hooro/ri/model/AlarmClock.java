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
import com.hooro.ri.util.FieldHeader;


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
@Table(name="alarm_clock")
public class AlarmClock implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
    /**
     * 事件名称
     */
	@Column(name = "event_name")
	private String eventName;
    /**
     * 终端ID
     */
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name = "mobile_id")
	private Mobile mobile;
    /**
     * 重复提醒
     */
	@Column(name = "time_interval")
	private String timeInterval;
    /**
     * 最后修改人账号
     */
	@Column(name = "update_by")
	private String updateBy;
    /**
     * 事件内容
     */
	@Column(name = "event_content")
	private String eventContent;
    /**
     * 是否启用
     */
	private Integer status;
    /**
     * 提醒时间
     */
	@Column(name = "event_time")
	private String eventTime;

	@Transient
	private String simMark;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}



	public Mobile getMobile() {
		return mobile;
	}

	public void setMobile(Mobile mobile) {
		this.mobile = mobile;
	}

	public String getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getSimMark() {
		if(mobile!=null)
			simMark=mobile.getSimMark();
		return simMark;
	}

	public void setSimMark(String simMark) {
		this.simMark = simMark;
	}

	public String notNullCheck() {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(eventName))
    	    return "请填写  事件名称";
    	
    	if(StringUtils.isBlank(timeInterval))
    	    return "请填写  是否重复提醒";
    	
    	if(StringUtils.isBlank(eventContent))
    	    return "请填写  事件内容";
    	
    	if(StringUtils.isBlank(eventTime))
    	    return "请填写  事件内容";
    	
    	if(status==null)
    	    return "请填写 是否启用";
    	
    	return null;
	}





}
