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
import com.hooro.ri.config.netty.NettyServerHandler;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.Contacts;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.AppUserMobileServiceImpl;
import com.hooro.ri.service.impl.ContactsServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.util.PushNotiUtil;

import net.sf.json.JSONObject;

/**
 * 电话本
 *
 */
@RestController
public class AppContactsController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private ContactsServiceImpl contactsService;
	
	@Autowired   private LoginInfoServiceImpl loginInfoService;
	
	@Autowired   private MobileServiceImpl mobileService;
	
	@Autowired   private AppUserMobileServiceImpl appUserMobileService;

	/**
	 * 获取所有电话本
	 * @return
	 */
	@RequestMapping(value="/getContactsAllInterface")
	public Result getContactsAll(String token,String simMark)
	{
		try {

			if(!StringUtils.isAllNotEmpty(token,simMark)) {
				return Result.warnResult().setMsg("请填写完整参数token,simMark");
			}

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			if(!appUserMobileService.reviewedOrNot(appUser.getId(),simMark)) {
				return Result.warnResult().setMsg("simMark不存在或正在审核中！");
			}
	
			List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_mobile.simMark",simMark));
			List<Contacts> contactsList=contactsService.findJoin(filters);
			return Result.successResult().setObj(contactsList);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}

	
	/**
	 * 获取指定ID电话本
	 * @return
	 */
	@RequestMapping(value="/getContactsByIdInterface")
	public Result getContactsById(Integer id,String token)
	{
		try {
			if(id==null)
				return Result.warnResult().setMsg("id不能为空");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			Contacts contacts=contactsService.getById(id);
			return Result.successResult().setObj(contacts);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	/**
	 * 修改或新增电话本
	 * @return
	 */
	@RequestMapping(value="/saveContactsAllInterface")
	public Result saveContactsAll(@ModelAttribute("Contacts") Contacts contacts,String token,String simMark )
	{
		try {
			if(!StringUtils.isAllNotEmpty(token,simMark)) {
				return Result.warnResult().setMsg("请填写完整参数token,simMark");
			}
			

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			if(!appUserMobileService.reviewedOrNot(appUser.getId(),simMark)) {
				return Result.warnResult().setMsg("simMark不存在或未正在审核中！");
			}
			
			String msg=contacts.notNullCheck();
			if(msg!=null) {
				return Result.warnResult().setMsg(msg);
			}

			
			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)
				return Result.warnResult().setMsg("SimMark卡号不存在");
			
			contacts.setMobile(mobile);
			contacts.setUpdateBy(appUser.getAccount());
			
			if(!contactsService.isUnique(contacts,"contactPhone,mobile.id")) {
				return Result.warnResult().setMsg("该联系人已经存在！");
			};
			contactsService.saveEntity(contacts);
			
			//如果终端在线通知终端
			if(NettyServerHandler.map.containsKey(simMark)) {
				log.info(JSONObject.fromObject("{\"command\": \"emergency contact\"}").toString());
				NettyServerHandler.map.get(simMark).writeAndFlush(JSONObject.fromObject("{\"command\": \"emergency contact\"}").toString());
			}
			return Result.successResult().setObj(contacts.getId());

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value="/delContactsInterface")
	public Result getContacts(Integer id,String token)
	{
		try {
			if(id==null)
				return Result.warnResult().setMsg("id不能为空");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			Contacts condition = contactsService.getById(id);
			if (condition == null) {
				return Result.warnResult().setMsg("该ID不存在！");
			}
			
			String simMark = condition.getMobile().getSimMark();
			contactsService.delete(condition);
			
			//Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
			//如果终端在线通知终端
			if(NettyServerHandler.map.containsKey(simMark)) {
				log.info(JSONObject.fromObject("{\"command\": \"emergency contact\"}").toString());
				NettyServerHandler.map.get(simMark).writeAndFlush(JSONObject.fromObject("{\"command\": \"emergency contact\"}").toString());
			}
			return Result.successResult().setObj("success");

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}

}
