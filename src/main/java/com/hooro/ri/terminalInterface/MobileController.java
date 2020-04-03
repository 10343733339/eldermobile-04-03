package com.hooro.ri.terminalInterface;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.hooro.ri.config.netty.NettyServerHandler;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.AppUserMobile;
import com.hooro.ri.model.LoginInfo;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.AppUserMobileServiceImpl;
import com.hooro.ri.service.impl.AppUserServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.util.OnLlineUtil;
import com.hooro.ri.util.PushNotiUtil;
import com.hooro.ri.util.SMSUtil;
import com.hooro.ri.vo.AppUserMobileVo;

import net.sf.json.JSONObject;

/**
 * 终端接口
 *
 */
@RestController
public class MobileController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private MobileServiceImpl mobileService;
	@Autowired   private AppUserMobileServiceImpl  appUserMobileServiceImpl;
	@Autowired   private AppUserServiceImpl appUserService;
	@Autowired   private LoginInfoServiceImpl loginInfoService;


	/**
	 * 终端验证
	 * @return
	 */
	@RequestMapping(value="mobileLoginInterface")
	public Result mobileLogin(Mobile parmobile)
	{
		try {
			
			
			if(StringUtils.isBlank(parmobile.getSimMark()))
				return Result.warnResult().setMsg("请填写卡号");

			if(StringUtils.isBlank(parmobile.getVersion()))
				return Result.warnResult().setMsg("请填写版本号");
			
			if(StringUtils.isBlank( parmobile.getPassWord()))
				return Result.warnResult().setMsg("密码不能为空");

			List<PropertyFilter> filters = Lists.newArrayList();
			filters.add(new PropertyFilter("EQS_simMark",parmobile.getSimMark()));
			filters.add(new PropertyFilter("EQS_passWord",parmobile.getPassWord()));
			List<Mobile>  mobiles= mobileService.find(filters);
			if(CollectionUtils.isEmpty(mobiles)) {
				return Result.warnResult().setMsg("账号或者密码有误！"); 
			}
			if(NettyServerHandler.map.containsKey(parmobile.getSimMark())){
				return Result.warnResult().setMsg("当前账号正在使用中！"); 
			}
			Mobile mobile=mobiles.get(0);
			String token = UUID.randomUUID().toString();
			log.info(parmobile.getSimMark()+"卡号登录token为"+token);
			mobile.setToken(token);
			mobileService.save(mobile);
			return Result.successResult().setMsg("登录成功").setObj(token);
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	/**
	 * 注册接口
	 * @return
	 */
	@RequestMapping(value="mobileRegisterInterface")
	public Result mobileRegister(Mobile parmobile)
	{
		try {
			
			
			if(StringUtils.isBlank(parmobile.getSimMark()))
				return Result.warnResult().setMsg("请填写卡号");

			if(StringUtils.isBlank(parmobile.getVersion()))
				return Result.warnResult().setMsg("请填写版本号");
			
			if(StringUtils.isBlank( parmobile.getPassWord()))
				return Result.warnResult().setMsg("密码不能为空");
			
			if(StringUtils.isBlank( parmobile.getSmsCode()))
				return Result.warnResult().setMsg("验证码不能为空");

			if(!mobileService.isUnique(parmobile, "simMark"))
			     return Result.warnResult().setMsg("该账号已存在");
			

			Integer status=SMSUtil.checkSMSCode(parmobile.getSimMark(), parmobile.getSmsCode());
			if(status==0)
				return Result.warnResult().setMsg("验证码错误");
			if(status==2)
				return Result.warnResult().setMsg("验证码已过期");
			mobileService.save(parmobile);
			return Result.successResult().setMsg("注册成功");
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	/**
	 * 获取指定终端绑定账号信息
	 * @return
	 */
	@RequestMapping(value="/getAccountBysimMarkInterface")
	public Result getAccountBysimMark( String token)
	{
		try {
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			Mobile mobile = mobileService.findUniqueBy("token", token);
			if(mobile == null) {
				return Result.warnResult().setMsg("登录过期请重新登录！");
			}
			
			List<PropertyFilter> filters =Lists.newArrayList();
			filters .add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			filters.add(new PropertyFilter("EQI_isExamine","1"));
			List<AppUserMobile> appUserMobileList=appUserMobileServiceImpl.find(filters);
			List<AppUserMobileVo> appUsermobileVoList=Lists.newArrayList();
			AppUserMobileVo appUserMobileVo;
			for(int i=0;i<appUserMobileList.size();i++)
			{
				appUserMobileVo=new AppUserMobileVo();
				String account = appUserMobileList.get(i).getAppAccount();
				appUserMobileVo.setAccount(account);
				appUserMobileVo.setNickName(appUserMobileList.get(i).getAppNickName());
				//用户端是否在线
				if (NettyServerHandler.map.containsKey(account)) {
					appUserMobileVo.setIsOnLine(1);
				}else {
					appUserMobileVo.setIsOnLine(0);
				}
				appUsermobileVoList.add(appUserMobileVo);
			}
			return Result.successResult().setObj(appUsermobileVoList);
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	/**
	 * 更新终端在线状态
	 * @return
	 */
	@RequestMapping(value="/setOneLinestateInterface")
	public Result setOneLinestate( String simMark)
	{
		try {
			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写卡号");
			
			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)  
			{
				return Result.warnResult().setMsg("未注册");
			}
			Long time=OnLlineUtil.onLine.get(simMark);
			if(time==null)
			{
				List<AppUser> appUserList=appUserService.findBy("simMark", simMark);
    			for(AppUser appUser:appUserList)
    				{
    				if(StringUtils.isNotBlank(appUser.getSimMark()))
    				{
    					LoginInfo loginInfo=loginInfoService.getLoginInfoByaccount(appUser.getId());
    					PushNotiUtil.pushSingleMsg(loginInfo.getXg_token(), "{\"cmd\":\"GoOnline\",\"data\":\""+simMark+"\"}", null, 2, 2,loginInfo.getXgType(),"");
    					log.info("\t设备已上线:"+simMark+"\n");
    				}
    				}
			}
			OnLlineUtil.onLine.put(simMark,new Date().getTime());
			return Result.successResult();
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	
	/**
	 *  找回密码
	 */
	@RequestMapping(value="retrieveMobilePasswordInterface")
	public Result retrievePassword(String simMark,String code,String newPassWord) 
	{
		
		Result result = new Result();
		try
		{
			if(StringUtils.isBlank(simMark))
			{
				return result.setCode(2).setMsg("simMark不能为空");
			}
			
			if(StringUtils.isBlank(code))
			{
				return result.setCode(2).setMsg("code不能为空");
			}
			
			if(StringUtils.isBlank(newPassWord))
			{
				return result.setCode(2).setMsg("newPassWord不能为空");
			}
			Mobile mobile = mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)
				return result.setCode(2).setMsg("未找到此账号");
			
			Integer status=SMSUtil.checkSMSCode(mobile.getSimMark(), code);
			if(status==0)
				return result.setCode(2).setMsg("验证码错误");
			if(status==2)
				return result.setCode(2).setMsg("验证码已过期");
			
			
			mobile.setPassWord(newPassWord);
			mobileService.save(mobile);
			return Result.successResult();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
}}
