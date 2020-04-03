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
import com.hooro.ri.model.AlarmClock;
import com.hooro.ri.service.impl.AlarmClockServiceImpl;

/** 
* @ClassName: Controller 
* @Description: TODO(Controller) 
* @author ty 
* @date 2018-3-16 下午02:12:55 
*  
*/
@Controller
@RequestMapping(value = "alarmClock")
public class BackAlarmClockController extends BaseController<AlarmClock, Integer> {
	
	@Autowired  private AlarmClockServiceImpl alarmClockService;
	@Override
	public EntityManager<AlarmClock, Integer> getEntityManager() {
		 return alarmClockService;
	}
	 
	/**
	 * 列表 页面
	 */
	@RequestMapping(value = {""})
	public String list() {
		return "alarmClock";
	}
	 
	/**
	 * 编辑 页面
	 */
	@RequestMapping(value = { "input" })
	public String input(@ModelAttribute("model") AlarmClock alarmClock) throws Exception {
		return "alarmClock_input";
	}


	

}
