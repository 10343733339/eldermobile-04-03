package com.hooro.ri.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eryansky.common.model.Combobox;
import com.eryansky.common.model.Result;
import com.eryansky.common.model.SelectType;
import com.eryansky.common.model.TreeNode1;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.utils.StringUtils;
import com.eryansky.common.utils.mapper.JsonMapper;
import com.eryansky.common.web.springmvc.BaseController;
import com.google.common.collect.Lists;
import com.hooro.ri.model.Role;
import com.hooro.ri.service.impl.RoleServiceImpl;

/** 
* @ClassName: BrowseController 
* @Description: TODO(角色Controller) 
* @author ty 
* @date 2018-3-16 下午02:12:55 
*  
*/
@Controller
@RequestMapping(value = "role")
public class RoleController extends BaseController<Role, Integer> {
	
	@Autowired  private RoleServiceImpl roleService;
	@Override
	public EntityManager<Role, Integer> getEntityManager() {
		 return roleService;
	}
	 
	/**
	 * 角色列表 页面
	 */
	@RequestMapping(value = {""})
	public String list() {
		return "role";
	}
	 
	/**
	 * 编辑角色 页面
	 */
	@RequestMapping(value = { "input" })
	public String input(@ModelAttribute("model") Role role) throws Exception {
		return "role_input";
	}
	
	/**
	 * 保存
	 */
	@RequestMapping(value = { "save" })
	@ResponseBody
	public Result save(@ModelAttribute("model") Role role) {
		//roleService.evict(role);
		Result result = null;
		// 名称重复校验
		Role nameCheckRole = roleService.getRoleByName(role.getName());
		if (nameCheckRole != null && !nameCheckRole.getId().equals(role.getId())) {
			result = new Result(Result.WARN, "角色名为[" + role.getName() + "]已存在,请修正!", "name");
			return result;
		}
		roleService.saveEntity(role);
		result = Result.successResult();
		return result;
	}
	 
	/**
	 * 设置角色菜单 页面
	 */
    @RequestMapping(value = {"menu"})
    public String menu(Model model,Integer id) throws Exception {
    	List<TreeNode1> treeNodes=roleService.getMenuRole(id);
        String resourceComboboxData = JsonMapper.nonDefaultMapper().toJson(treeNodes);
        model.addAttribute("menuComboboxData", resourceComboboxData.replaceAll(",\"nodes\":\\[\\]", ""));
        return "role_menu";
    }
	 
	/**
	 * 设置角色菜单
	 */
	@RequestMapping(value = { "updateRoleMenu" })
	@ResponseBody
	public Result updateRoleMenu(@ModelAttribute("role") Role role,String ids) {
		Result result;
		roleService.updateRoleMenu(role,ids);
		result = Result.successResult();
		return result;
	}
	
	/**
	 * 角色下拉框
	 */
	@RequestMapping(value = { "combobox" })
	@ResponseBody
	public List<Combobox> combobox(String selectType) throws Exception {
		List<Role> list = roleService.findBy("status",1);
		List<Combobox> cList = Lists.newArrayList();

		// 为combobox添加 "---全部---"、"---请选择---"
		if (!StringUtils.isBlank(selectType)) {
			SelectType s = SelectType.getSelectTypeValue(selectType);
			if (s != null) {
				Combobox selectCombobox = new Combobox("", s.getDescription());
				cList.add(selectCombobox);
			}
		}
		for (Role r : list) {
			Combobox combobox = new Combobox(r.getId() + "", r.getName());
			cList.add(combobox);
		}
		return cList;
	}

}
