package com.hooro.ri.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.exception.DaoException;
import com.eryansky.common.exception.ServiceException;
import com.eryansky.common.exception.SystemException;
import com.eryansky.common.model.TreeNode1;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.eryansky.common.orm.hibernate.Parameter;
import com.eryansky.common.utils.StringUtils;
import com.eryansky.common.utils.collections.Collections3;
import com.google.common.collect.Lists;
import com.hooro.ri.model.Menu;
import com.hooro.ri.model.Role;
import com.hooro.ri.model.User;
import com.hooro.ri.util.MenuTreeNode;

/** 
* @ClassName: MenuServiceImpl 
* @Description: TODO(菜单管理 业务类) 
* @author ty 
* @date 2018-3-16 下午02:12:55 
*  
*/
@Service("menuService")
public class MenuServiceImpl extends EntityManager<Menu, Integer> {
	
	private HibernateDao<Menu, Integer> menuDao;
	
	@Autowired
	private UserServiceImpl userService;
	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	menuDao = new HibernateDao<Menu, Integer>(sessionFactory, Menu.class);
	}
	
	@Override
	protected HibernateDao<Menu, Integer> getEntityDao() {
		return menuDao;
	}

	/**
	 * 根据登录用户获取菜单树
	 */
	public List<MenuTreeNode> getNavMenuTreeByUserId(Integer id) throws DaoException,SystemException, ServiceException {
        List<MenuTreeNode> nodes = Lists.newArrayList();
        List<Menu> userMenus = Lists.newArrayList();
        User user = userService.getById(id);
        if (user != null && id==1) { // 超级管理员
        	userMenus = this.getByParentId(null,1);
        } else if (user != null) {
        	userMenus = this.getMenusByUserId(id);
        }
        
        for(Menu menu : userMenus){
            if(user != null && id==1){// 超级管理员
            	MenuTreeNode node =  this.menuToTreeNode(menu);
                if(node !=null){
                    nodes.add(node);
                }
            } else{
                if(menu != null && menu.getParentMenu() == null){
                	MenuTreeNode node =  this.menuToTreeNode(userMenus,menu);
                    if(node !=null){
                        nodes.add(node);
                    }
                }
            }
        }
        return nodes;
	}
	
	/**
	 * 根据用户id获取菜单列表
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getMenusByUserId(Integer userId) throws DaoException, SystemException, ServiceException {

		Parameter parameter = new Parameter(userId, 1);
		// 角色权限
		List<Menu> roleMenus = menuDao.distinct(menuDao.createQuery("select ms from User u left join u.role rs left join rs.menus ms where u.id= :p1 and ms.status = :p2  order by ms.orderNo desc",parameter)).setCacheable(true).list();
		// 去除空对象
		Iterator<Menu> roleIterator = roleMenus.iterator();
		while (roleIterator.hasNext()) {
			Menu rolemenu = roleIterator.next();
			if (rolemenu == null) {
				roleIterator.remove();
			}
		}
		return roleMenus;
	}
	
	/**
	 * 根据父级id和状态获取菜单列表
	 */
	public List<Menu> getByParentId(Integer parentId, Integer status) throws DaoException, SystemException, ServiceException {
		if(status == null){
			status = 1;
		}
		StringBuilder hql = new StringBuilder();
		Parameter parameter = new Parameter();
		hql.append("from Menu r where r.status = :status "); 
		parameter.put("status",status);
		hql.append(" and r.parentMenu");
		if (parentId == null) {
			hql.append(" is null ");
		} else {
			hql.append(".id = :parentId ");
		    parameter.put("parentId",parentId);
		}
		hql.append(" order by r.orderNo desc");
		List<Menu> list = getEntityDao().find(hql.toString(), parameter);
		return list;
	}
	
	/**
	 * 菜单表转为树
	 */
    private MenuTreeNode menuToTreeNode(Menu menu) throws DaoException,SystemException,ServiceException {
    	MenuTreeNode treeNode = new MenuTreeNode(menu.getId().toString(),menu.getName(),null,menu.getIcon());
    	if(menu.getParentMenu()==null){
    		treeNode.setParentId("0");
    		treeNode.setUrl("");
    	}else{
    		treeNode.setParentId(menu.getParentMenu().getId().toString());
    		treeNode.setUrl(menu.getUrl());
    	}

		List<MenuTreeNode> childrenTreeNodes = Lists.newArrayList();
		for(Menu subMenu:menu.getSubMenus()){
			if(subMenu.getType()==0&&subMenu.getFunctionType()==0){
				MenuTreeNode node = menuToTreeNode(subMenu);
				if(node !=null){
					childrenTreeNodes.add(node);
		    	}
			}
		}
		treeNode.setChildMenus(childrenTreeNodes);

		return treeNode;
    }
    
    /**
     * 获取所有一级菜单
     */
    public List<TreeNode1> getMenuTree() {
        List<TreeNode1> treeNodes = Lists.newArrayList();
        List<Menu> menus = getByParentId(null,1);
        for (Menu rs:menus) {
            TreeNode1 rootNode = getTreeNode(rs);
            treeNodes.add(rootNode);
        }
        return treeNodes;
    }
    
    /**
     * 根据菜单获取树（包括子集）
     */
	public TreeNode1 getTreeNode(Menu entity) {
		TreeNode1 node = this.menuToTreeNode1(entity);
		List<Menu> subResources = this.getByParentId(entity.getId(), 1);
		
        List<TreeNode1> children = Lists.newArrayList();
        for (Menu d : subResources) {
            TreeNode1 treeNode = null;
            treeNode = getTreeNode(d);
            children.add(treeNode);
        }
        node.setNodes(children);
        
		return node;
	}
	
	/**
	 * 菜单表转为树
	 */
	private TreeNode1 menuToTreeNode1(Menu menu) {
		
		TreeNode1 treeNode = new TreeNode1(menu.getId().toString(),menu.getName(), null,null,null,null);
		// 自定义属性 url
		/*Map<String, Object> attributes = Maps.newHashMap();
		attributes.put("url", menu.getUrl());
		attributes.put("code", menu.getCode());
		treeNode.setAttributes(attributes);*/
		
		List<TreeNode1> childrenTreeNodes = Lists.newArrayList();
		for (Menu subMenu : menu.getSubMenus()) {
			TreeNode1 node = menuToTreeNode1(subMenu);
			if (node != null) {
				childrenTreeNodes.add(node);
			}
		}
		treeNode.setNodes(childrenTreeNodes);
		return treeNode;
	}
	
	/**
	 * 菜单表转为树
	 */
	private MenuTreeNode menuToTreeNode(List<Menu> repositoryMenus, Menu menu) throws DaoException, SystemException, ServiceException {
		if (menu == null || !repositoryMenus.contains(menu)) {
			return null;
		}
		MenuTreeNode treeNode = new MenuTreeNode(menu.getId().toString(),menu.getName(),null,menu.getIcon());
    	if(menu.getParentMenu()==null){
    		treeNode.setParentId("0");
    		treeNode.setUrl("");
    		treeNode.setIcon(menu.getIcon());
    	}else{
    		treeNode.setParentId(menu.getParentMenu().getId().toString());
    		treeNode.setUrl(menu.getUrl());
    	}

		List<MenuTreeNode> childrenTreeNodes = Lists.newArrayList();
		for (Menu subMenu : menu.getSubMenus()) {
			if(subMenu.getType()==0&&subMenu.getFunctionType()==0){
				MenuTreeNode node = menuToTreeNode(repositoryMenus, subMenu);
				if (node != null) {
					childrenTreeNodes.add(node);
				}
			}
		}
		treeNode.setChildMenus(childrenTreeNodes);
		return treeNode;
	}
	
    /**
     * 根据请求地址判断用户是否有权访问该url.
     */
    public boolean isAuthority(String requestUrl,Integer userId)
            throws DaoException,SystemException,ServiceException{
        //如果是超级管理员 直接允许被授权
        if(userId==1) {
            return true;
        }
        //检查该URL是否需要拦截
        boolean isInterceptorUrl = this.isInterceptorUrl(requestUrl);
        if (isInterceptorUrl){
            //用户权限
        	User user = userService.getById(userId);
        	List<String> userAuthoritys = Lists.newArrayList();
        	if(user.getRole()!=null){
        		if(!Collections3.isEmpty(user.getRole().getMenus())){
        			for(Menu menu : user.getRole().getMenus()){
        				if(menu.getType()==0&&menu.getFunctionType()==0){
        					userAuthoritys.add(menu.getUrl());
        				}
        			}
        		}
        	}
            for(String markUrl :userAuthoritys){
            	if(StringUtils.isNotBlank(markUrl)&&markUrl.equals(requestUrl))
            		return  true;
            }
            return false;
        }
        return true;
    }
    
    /**
     * 检查某个URL是都需要拦截.
     */
    public boolean isInterceptorUrl(String requestUrl)
            throws DaoException,SystemException,ServiceException{
        List<String> markUrlList = this.getAllInterceptorUrls();
        for(String markUrl :markUrlList){
            if(markUrl.equals(requestUrl))
            	return  true;
        }
        return false;
    }
    
    /**
     * 查找需要拦截的所有url规则.
     */
    @SuppressWarnings("unchecked")
	public List<String> getAllInterceptorUrls()
            throws DaoException,SystemException,ServiceException{
        List<String> markUrls = Lists.newArrayList();
        Parameter parameter = new Parameter();
        List<Menu> menus = getEntityDao().createQuery("from Menu m where m.type = 0 and m.functionType = 0", parameter).list();
        for(Menu menu : menus){
            if(StringUtils.isNotBlank(menu.getUrl())){
                markUrls.add(menu.getUrl());
            }
        }
        return markUrls;
    }
    /**
     * 根据父ID获取按钮组
     */
	@SuppressWarnings({"unchecked" })
	public List<Menu> getButtonUrl(Integer parentId,Integer userId){
		if(userId!=1)
		{
			User user=userService.getById(userId);
			Role role=user.getRole();
			List<Menu> funcitonMenus=new ArrayList<Menu>();
	    	for(Menu menu:role.getMenus())
	    		{
	    		if(menu.getFunctionType()==1&&menu.get_parentId().equals(parentId))
	    			{
	    			funcitonMenus.add(menu);
	    			}
	    		}
			return  funcitonMenus;
		}
        String sql="select * from  menu  where `status`=1  and parent_id=:p1 and function_type=1";
		List<Menu> ssList= getEntityDao().createSqlQuery(sql, new Parameter(parentId)).addEntity(Menu.class).setCacheable(true).list();
        return ssList;
    }
	/**
     * 获取所有一级菜单
     */
    @SuppressWarnings("unchecked")
	public List<Menu> getAllParentMenu() 
	{
        String sql="select  *  from  menu where parent_id is NULL";
        List<Menu> menuList= getEntityDao().createSqlQuery(sql, null).addEntity(Menu.class).setCacheable(true).list();
        return menuList;
    }

}
