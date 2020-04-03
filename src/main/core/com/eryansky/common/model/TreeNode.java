/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.eryansky.common.model;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.io.Serializable;
import java.util.List;

/**
 * easyui树形节点TreeNode模型.
 * 
 * @author : 尔演&Eryan eryanwcp@gmail.com
 * @date : 2013-1-11 下午10:01:30
 */
@SuppressWarnings("serial")
public class TreeNode implements Serializable {

	/**
	 * 节点id
	 */
	private String id;
	/**
	 * 树节点名称
	 */
	private String name;
    /**
     * 父级节点id
     */
    private String parentId;
    /**
     * url
     */
    private String url;
    /**
     * 排序
     */
    private String order;
	/**
	 * 子节点
	 */
	private List<TreeNode> childMenus;

	
	
	
	public TreeNode(String id, String name, String order) {

		super();
		this.id = id;
		this.name = name;
		this.order = order;
	}




	public String getId() {
	
		return id;
	}



	
	public void setId(String id) {
	
		this.id = id;
	}



	
	public String getName() {
	
		return name;
	}



	
	public void setName(String name) {
	
		this.name = name;
	}



	
	public String getParentId() {
	
		return parentId;
	}



	
	public void setParentId(String parentId) {
	
		this.parentId = parentId;
	}



	
	public String getUrl() {
	
		return url;
	}



	
	public void setUrl(String url) {
	
		this.url = url;
	}



	
	public String getOrder() {
	
		return order;
	}



	
	public void setOrder(String order) {
	
		this.order = order;
	}



	
	public List<TreeNode> getChildMenus() {
	
		return childMenus;
	}



	
	public void setChildMenus(List<TreeNode> childMenus) {
	
		this.childMenus = childMenus;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
