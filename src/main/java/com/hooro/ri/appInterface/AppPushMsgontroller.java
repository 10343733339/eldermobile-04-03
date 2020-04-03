package com.hooro.ri.appInterface;

import java.util.List;
import java.util.Map;

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
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.LoginInfo;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.util.PushNotiUtil;

import net.sf.json.JSONObject;

/**
 * 推送消息
 *
 */
@RestController
public class AppPushMsgontroller  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired  MobileServiceImpl mobileService;
	@Autowired  LoginInfoServiceImpl  loginInfoService;

	/**
	 * 推送给指定账号
	 */
	/**
	 * @param account
	 * @param content
	 * @param type
	 * @return
	 */
	@RequestMapping(value="phshMsgAccountInterface")
	public Result phshMsgAccount(String token,String content) 
	{
		try
		{

			if(StringUtils.isBlank(content))
				return Result.warnResult().setMsg("请填写消息内容");


			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");


			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");

			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());

			if(StringUtils.isBlank(mobile.getXgToken()))
				return Result.warnResult().setMsg("该账号处于离线状态、无法接收推送");	


			return PushNotiUtil.pushSingleMsg(mobile.getXgToken(), content, null,2, 1,1,"");

		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}

	/**
	 * 推送给指定账号
	 */
	/**
	 * @param account
	 * @param content
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="phshMsgAccountJsonInterface",produces="application/json;charset=UTF-8",method=RequestMethod.POST)
	public Result phshMsgAccount(@RequestBody String json) 
	{
		try
		{
			JSONObject  jasonObject = JSONObject.fromObject(json);
			Map<String,String> map = (Map<String,String>)jasonObject;

			if(StringUtils.isBlank(map.get("account")))
				return Result.warnResult().setMsg("请填写接收消息的账号");

			if(StringUtils.isBlank(map.get("content")))
				return Result.warnResult().setMsg("请填写消息内容");


			if(StringUtils.isBlank(map.get("type")))
				return Result.warnResult().setMsg("请填写推送类型,1推送给终端、2推送给app");

			if(StringUtils.isBlank(map.get("msgType")))
				return Result.warnResult().setMsg("请填写消息类型1:通知消息  2、应用内消息");

			String account=map.get("account");
			Integer xgType=1;
			if(map.get("type").equals("1"))
			{
				Mobile mobile=mobileService.findUniqueBy("simMark", account);
				if(mobile==null)
					return Result.warnResult().setMsg("账号不存在");

				if(StringUtils.isBlank(mobile.getXgToken()))
					return Result.warnResult().setMsg("该账号处于离线状态、无法接收推送");	
				account=mobile.getXgToken();

			}
			else
			{

				List<PropertyFilter> filters=Lists.newArrayList();
				filters.add(new PropertyFilter("EQS_appUser.account",account));
				filters.add(new PropertyFilter("EQI_status","1"));
				List<LoginInfo> loginInfoList=loginInfoService.find(filters);
				if(loginInfoList==null||loginInfoList.size()==0)
					return Result.warnResult().setMsg("账号不存在");

				if(StringUtils.isBlank(loginInfoList.get(0).getXg_token()))
					return Result.warnResult().setMsg("该账号处于离线状态、无法接收推送");	

				account=loginInfoList.get(0).getXg_token();
				xgType=loginInfoList.get(0).getXgType();
			}
			return PushNotiUtil.pushSingleMsg(account, map.get("content"), null,Integer.parseInt(map.get("msgType")) , Integer.parseInt(map.get("type")),xgType,"");

		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	
	/**
	 * @param token
	 * @param status 
	 * @param dur    间隔
	 * @param count  次数
	 * @param type   0:高精度;1:低功耗;2:GPS
	 * @return
	 */
	@RequestMapping(value="onOrOffLocationInterface")
	public Result onOrOffLocation(String token,String status,String dur,String count,String type) 
	{
		try
		{
            count="99999";//暂时强制99999次  ios已经是  安卓有问题
			String msg="";
			if(StringUtils.isBlank(status))
				return Result.warnResult().setMsg("请填写状态");


			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

            if(status.equals("startLocation"))
            {
    			if(StringUtils.isBlank(dur))
    				return Result.warnResult().setMsg("请填写dur");
    			
    			if(StringUtils.isBlank(count))
    				return Result.warnResult().setMsg("请填写count");
    			
    			if(StringUtils.isBlank(type))
    				return Result.warnResult().setMsg("请填写type");
    			
    			msg="{\"cmd\":\"startLocation\",\"dur\":\""+dur+"\",\"count\":\""+count+"\",\"type\":\""+type+"\"}";
            }
            else
            {
            	msg="{\"cmd\":\"stopLocation\"}";
            }
			
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");

			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());

			if(StringUtils.isBlank(mobile.getXgToken()))
				return Result.warnResult().setMsg("该账号处于离线状态、无法接收推送");	


			return PushNotiUtil.pushSingleMsg(mobile.getXgToken(), msg, null,2, 1,1,"");

		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	
	/**  上传wifi
	 * @param token
	 * @param type  １　WIFICIPHER_NOPASS    ２　WIFICIPHER_WEP   ３　WIFICIPHER_WPA
	 *               
	 * @return
	 */
	@RequestMapping(value="addWifiInterface")
	public Result addWifi(String token,String name,String password,String type) 
	{
		try
		{

			String msg="";
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			if(StringUtils.isBlank(name))
				return Result.warnResult().setMsg("请填写name");
			
			if(StringUtils.isBlank(password))
				return Result.warnResult().setMsg("请填写password");
			
			if(StringUtils.isBlank(type))
				return Result.warnResult().setMsg("请填写type");
			
			
			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

          
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");

			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());

			if(StringUtils.isBlank(mobile.getXgToken()))
				return Result.warnResult().setMsg("该账号处于离线状态、无法接收推送");	

            msg="{\"cmd\":\"addWifi\",\"name\":\""+name+"\",\"password\":\""+password+"\",\"type\":\""+type+"\"} ";
			return PushNotiUtil.pushSingleMsg(mobile.getXgToken(), msg, null,2, 1,1,"");

		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
	
	/**  请求监控
	 * @param token
	 *  type 0表示 视频监控，1表示 语音家弄
	 * @return
	 */
	@RequestMapping(value="startMonitorInterface")
	public Result startMonitor(String token,Integer type,String cameraId) 
	{
		try
		{

			String msg="";
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");
			
			if(StringUtils.isBlank(cameraId))
				return Result.warnResult().setMsg("请先填写cameraId");

			if(type==null)
				return Result.warnResult().setMsg("请选择监控类型");
			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");
          
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");
			
			

			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());

			if(StringUtils.isBlank(mobile.getXgToken()))
				return Result.warnResult().setMsg("该账号处于离线状态、无法接收推送");	

            msg="{\"cmd\":\"startMonitor\",\"type\":\""+type+"\",\"token\":\""+token+"\",\"cameraId\":\""+cameraId+"\"}";
			return PushNotiUtil.pushSingleMsg(mobile.getXgToken(), msg, null,2, 1,1,"");

		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}
	}
}
