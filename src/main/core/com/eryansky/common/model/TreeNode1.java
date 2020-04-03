/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.eryansky.common.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Maps;

/**
 * easyui树形节点TreeNode模型.
 * 
 * @author : 尔演&Eryan eryanwcp@gmail.com
 * @date : 2013-1-11 下午10:01:30
 */
@SuppressWarnings("serial")
public class TreeNode1 implements Serializable {
	/**
	 * 节点id
	 */
	private String id;
	/**
	 * 树节点名称
	 */
	private String text;
	/**
	 * 前面的小图标样式
	 */
	private String icon = "";
	/**
	 * 是否勾选状态（默认：否false）
	 */
	private Map<String, Object> state = Maps.newHashMap();
	/**
	 * 自定义属性
	 */
	private Map<String, Object> attributes = Maps.newHashMap();
	/**
	 * 子节点
	 */
	private List<TreeNode1> nodes;

	public TreeNode1() {

	}

	public TreeNode1(String id, String text, String icon, Map<String, Object> state, Map<String, Object> attributes,
			List<TreeNode1> nodes) {
		super();
		this.id = id;
		this.text = text;
		this.icon = icon;
		this.state = state;
		this.attributes = attributes;
		this.nodes = nodes;
	}

	
	public String getId() {
	
		return id;
	}

	
	public void setId(String id) {
	
		this.id = id;
	}

	
	public String getText() {
	
		return text;
	}

	
	public void setText(String text) {
	
		this.text = text;
	}

	
	public String getIcon() {
	
		return icon;
	}

	
	public void setIcon(String icon) {
	
		this.icon = icon;
	}

	
	public Map<String, Object> getState() {
	
		return state;
	}

	
	public void setState(Map<String, Object> state) {
	
		this.state = state;
	}

	
	public Map<String, Object> getAttributes() {
	
		return attributes;
	}

	
	public void setAttributes(Map<String, Object> attributes) {
	
		this.attributes = attributes;
	}

	
	public List<TreeNode1> getNodes() {
	
		return nodes;
	}

	
	public void setNodes(List<TreeNode1> nodes) {
	
		this.nodes = nodes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
