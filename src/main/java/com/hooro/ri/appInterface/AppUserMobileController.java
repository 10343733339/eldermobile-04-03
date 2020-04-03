package com.hooro.ri.appInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.Page;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.config.netty.NettyServerHandler;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.AppUserMobile;
import com.hooro.ri.model.MessageBoard;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.model.Navigation;
import com.hooro.ri.model.vo.AppUserMobileVo;
import com.hooro.ri.service.impl.AppUserMobileServiceImpl;
import com.hooro.ri.service.impl.AppUserServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MessageBoardServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.service.impl.NavigationServiceImpl;
import com.hooro.ri.util.OnLlineUtil;
import com.hooro.ri.util.SMSUtil;

import jersey.repackaged.com.google.common.collect.Lists;
import net.sf.json.JSONObject;

/**
 * app接口
 *
 */
@RestController
public class AppUserMobileController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private AppUserServiceImpl appUserService;
	@Autowired   private LoginInfoServiceImpl loginInfoService;
	@Autowired   private MobileServiceImpl mobileService;
	@Autowired   private AppUserMobileServiceImpl appUserMobileService;
	@Autowired   private MessageBoardServiceImpl  messageBoardService;
	@Autowired   private NavigationServiceImpl  navigationService;
	/**
	 * 绑定设备
	 */
	@RequestMapping(value="bindingMobileInterface")
	public Result bindingMobile(String simMark,String token,String mobileName) 
	{
		try
		{
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写simMark");

			if(StringUtils.isBlank(mobileName))
				return Result.warnResult().setMsg("请填写mobileName,设备名称");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)
				return Result.warnResult().setMsg("SimMark卡号不存在");

			List<AppUserMobile>  appUserMobileList =appUserMobileService.findBy("mobile.id", mobile.getId());
			AppUserMobile appUserMobile=new AppUserMobile();
			appUserMobile.setAppUser(appUser);
			appUserMobile.setMobile(mobile);
			appUserMobile.setMobileName(mobileName);
			appUserMobile.setIsExamine(1);
			appUserMobile.setIsAutoAnswer(0);
			
			//验证是否已经添加过的
			if(!appUserMobileService.isUnique(appUserMobile, "appUser.id,mobile.id,isExamine")) {
				return Result.warnResult().setMsg("该账号已与该设备绑定、请勿重复绑定");
			}
			appUserMobile.setIsExamine(0);
			
			//已经添加过了 重新发送验证码
			if(!appUserMobileService.isUnique(appUserMobile, "appUser.id,mobile.id,isExamine")) {
				Boolean auditStatus = true;
				if (appUserMobileList.size() > 1) {
					for(AppUserMobile  appUserMobileVo : appUserMobileList) {
						if (appUserMobileVo.getIsExamine() == 1) {
							auditStatus = false;
							if(NettyServerHandler.map.containsKey(appUserMobileVo.getAppAccount()))  {
								log.info("向"+appUserMobileVo.getAppAccount()+JSONObject.fromObject("{\"command\": \"bindingAudit\"}").toString());
								NettyServerHandler.map.get(appUserMobileVo.getAppAccount()).writeAndFlush(JSONObject.fromObject("{\"command\": \"bindingAudit\"}").toString());
							}
							
						}
					}
					
				}
				
				if (auditStatus) {
					if (!SMSUtil.sendSMSCode(simMark)) {
						log.error("向{}发送短信失败",simMark);
						return Result.errorResult().setMsg("向:"+simMark+"发送短信失败");
					}else {
						log.info("向{}发送第一次绑定验证短信成功",simMark);
						return Result.successResult().setMsg("已发送验证码，请继续输入正确的验证码完成绑定");
						}
				}
				
				return Result.successResult().setMsg("请等待审核！").setCode(3);

			}

			//是否第一次绑定
			if(CollectionUtils.isEmpty(appUserMobileList))
			{
				if (!SMSUtil.sendSMSCode(simMark)) {
					log.error("向{}发送短信失败",simMark);
					return Result.errorResult().setMsg("向:"+simMark+"发送短信失败");
				}else {
					log.info("向{}发送第一次绑定验证短信成功",simMark);
					appUserMobileService.saveEntity(appUserMobile);
					return Result.successResult().setMsg("已发送验证码，请继续输入正确的验证码完成绑定");
					}
			}
			else
			{
				Map<String,AppUserMobile>  auditPassMap = new HashMap<String,AppUserMobile>();
				for(AppUserMobile  appUserMobileVo : appUserMobileList) {
					if (appUserMobileVo.getIsExamine() == 1) {
						auditPassMap.put(appUserMobileVo.getAppAccount(), appUserMobileVo);
					}
				}
				
				//都是未审核通过的用户
				if(MapUtils.isEmpty(auditPassMap)) {
					if (!SMSUtil.sendSMSCode(simMark)) {
						log.error("向{}发送短信失败",simMark);
						return Result.errorResult().setMsg("向:"+simMark+"发送短信失败");
					}else{
						log.info("向{}发送第一次绑定验证短信成功",simMark);
						appUserMobileService.saveEntity(appUserMobile);
						return Result.successResult().setMsg("已发送验证码，请继续输入正确的验证码完成绑定");
						}
				}else {
					//如果在线通知手机端
					for(String str : auditPassMap.keySet()) {
						if(NettyServerHandler.map.containsKey(str)) {
							log.info(JSONObject.fromObject("{\"command\": \"bindingAudit\"}").toString());
							NettyServerHandler.map.get(str).writeAndFlush(JSONObject.fromObject("{\"command\": \"bindingAudit\"}").toString());
						}
					}
					appUserMobileService.saveEntity(appUserMobile);
					return Result.successResult().setMsg("请等待审核！").setCode(3);
				}
				
			}
			
			
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}



	/**
	 * 获取当前账号所有绑定的终端
	 */
	@RequestMapping(value="getMobileAllInterface")
	public Result bindingMobile(String token) 
	{
		try
		{
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");


			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_appUser.id",appUser.getId()+""));
			filters.add(new PropertyFilter("EQI_isExamine","1"));
			List<AppUserMobile>  appUserMobileList =appUserMobileService.find(filters);
            for(AppUserMobile appUserMobile :appUserMobileList) {
            	//查看是否在线
            	if(NettyServerHandler.map.containsKey(appUserMobile.getSimMark())) {
            		appUserMobile.setIsOnLine(1);
            	}
            	//获取最后一次位置信息、电量
            	filters.clear();
    			filters.add(new PropertyFilter("EQI_mobile.id",appUserMobile.getMobile().getId()+""));
    			Page<Navigation> navigationPage=new Page<Navigation>(0, 1);
    			navigationPage.setOrder("desc");
    			navigationPage.setOrderBy("id");
    			navigationPage=navigationService.findPage(navigationPage, filters);
    			if(CollectionUtils.isNotEmpty(navigationPage.getResult())) {
    				appUserMobile.setElectricQuantity(navigationPage.getResult().get(0).getElectricQuantity());
    				appUserMobile.setLatitude(navigationPage.getResult().get(0).getLatitude());
    				appUserMobile.setLongitude(navigationPage.getResult().get(0).getLongitude());
    			}
            }
			return Result.successResult().setMsg("success").setObj(appUserMobileList);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}


	/**
	 * 获取当设备绑定的所有账号、管理员权限
	 */
	@RequestMapping(value="getMobileAdminAllInterface")
	public Result getMobileAdminAll(String token) 
	{
		try
		{
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");


			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");

			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_isAdmin","1"));
			filters.add(new PropertyFilter("EQI_appUser.id",appUser.getId()+""));
			filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			List<AppUserMobile> appUserMobileList=appUserMobileService.find(filters);
			if(appUserMobileList==null||appUserMobileList.size()==0)
				return Result.warnResult().setMsg("当前登录账号与目标设备无绑定关系或非管理权限");

			filters.clear();
			appUserMobileList.clear();
			appUserMobileList =appUserMobileService.findBy("mobile.id", mobile.getId());
			return Result.successResult().setMsg("success").setObj(appUserMobileList);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}

	/**
	 * 解除绑定的终端
	 */
	@RequestMapping(value="untieMobileInterface")
	public Result untieMobile(String token,String simMark) 
	{
		try
		{
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");




			AppUser currentAppUser=loginInfoService.getAppUserByToken(token);
			if(currentAppUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");


			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile == null) {
				return Result.warnResult().setMsg("simMark不存在");
			}

			List<PropertyFilter> filters=Lists.newArrayList();
	
				filters.add(new PropertyFilter("EQI_appUser.id",currentAppUser.getId()+""));
				filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
				List<AppUserMobile> appUserMobileList=appUserMobileService.find(filters);
				if(appUserMobileList!=null&&appUserMobileList.size()>0)
				{
						appUserMobileService.delete(appUserMobileList.get(0));
						if (NettyServerHandler.map.containsKey(simMark)) {
							log.info(JSONObject.fromObject("{\"command\": \"BindingSuccess\"}").toString());
						    NettyServerHandler.map.get(simMark).writeAndFlush(JSONObject.fromObject("{\"command\": \"BindingSuccess\"}").toString());
						}
						return Result.successResult().setMsg("解绑成功");
				}
				else
				{
					return Result.warnResult().setMsg("前登录账号与目标设备无绑定关系"); 
				}
		
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}


	/**
	 * 修改终端名字
	 */
	@RequestMapping(value="updateMobileNameInterface")
	public Result updateMobileName(String token,String simMark,String mobileName,Integer isAutoAnswer) 
	{
		try
		{
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");


			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写simMark");
			
			if(isAutoAnswer == null)
				return Result.warnResult().setMsg("请填写是否自动接听");

			if(StringUtils.isBlank(mobileName))
				return Result.warnResult().setMsg("请填写mobileName");

			AppUser currentAppUser=loginInfoService.getAppUserByToken(token);
			if(currentAppUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)
				return Result.warnResult().setMsg("simMark无对应终端");

			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_appUser.id",currentAppUser.getId()+""));
			filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			List<AppUserMobile> appUserMobileList=appUserMobileService.find(filters);

			if(appUserMobileList==null||appUserMobileList.size()==0)
				return Result.warnResult().setMsg("当前账号与对应终端无绑定，无法操作");

			appUserMobileList.get(0).setMobileName(mobileName);
			appUserMobileList.get(0).setIsAutoAnswer(isAutoAnswer);
			appUserMobileService.saveEntity(appUserMobileList.get(0));
			return Result.successResult().setMsg("success");


		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	/**
	 *   视频分享
	 * @param token
	 * @param simMark
	 * @param videoUrl
	 * @return
	 */
	@RequestMapping(value="shareVideoInterface")
	public Result shareVideo(String token,String simMark,String videoUrl) {
		
		if(!StringUtils.isAllNotEmpty(token,simMark,videoUrl)) {
			return Result.warnResult().setMsg("请填写完整参数token,simMark,videoUrl");
		}
		

		AppUser appUser=loginInfoService.getAppUserByToken(token);
		if(appUser==null)
		    return Result.warnResult().setMsg("token不存在或已过期");
		
		if(!appUserMobileService.reviewedOrNot(appUser.getId(),simMark)) {
			return Result.warnResult().setMsg("simMark不存在或未正在审核中！");
		}
		
		Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
		MessageBoard messageBoard = new MessageBoard();
		messageBoard.setUrl(videoUrl);
		messageBoard.setType(2);
		messageBoard.setStatus(0);
		messageBoard.setMobile(mobile);
		messageBoard.setAccount(appUser.getAccount());
		messageBoardService.saveEntity(messageBoard);
		
		//如果终端在线通知终端
		if(NettyServerHandler.map.containsKey(simMark)) {
			log.info(JSONObject.fromObject("{\"command\": \"Update video sharing\"}").toString());
			NettyServerHandler.map.get(simMark).writeAndFlush(JSONObject.fromObject("{\"command\": \"Update video sharing\"}").toString());
		}
		return Result.successResult();
		
	}
	/**
	 * 审核绑定请求
	 */
	@RequestMapping(value="examineBindingMobileInterface")
	public Result examineBindingMobile(String simMark,String token,Boolean isExamine,String account) 
	{
		try
		{
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写simMark");

			if(isExamine == null)
				return Result.warnResult().setMsg("请填写isExamine");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");
			
			AppUser auditAppUser = appUserService.findUniqueBy("account", account);
			if(auditAppUser == null)
				return Result.warnResult().setMsg(account+"该账号不存在！");

			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)
				return Result.warnResult().setMsg("SimMark卡号不存在");

			List<PropertyFilter> filters = Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_mobile.id", mobile.getId()+""));
			filters.add(new PropertyFilter("EQI_appUser.id",auditAppUser.getId()+""));
			List<AppUserMobile>  appUserMobileList =appUserMobileService.find(filters);
			
			if(null == appUserMobileList || appUserMobileList.size() ==0 || appUserMobileList.get(0).getIsExamine() !=0){
				return Result.warnResult().setMsg("无效的绑定请求,或已被其他用户审核完成");
			}
			
			if(isExamine) {
				appUserMobileList.get(0).setIsExamine(1);
			}else {
				appUserMobileList.get(0).setIsExamine(2);
			}
			
			if (NettyServerHandler.map.containsKey(simMark)) {
				log.info(JSONObject.fromObject("{\"command\": \"BindingSuccess\"}").toString());
			    NettyServerHandler.map.get(simMark).writeAndFlush(JSONObject.fromObject("{\"command\": \"BindingSuccess\"}").toString());
			}
			
			appUserMobileService.saveEntity(appUserMobileList.get(0));
			return Result.successResult().setMsg("审核完成！");
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}

	
	
	/**
	 * 第一次绑定验证码方式
	 */
	@RequestMapping(value="firstExamineBindingMobileInterface")
	public Result examineBindingMobile(String simMark,String token,String smsCode) 
	{
		try
		{
			if(StringUtils.isBlank(smsCode))
				return Result.warnResult().setMsg("验证码不能为空");
			
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写simMark");
			

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");
			
			Integer status=SMSUtil.checkSMSCode(simMark, smsCode);
			if(status==0)
				return Result.warnResult().setMsg("验证码错误");
			if(status==2)
				return Result.warnResult().setMsg("验证码已过期");


			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)
				return Result.warnResult().setMsg("SimMark卡号不存在");

			
			List<PropertyFilter> filters = Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_mobile.id", mobile.getId()+""));
			filters.add(new PropertyFilter("EQI_appUser.id",appUser.getId()+""));
			List<AppUserMobile>  appUserMobileList =appUserMobileService.find(filters);
			if(null == appUserMobileList || appUserMobileList.size() ==0 || appUserMobileList.get(0).getIsExamine() !=0){
				return Result.warnResult().setMsg("无效的绑定请求,或已被其他用户审核完成");
			}
			appUserMobileList.get(0).setIsExamine(1);
			appUserMobileService.saveEntity(appUserMobileList.get(0));
			if (NettyServerHandler.map.containsKey(simMark)) {
				log.info(JSONObject.fromObject("{\"command\": \"BindingSuccess\"}").toString());
			    NettyServerHandler.map.get(simMark).writeAndFlush(JSONObject.fromObject("{\"command\": \"BindingSuccess\"}").toString());
			}
			return Result.successResult().setMsg("审核完成！");
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	
	/**
	 * 需审核列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="waitExamineListInterface")
	public Result waitExamineListInterface(String token) 
	{
		try
		{
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");


			String sql="SELECT am.id, a.account,am.mobile_name,m.sim_mark  FROM app_user_mobile am  left join app_user a on a.id = am.app_user_id  left join  mobile m on m.id = am.mobile_id  where\r\n" + 
					"mobile_id IN ( SELECT mobile_id FROM app_user_mobile WHERE app_user_id = "+appUser.getId()+" AND is_examine = 1 ) AND is_examine = 0";
			List<AppUserMobile> appUserMobileList=appUserMobileService.getEntityDao().createSqlQuery(sql, null).addEntity(AppUserMobileVo.class).list();
			return Result.successResult().setObj(appUserMobileList);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}

	
	/**
	 * 查看在线终端！
	 */
	@RequestMapping(value="showListInterface")
	public Object showList() 
	{
		return NettyServerHandler.map.keySet().toString() + "|||" + NettyServerHandler.overtimeMap.keySet().toString();
	}
   public static void main(String[] args) throws Exception {
	   decompose(4,3,1);
}

   public static Integer decompose(Integer number,Integer param, Integer orderNumber) {
	     if(param <= 0) {
	    	 return 0;
	     }else {
	    	 System.out.println(number - param +"+"+param +"="+number);
	    	 return decompose(number,--param,orderNumber);
	     }
   }
   
   
}
