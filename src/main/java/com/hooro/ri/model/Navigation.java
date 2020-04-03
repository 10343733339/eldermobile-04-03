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
public class Navigation implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
    /**
     * 经度
     */
	private Double longitude;
    /**
     * 纬度
     */
	private Double latitude;
    /**
     * 定位类型
     */
	private String type;
    /**
     * 终端
     */
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name = "mobile_id")
	private Mobile mobile;
    /**
     * 创建时间
     */
	@Column(name = "create_time",updatable=false)
	private Date createTime=new Date();
	
	
	@Transient
	private String simMark;

	/**
	 * 电量
	 */
	@Column(name = "electric_quantity")
	private Integer electricQuantity;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Mobile getMobile() {
		return mobile;
	}
	public void setMobile(Mobile mobile) {
		this.mobile = mobile;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSimMark() {
		if(mobile!=null)
			simMark=mobile.getSimMark();
		return simMark;
	}
	public void setSimMark(String simMark) {
		this.simMark = simMark;
	}




}
