package com.hooro.ri.controller;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eryansky.common.exception.ActionException;
import com.eryansky.common.model.Result;
import com.eryansky.common.model.SelectType;
import com.eryansky.common.model.TreeNode1;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.utils.StringUtils;
import com.eryansky.common.utils.collections.Collections3;
import com.eryansky.common.web.springmvc.BaseController;
import com.google.common.collect.Lists;
import com.hooro.ri.model.Menu;
import com.hooro.ri.service.impl.MenuServiceImpl;


/** 
* @ClassName: BrowseController 
* @Description: TODO(菜单Controller) 
* @author ty 
* @date 2018-3-16 下午02:12:55
*  
*/
@Controller
@RequestMapping(value = "menu")
public class MenuController extends BaseController<Menu, Integer> {
	
	@Autowired   private MenuServiceImpl menuService;
	@Override
	public EntityManager<Menu, Integer> getEntityManager() {
		return menuService;
	}
	 
	/**
	 * 菜单列表 页面
	 */
	@RequestMapping(value = {""})
	public String list() {
		return "menu";
	}
	
	/**
	 * 编辑菜单 页面
	 */
	@RequestMapping(value = { "input" })
	public String input(@ModelAttribute("model") Menu menu,Model model) throws Exception {
		model.addAttribute("menus",menuService.getAllParentMenu());
		return "menu_input";
	}
	
	/**
	 * 设置功能 页面
	 */
	@RequestMapping(value = { "function" })
	public String function(@ModelAttribute("model") Menu menu) throws Exception {
		return "menu_function";
	}
	
	/**
	 * 设置功能 编辑 页面
	 */
	@RequestMapping(value = { "function_input" })
	public String functionInput(Model model, @ModelAttribute("model") Menu menu, Integer parentId) throws Exception {
		model.addAttribute("parentId", parentId);
		return "menu_function_input";
	}
	
	/**
	 * 保存
	 */
    @RequestMapping(value = {"_save"})
    @ResponseBody
    public Result save(@ModelAttribute("model") Menu menu,Integer _parentId)  {
        menuService.evict(menu);
        Result result = null;
        menu.setParentMenu(null);
        // 设置上级节点
        if (_parentId != null) {
            Menu parentMenu = menuService.getById(_parentId);
            if (parentMenu == null) {
                throw new ActionException("父级菜单已被删除.");
            }
            menu.setParentMenu(parentMenu);
        }

        if (menu.getId() != null) {
            if (menu.getId().equals(menu.get_parentId())) {
                result = new Result(Result.ERROR, "[上级菜单]不能与[菜单名称]相同.",null);
                return result;
            }
        }
        menuService.merge(menu);
        result = Result.successResult();
        return result;
    }
	 
	/**
	 * 父级下拉框
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "parentMenu" })
	@ResponseBody
	public List<TreeNode1> parentMenu(@ModelAttribute("model") Menu menu, String selectType) {
		List<TreeNode1> treeNodes = null;
		List<TreeNode1> titleList = Lists.newArrayList();
		// 添加 "---全部---"、"---请选择---"
		if (!StringUtils.isBlank(selectType)) {
			SelectType s = SelectType.getSelectTypeValue(selectType);
			if (s != null) {
				TreeNode1 selectTreeNode = new TreeNode1("", s.getDescription(),null,null,null,null);
				titleList.add(selectTreeNode);
			}
		}
		treeNodes = menuService.getMenuTree();
		for(TreeNode1 entiy : treeNodes){
			entiy.setNodes(null);
		}
		List<TreeNode1> unionList = ListUtils.union(titleList, treeNodes);
		return unionList;
	}
	 
    /**
     * 删除
     */
    @RequestMapping(value = {"_remove"})
    @ResponseBody
    public Result remove(@RequestParam(value = "ids", required = false) List<Integer> ids) {
        Result result;
        if(!Collections3.isEmpty(ids)){
            menuService.deleteByIds(ids);
            result = Result.successResult();
        }else{
            result = new Result().setCode(Result.WARN).setMsg("参数[id]为空.");
        }
        
        result = Result.successResult();
        return result;

	}
}
