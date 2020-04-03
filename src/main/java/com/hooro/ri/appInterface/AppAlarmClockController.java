package com.hooro.ri.appInterface;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.model.AlarmClock;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.AlarmClockServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.util.PushNotiUtil;

/**
 * 闹钟
 *
 */
@RestController
public class AppAlarmClockController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private AlarmClockServiceImpl alarmClockService;

	@Autowired   private LoginInfoServiceImpl loginInfoService;
	
	@Autowired  MobileServiceImpl mobileService;
	
	/**
	 * 获取所有闹钟
	 * @return
	 */
	@RequestMapping(value="/getAlarmClockAllInterface")
	public Result getAlarmClockAll(String token)
	{
		try {
			
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");
			
			List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_mobile.simMark",appUser.getSimMark()));
			List<AlarmClock> contactsList=alarmClockService.findJoin(filters);
			return Result.successResult().setObj(contactsList);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}

	
	/**
	 * 获取指定ID闹铃
	 * @return
	 */
	@RequestMapping(value="/getAlarmClockByIdInterface")
	public Result getAlarmClockById(Integer id,String token)
	{
		try {
			if(id==null)
				return Result.warnResult().setMsg("id不能为空");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			AlarmClock contacts=alarmClockService.getById(id);
			return Result.successResult().setObj(contacts);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	/**
	 * 修改或新增闹铃
	 * @return
	 */
	@RequestMapping(value="/saveAlarmClockAllInterface")
	public Result saveAlarmClockAll(@ModelAttribute("AlarmClock") AlarmClock alarmClock,String token )
	{
		try {
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");
			
			
			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");
			
			String msg=alarmClock.notNullCheck();
			if(msg!=null)
				return Result.warnResult().setMsg(msg);
			
			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
			if(mobile==null)
				return Result.warnResult().setMsg("SimMark卡号不存在");
			
			alarmClock.setMobile(mobile);
			alarmClock.setUpdateBy(appUser.getAccount());
			alarmClockService.saveEntity(alarmClock);
			
			if(StringUtils.isNotBlank(mobile.getXgToken()))
			    PushNotiUtil.pushSingleMsg(mobile.getXgToken(), "{\"cmd\":\"remindUpdate\"}", null, 2, 1,1,"");
			return Result.successResult().setObj(alarmClock.getId());

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	/**
	 * 删除闹铃
	 * @return
	 */
	@RequestMapping(value="/delAlarmClockInterface")
	public Result getAlarmClock(Integer id,String token)
	{
		try {
			if(id==null)
				return Result.warnResult().setMsg("id不能为空");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			alarmClockService.deleteById(id);
			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
			if(mobile!=null&&StringUtils.isNotBlank(mobile.getXgToken()))
			    PushNotiUtil.pushSingleMsg(mobile.getXgToken(), "{\"cmd\":\"remindUpdate\"}", null, 2, 1,1,"");
			
			return Result.successResult().setObj("success");

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	
}
