package com.hooro.ri.appInterface;

import java.util.Date;
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
import com.google.common.collect.Lists;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.AppUserMobile;
import com.hooro.ri.model.LoginInfo;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.AppUserMobileServiceImpl;
import com.hooro.ri.service.impl.AppUserServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.util.OnLlineUtil;
import com.hooro.ri.util.SMSUtil;

/**
 * app用户接口
 *
 */
@RestController
public class AppUserController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private AppUserServiceImpl appUserService;
	@Autowired   private LoginInfoServiceImpl loginInfoService;
	@Autowired   private MobileServiceImpl mobileService;
	@Autowired   private AppUserMobileServiceImpl appUserMobileServiceImpl;
	/**
	 * 登录验证  
	 */ 
	@RequestMapping(value = { "loginInterface"})
	public Result Login(String account, String passWord,String registrationId,String xgType) {

		try {
			Result result = null;
			String msg = null;
			if(StringUtils.isBlank(account)){
				msg = "请输入账号!";
				result = new Result(Result.ERROR, msg, null);
				return result;
			}
			if(StringUtils.isBlank(passWord)){
				msg = "请输入密码!";
				result = new Result(Result.ERROR, msg, null);
				return result;
			}
			
			if(StringUtils.isBlank(registrationId)){
				msg = "请填写registrationId!";
				result = new Result(Result.ERROR, msg, null);
				return result;
			}
			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQS_account",account));
			filters.add(new PropertyFilter("EQS_passWord",passWord));
			List<AppUser> appUserList= appUserService.find(filters);

			if (appUserList == null||appUserList.size()==0) {
				msg = "用户名或密码不正确!";
				result = new Result(Result.ERROR, msg, "codeError");
				return result;
			}
            //获取未审核的数据
			filters.clear();
			filters.add(new PropertyFilter("EQI_appUser.id",appUserList.get(0).getId()+""));
			filters.add(new PropertyFilter("EQI_isExamine","0"));
			List<AppUserMobile> appUserMobiles= appUserMobileServiceImpl.find(filters);
			appUserList.get(0).setAppUserMobiles(appUserMobiles);
			
			String token=loginInfoService.saveToken(appUserList.get(0),registrationId,xgType);
			appUserList.get(0).setToken(token);
			result = new Result(Result.SUCCESS, "用户验证通过!", appUserList.get(0));
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}


	/**
	 * 注册
	 */
	@RequestMapping(value = { "regeditInterface"})
	public Result regedit(@ModelAttribute("AppUser") AppUser appUser,String smsCode) {
		Result result = new Result();
		
		try {
			if(StringUtils.isBlank(smsCode))
				return result.setCode(2).setMsg("验证码不能为空");
			
			String msg=appUser.checkDate();
			if(msg!=null)
				return result.setCode(2).setMsg(msg);
			
			
			if(!appUserService.isUnique(appUser, "account"))
			     return result.setCode(2).setMsg("该账号已存在");
			
			
			appUser.setPhone(appUser.getAccount());;
			Integer status=SMSUtil.checkSMSCode(appUser.getPhone(), smsCode);
			if(status==0)
				return result.setCode(2).setMsg("验证码错误");
			if(status==2)
				return result.setCode(2).setMsg("验证码已过期");
			
			appUserService.save(appUser);
			return result.setCode(1).setMsg("注册成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}


	/**
	 * 发送短信验证码
	 */
	@RequestMapping(value="sendCodeInterface")
	public Result sendCode(String phone) 
	{
		try
		{
			if (StringUtils.isBlank(phone)) return Result.errorResult().setMsg("请填写手机号！");

			if (!SMSUtil.sendSMSCode(phone)) return Result.errorResult().setMsg("发送失败");
			
			log.info("短信发送成功"+phone);

			return Result.successResult();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	/**
	 * 绑定默认终端
	 */
	@RequestMapping(value="bindingDefaultSimMarkInterface")
	public Result bindingDefaultSimMark(String token,String simMark) 
	{
		
		Result result = new Result();
		try
		{
			
			if(StringUtils.isBlank(token))
			{
				return result.setCode(2).setMsg("token不能为空");
			}
			if(StringUtils.isBlank(simMark))
			{
				return result.setCode(2).setMsg("simMark不能为空");
			}
			
			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return result.setCode(2).setMsg("token不存在或已过期");
			
			appUser.setSimMark(simMark);
			appUserService.save(appUser);
			return Result.successResult();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	
	/**
	 *  注销登录
	 */
	@RequestMapping(value="logoutInterface")
	public Result bindingDefaultSimMark(String token) 
	{
		
		Result result = new Result();
		try
		{
			if(StringUtils.isBlank(token))
			{
				return result.setCode(2).setMsg("token不能为空");
			}
			
			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_status","1"));
			filters.add(new PropertyFilter("EQS_token",token));
			List<LoginInfo> list=loginInfoService.find(filters);
			if(list==null||list.size()==0)
				return result.setCode(2).setMsg("token不存在或已过期");
			
			LoginInfo loginInfo=list.get(0);
			loginInfo.setStatus(0);
			loginInfoService.save(loginInfo);
			return Result.successResult();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	
	/**
	 *  修改密码
	 */
	@RequestMapping(value="updatePasswordInterface")
	public Result updatePassword(String token,String newPassWord,String oldPassWord) 
	{
		
		Result result = new Result();
		try
		{
			if(StringUtils.isBlank(token))
			{
				return result.setCode(2).setMsg("token不能为空");
			}
			
			if(StringUtils.isBlank(newPassWord))
			{
				return result.setCode(2).setMsg("newPassWord不能为空");
			}
			
			if(StringUtils.isBlank(oldPassWord))
			{
				return result.setCode(2).setMsg("oldPassWord不能为空");
			}
			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_status","1"));
			filters.add(new PropertyFilter("EQS_token",token));
			List<LoginInfo> list=loginInfoService.find(filters);
			if(list==null||list.size()==0)
				return result.setCode(2).setMsg("token不存在或已过期");
			
			AppUser appUser=list.get(0).getAppUser();
			if(!appUser.getPassWord().equals(oldPassWord))
				return result.setCode(2).setMsg("原密码错误!");
			appUser.setPassWord(newPassWord);
			appUserService.save(appUser);
			return Result.successResult();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	
	/**
	 *  找回密码
	 */
	@RequestMapping(value="retrievePasswordInterface")
	public Result retrievePassword(String account,String code,String newPassWord) 
	{
		
		Result result = new Result();
		try
		{
			if(StringUtils.isBlank(account))
			{
				return result.setCode(2).setMsg("account不能为空");
			}
			
			if(StringUtils.isBlank(code))
			{
				return result.setCode(2).setMsg("code不能为空");
			}
			
			if(StringUtils.isBlank(newPassWord))
			{
				return result.setCode(2).setMsg("newPassWord不能为空");
			}
			AppUser appUser=appUserService.findUniqueBy("account", account);
			if(appUser==null)
				return result.setCode(2).setMsg("未找到此账号");
			
			Integer status=SMSUtil.checkSMSCode(account, code);
			if(status==0)
				return result.setCode(2).setMsg("验证码错误");
			if(status==2)
				return result.setCode(2).setMsg("验证码已过期");
			
			
			appUser.setPassWord(newPassWord);
			appUserService.save(appUser);
			return Result.successResult();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	/**
	 *  设置围栏
	 */
	@RequestMapping(value="setFenceInterface")
	public Result setFence(String token,String longitude,String latitude,Double radius) 
	{
		
		Result result = new Result();
		try
		{
			if(StringUtils.isBlank(token))
				return result.setCode(2).setMsg("token不能为空");
			
			if(StringUtils.isBlank(longitude))
				return result.setCode(2).setMsg("longitude");
			
			if(StringUtils.isBlank(latitude))
				return result.setCode(2).setMsg("latitude不能为空");
			
			if(radius==null)
				return result.setCode(2).setMsg("radius不能为空");
			
			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");
			
			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
			mobile.setLongitude(longitude);
			mobile.setLatitude(latitude);
			mobile.setRadius(radius);
			mobileService.save(mobile);
			return Result.successResult();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	
	/**
	 *  查看终端是否在线情况
	 */
	@RequestMapping(value="getOnlineStateInterface")
	public Result getOnlineState(String token) 
	{
		
		Result result = new Result();
		try
		{
			if(StringUtils.isBlank(token))
				return result.setCode(2).setMsg("token不能为空");
			
			
			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");
			
			
			Long onLine=OnLlineUtil.onLine.get(appUser.getSimMark());
			if(onLine==null)
				return Result.successResult().setObj("0");
			
			
			if(new Date().getTime()-onLine>180000)
				return Result.successResult().setObj("0");
			else
				return Result.successResult().setObj("1");
			
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	

	
	
}
