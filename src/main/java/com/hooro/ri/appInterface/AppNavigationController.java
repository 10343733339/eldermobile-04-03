package com.hooro.ri.appInterface;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.model.Navigation;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.service.impl.NavigationServiceImpl;

import net.sf.json.JSONObject;

/**
 * 轨迹
 *
 */
@RestController
public class AppNavigationController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private MobileServiceImpl mobileService;
	@Autowired   private NavigationServiceImpl navigationService;
	@Autowired   private LoginInfoServiceImpl loginInfoService;

	/**
	 * 获取当天轨迹
	 * @return
	 *//*
	@RequestMapping(value="/getNavigationSameDayInterface")
	public Result getNavigationSameDay(String token)
	{
		try {
			
			List<Navigation> navigation=navigationService.getNavigationSameDay();
		    return Result.successResult().setMsg("success").setObj(navigation);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}*/
	
	
	/**
	 * 获取轨迹
	 * @return
	 */
	@RequestMapping(value="/getNavigationByMaxMinTimeInterface")
	public Result getNavigationByMaxMinTime(String token,String startTime,String endTime)
	{
		try {
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");
			
			if(StringUtils.isBlank(startTime))
				return Result.warnResult().setMsg("请填写startTime");
			
			if(StringUtils.isBlank(endTime))
				return Result.warnResult().setMsg("请填写endTime");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

          
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");

			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
			
			
			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			filters.add(new PropertyFilter("GED_createTime",startTime));
			filters.add(new PropertyFilter("LED_createTime",endTime));
			List<Navigation> navigation=navigationService.find(filters);
		    return Result.successResult().setMsg("success").setObj(navigation);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
}
