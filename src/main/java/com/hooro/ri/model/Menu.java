package com.hooro.ri.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

/**
 * 菜单表
 * @ClassName: Menu
 * @author ty
 * @date 2018-3-16 下午02:12:55
 */

@SuppressWarnings("serial")
@Entity
@Table(name="menu")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler","parentMenu","roles","subMenus"})
//@Cacheable
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
//标识是一个实体Bean
//表名
//设置springMvc转换Json 过滤字段、设置懒加载不设置这里还会出现1+N问题
//开启二级缓存
//设置读写级别缓存、可更改缓存级别
public class Menu implements Serializable {
	
	private Integer id; 
	/**
     * id
     */
	private String name; 
	/**
     * 菜单名
     */
	private String icon; 
	/**
     * 图标
     */
	private String url; 
	/**
     *  菜单url
     */
	private Integer status; 
	/**
     * 状态 0：启用 1：停用
     */
	private Integer type; 
	/**
     * 类型 0：pc 1：app
     */
	private Integer functionType; 
	/**
     * 功能类型 0：菜单 1：功能
     */
    private Menu parentMenu; 
	/**
     * 父级
     */
    private List<Role> roles = Lists.newArrayList(); 
	/**
     * 角色集合
     */
    private List<Menu> subMenus = Lists.newArrayList(); 
	/**
     * 父级id
     */
    private Integer _parentId; 
	/**
     * 父级name
     */
    private String _parentName; 
	/**
     * 排序字段
     */
    private Integer orderNo;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
	
		return id;
	}

	
	public void setId(Integer id) {
	
		this.id = id;
	}

	@Column(name = "name",length = 50)
	public String getName() {
	
		return name;
	}

	
	public void setName(String name) {
	
		this.name = name;
	}

	@Column(name = "icon",length = 50)
	public String getIcon() {
		return icon;
	}

	
	public void setIcon(String icon) {
	
		this.icon = icon;
	}

	@Column(name = "url",length = 50)
	public String getUrl() {
		return url;
	}

	
	public void setUrl(String url) {
	
		this.url = url;
	}

	@Column(name = "order_no")
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "status")
	public Integer getStatus() {
	
		return status;
	}

	
	public void setStatus(Integer status) {
	
		this.status = status;
	}
    
	@Column(name = "type")
	public Integer getType() {
	
		return type;
	}
	
	public void setType(Integer type) {
	
		this.type = type;
	}

	@Column(name = "function_type")
	public Integer getFunctionType() {
	
		return functionType;
	}


	public void setFunctionType(Integer functionType) {
	
		this.functionType = functionType;
	}


	@ManyToOne(cascade = { CascadeType.ALL },fetch=FetchType.LAZY)
    @JoinColumn(name = "parent_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public Menu getParentMenu() {
	
		return parentMenu;
	}
    
	public void setParentMenu(Menu parentMenu) {
	
		this.parentMenu = parentMenu;
	}
	
	@Transient
	public Integer get_parentId() {
        if (parentMenu != null) {
            _parentId = parentMenu.getId();
        }
		return _parentId;
	}

	
	public void set_parentId(Integer parentId) {
	
		_parentId = parentId;
	}
	
	@Transient
	public String get_parentName() {
        if (parentMenu != null) {
            _parentName = parentMenu.getName();
        }
		return _parentName;
	}


	
	public void set_parentName(String parentName) {
	
		_parentName = parentName;
	}


	@ManyToMany(cascade = {CascadeType.ALL},fetch=FetchType.LAZY,mappedBy="menus")
    //@JoinTable(name = "role_menu", joinColumns = {@JoinColumn(name = "menu_id")},inverseJoinColumns = {@JoinColumn(name = "role_id")})
	public List<Role> getRoles() {
	
		return roles;
	}

	
	public void setRoles(List<Role> roles) {
	
		this.roles = roles;
	}

	@OneToMany(mappedBy = "parentMenu",cascade = {CascadeType.REMOVE})
	@NotFound(action=NotFoundAction.IGNORE)
	public List<Menu> getSubMenus() {
	
		return subMenus;
	}

	public void setSubMenus(List<Menu> subMenus) {
	
		this.subMenus = subMenus;
	}
	
public static void main(String[] args) {
	String str="<i class='Hui-iconfont'>&#xe640;</i>";
	System.out.println(str.replaceAll("&","&amp").replaceAll("<","&lt").replaceAll(">","&gt"));
	//return icon.replace(/&/g, '&amp').replace(/</g, '&lt').replace(/>/g, '&gt');
}
	
}
