package com.hooro.ri.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.web.springmvc.BaseController;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.MobileServiceImpl;

/** 
* @ClassName: Controller 
* @Description: TODO(Controller) 
* @author ty 
* @date 2018-3-16 下午02:12:55 
*  
*/
@Controller
@RequestMapping(value = "mobile")
public class BackMobileSController extends BaseController<Mobile, Integer> {
	
	@Autowired  private MobileServiceImpl mobileService;
	@Override
	public EntityManager<Mobile, Integer> getEntityManager() {
		 return mobileService;
	}
	 
	/**
	 * 列表 页面
	 */
	@RequestMapping(value = {""})
	public String list() {
		return "mobile";
	}
	 
	/**
	 * 编辑 页面
	 */
	@RequestMapping(value = { "input" })
	public String input(@ModelAttribute("model") Mobile Mobile) throws Exception {
		return "mobile_input";
	}



}
