package com.hooro.ri.util;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;


/** 
 * @Description: TODO(菜单树) 
 * @author ty 
 * @date 2018-3-19 下午4:08:36 
 *  
 */
@SuppressWarnings("serial")
public class MenuTreeNode  implements Serializable{

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
		 * 图标
		 */
		private String icon;
		/**
		 * 子节点
		 */
		private List<MenuTreeNode> childMenus;




		public MenuTreeNode(String id, String name, String order,String icon) {

			super();
			this.id = id;
			this.name = name;
			this.order = order;
			this.icon=icon;
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




		public List<MenuTreeNode> getChildMenus() {

			return childMenus;
		}




		public void setChildMenus(List<MenuTreeNode> childMenus) {

			this.childMenus = childMenus;
		}


		
		

		public String getIcon() {
			return icon;
		}




		public void setIcon(String icon) {
			this.icon = icon;
		}




		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}

}
