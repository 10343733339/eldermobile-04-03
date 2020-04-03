package com.hooro.ri.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.eryansky.common.utils.ConvertUtils;
import com.eryansky.common.utils.collections.Collections3;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

/**
 * 角色表
 * @ClassName: Role
 * @author ty
 * @date 2018-3-16 下午02:12:55
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "role")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler","menus","menuIds"})
//@Cacheable
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
//标识是一个实体Bean
//表名
//设置springMvc转换Json 过滤字段、设置懒加载不设置这里还会出现1+N问题
//开启二级缓存
//设置读写级别缓存、可更改缓存级别
public class Role implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id; 
	/**
     * 角色名
     */
	@Column(name = "name",length = 50)
	private String name; 
	/**
     * 角色编码
     */
	@Column(name = "code",length = 50)
	private String code;  
	/**
     * 备注
     */
	@Column(name = "remark",length = 50)
	private String remark;  
	/**
     * 状态
     */
	@Column(name = "status")
	private Integer status; 
	/**
     * 关联菜单
     */
	//有树形结构不能使用懒加载，会发生递归树出现连接关闭情况
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
    @JoinTable(name = "role_menu", joinColumns = { @JoinColumn(name= "role_id") }, inverseJoinColumns = { @JoinColumn(name = "menu_id") })
    @NotFound(action=NotFoundAction.IGNORE)
    private List<Menu> menus = Lists.newArrayList();  
	/**
     * 关联菜单id集合 
     */
	@Transient
    private List<Integer> menuIds = Lists.newArrayList(); 

	public Role() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
	
		return name;
	}

	
	public void setName(String name) {
	
		this.name = name;
	}


	public String getCode() {
	
		return code;
	}

	
	public void setCode(String code) {
	
		this.code = code;
	}


	public String getRemark() {
	
		return remark;
	}

	
	public void setRemark(String remark) {
	
		this.remark = remark;
	}

	public Integer getStatus() {
	
		return status;
	}

	
	public void setStatus(Integer status) {
	
		this.status = status;
	}


	public List<Menu> getMenus() {
	
		return menus;
	}
    
	
	public void setMenus(List<Menu> menus) {
	
		this.menus = menus;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getMenuIds() {
		
        if (!Collections3.isEmpty(menus)) {
        	menuIds = ConvertUtils.convertElementPropertyToList(menus, "id");
        }
		return menuIds;
	}
	
	public void setMenuIds(List<Integer> menuIds) {
	
		this.menuIds = menuIds;
	}

    /** 
    * <p>Title:__getFcuntionMenu </p> 
    * <p>Description: 获取按钮菜单</p> 
    * @return 
    */
   /* public List<Menu> __getFcuntionMenu(Integer parentId)
    {
    	List<Menu> funcitonMenus=new ArrayList<Menu>();
    	for(Menu menu:menus)
    		{
    		if(menu.getFunctionType()==1&&menu.get_parentId().equals(parentId))
    			{
    			funcitonMenus.add(menu);
    			}
    		}
    	return funcitonMenus;
    }*/


}