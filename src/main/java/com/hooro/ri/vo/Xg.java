package com.hooro.ri.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Xg implements Serializable {
	private String xgToken;
	private Integer xgType;
	public String getXgToken() {
		return xgToken;
	}
	public void setXgToken(String xgToken) {
		this.xgToken = xgToken;
	}
	public Integer getXgType() {
		return xgType;
	}
	public void setXgType(Integer xgType) {
		this.xgType = xgType;
	}

}
