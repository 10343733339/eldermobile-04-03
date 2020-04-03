package com.hooro.ri.terminalInterface;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.config.netty.NettyServerHandler;
import com.hooro.ri.model.Contacts;
import com.hooro.ri.model.LoginInfo;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.AppUserServiceImpl;
import com.hooro.ri.service.impl.ContactsServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.util.JPushUtil;

/**
 * 电话本
 *
 */
@RestController
public class ContactsController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());


	@Autowired   private ContactsServiceImpl contactsService;

	@Autowired   private MobileServiceImpl mobileService;
	
	@Autowired   private LoginInfoServiceImpl loginInfoService;
	/**
	 * 获取电话本
	 * @return
	 */
	@RequestMapping(value="/getContactsInterface")
	public Result getContacts(String token)
	{
		try {
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");
			
			Mobile mobile = mobileService.findUniqueBy("token", token);
			if(mobile == null) {
				return Result.warnResult().setMsg("登录过期请重新登录！");
			}

			List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_mobile.simMark",mobile.getSimMark()));
			List<Contacts> contactsList=contactsService.findJoin(filters);
			return Result.successResult().setObj(contactsList);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	
	
	/**
	 * 音视频消息通知
	 * 当前app用户在线不推送，不在线发送透传消息
	 * @return
	 */
	@RequestMapping(value="/sendNoticeInterface")
	public Result sendNotice(String token,String account,String sendMsg)
	{
		try {
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");
			
			if(StringUtils.isBlank(account))
				return Result.warnResult().setMsg("请填写account");
			
			Mobile mobile = mobileService.findUniqueBy("token", token);
			if(mobile == null) {
				return Result.warnResult().setMsg("登录过期请重新登录！");
			}
           if (!NettyServerHandler.map.containsKey(account)) {
        	   LoginInfo  loginInfo  =  loginInfoService.getLogInfoByAccount(account);
        	   if (loginInfo == null) {
        		   return Result.warnResult().setMsg("音视频对讲用户不存在");
        	   }
        	   JPushUtil.jpushAndroid(loginInfo.getXg_token(),sendMsg);
           }
		  return Result.successResult();
			

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	


}
