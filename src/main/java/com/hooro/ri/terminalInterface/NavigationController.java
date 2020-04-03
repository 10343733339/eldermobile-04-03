package com.hooro.ri.terminalInterface;

import java.util.List;
import java.util.Map;

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
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.AppUserMobile;
import com.hooro.ri.model.Enclosure;
import com.hooro.ri.model.LoginInfo;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.model.Navigation;
import com.hooro.ri.service.impl.AppUserMobileServiceImpl;
import com.hooro.ri.service.impl.EnclosureServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.service.impl.NavigationServiceImpl;
import com.hooro.ri.util.MapUtils;
import com.hooro.ri.util.NavigationUtil;
import com.hooro.ri.util.PushNotiUtil;
import com.hooro.ri.vo.Xg;

import jersey.repackaged.com.google.common.collect.Lists;
import net.sf.json.JSONObject;

/**
 * 上传轨迹
 *
 */
@RestController
public class NavigationController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private MobileServiceImpl mobileService;
	@Autowired   private NavigationServiceImpl navigationService;
	@Autowired   private EnclosureServiceImpl enclosureService;
	@Autowired   private LoginInfoServiceImpl loginInfoService;
	@Autowired   private AppUserMobileServiceImpl appUserMobileService;
	
	
	
	
	
	/**
	 * 跌倒报警
	 * @return
	 */
	@RequestMapping(value="/testInterface")
	public Object fallAlarm11()
	{
		
		List<Object> xgList=loginInfoService.getXgTokenByMobileId(5);
			for(Object object:xgList)
	        	{
				Object[] obj=(Object[]) object;
				//Object[] xg=(Object[]) object;
				System.out.println(obj[0].toString()+"=="+obj[1]);
				PushNotiUtil.pushSingleMsg(obj[0].toString(), "{\"status\":\"fallAlarm\"}", null, 2, 2,(Integer)obj[1],"跌倒告警");
	        	}
			return xgList;

	}
	

	/**
	 * 监控就绪
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/monitoringReadinessInterface",produces="application/json;charset=UTF-8",method=RequestMethod.POST)
	public Result monitoringReadiness(@RequestBody String json)
	{
		try {
			
			JSONObject  jasonObject = JSONObject.fromObject(json);
			Map<String,String> map = (Map<String,String>)jasonObject;

			if(StringUtils.isBlank(map.get("token")))
				return Result.warnResult().setMsg("请填写token");
			
			if(StringUtils.isBlank(map.get("status")))
				return Result.warnResult().setMsg("请填写状态");
			

			
			AppUser appUser=loginInfoService.getAppUserByToken(map.get("token"));
			if(appUser==null)
			    return Result.warnResult().setMsg("请求监控账号已退出登录，监控取消");
			
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请求账号已切换到其他终端，监控取消");
			
			LoginInfo loginInfo=loginInfoService.getLoginInfoByaccount(appUser.getId());
	        	PushNotiUtil.pushSingleMsg(loginInfo.getXg_token(), "{\"cmd\":\"readyMonitor\",\"status\":\""+map.get("status")+"\"}", null, 2, 2,loginInfo.getXgType(),"");
		    return Result.successResult().setMsg("success");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	
	
	
	
	/**
	 * 低电量警告
	 * @return
	 */
	@RequestMapping(value="/lowElectricQuantityInterface")
	public Result lowElectricQuantity(String simMark)
	{
		try {
			

			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写simMark");
			
			
			Mobile mobile=mobileService.findUniqueBy("simMark",simMark);
			if(mobile==null)
				return Result.warnResult().setMsg("设备不存在，请先注册");
			
			
		    List<PropertyFilter> filters=Lists.newArrayList();
		    filters.add(new PropertyFilter("EQI_isAdmin","1"));
		    filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			List<AppUserMobile> appUserMobileList=appUserMobileService.find(filters);
			if(appUserMobileList!=null&&appUserMobileList.size()!=0)
			{
				AppUser appUser=appUserMobileList.get(0).getAppUser();
				LoginInfo loginfo=loginInfoService.getLoginInfoByaccount(appUser.getId());
				if(loginfo!=null)
				   PushNotiUtil.pushSingleMsg(loginfo.getXg_token(), "{\"cmd\":\"lowElectricQuantity\"}", null, 2, 2,loginfo.getXgType(),"终端低电量");
			}
				
		    return Result.successResult().setMsg("success");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	
		
}
