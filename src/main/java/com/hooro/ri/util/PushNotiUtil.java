package com.hooro.ri.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eryansky.common.model.Result;
import com.eryansky.common.utils.StringUtils;
import com.google.common.collect.Maps;
import com.hooro.ri.config.ThirdPartConfig;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;

public class PushNotiUtil 
{
	public static Integer MsgTypeNoti = 1;

	public static Integer MsgTypeCmd = 2;

	private final static Logger log = LoggerFactory.getLogger(PushNotiUtil.class);


	/**
	 * @param account 推送的账号
	 * @param content 内存
	 * @param ext
	 * @param msgType 消息类型
	 * @param type    账号类型 1终端 2app
	 * @return
	 */
	public static Result pushSingleMsg(String token, String content, Map<String, Object> ext, Integer msgType,Integer type,Integer xgType,String alertIos) 
	{
		if(xgType==null||xgType!=2)
		{
			Message msg = new Message();
			msg.setTitle("老人机助手");
			msg.setContent(content);
			msg.setType(msgType);
			msg.setCustom(ext);
			return sendSingleMsg(token, msg,type,xgType);
		}
		else
		{
			MessageIOS message = new MessageIOS();
			JSONObject aps = new JSONObject();
			JSONObject obj = new JSONObject();
			
			aps.put("content-available", 1);
			
			aps.put("badge", 1);
			if(StringUtils.isNotBlank(alertIos))
			  aps.put("sound", "default");
			
			aps.put("body", content);
			obj.put("aps", aps); 
			aps.put("alert", alertIos); 
			message.setRaw(obj.toString());
			return sendSingleMsg(token, message,type,xgType);
		}
	}

	/**
	 * @param account  推送的账号
	 * @param msg      推送消息
	 * @param type     1 推送给终端  2推送给app
	 * @return
	 */
	private static <T> Result sendSingleMsg(String token, T msg,Integer type,Integer xgType)
	{

		XingeApp xinge = null; 
		JSONObject result = null;
		JSONObject formalresult = null;
		if(xgType==null||xgType!=2) //android 推送
		{
			if (type==1)
			{
				xinge = new XingeApp(ThirdPartConfig.PUSH_MOBILE_APPID, ThirdPartConfig.PUSH_MOBILE_SECRE_KEY);
				result=xinge.pushSingleDevice(token, (Message) msg);
				formalresult=xinge.pushSingleDevice(token, (Message) msg);
			}
			else
			{
				xinge = new XingeApp(ThirdPartConfig.PUSH_APP_APPID, ThirdPartConfig.PUSH_APP_SECRE_KEY);
				result=xinge.pushSingleDevice(token, (Message) msg);
				formalresult=xinge.pushSingleDevice(token, (Message) msg);
			}
		}
		else//ios推送
		{
			xinge = new XingeApp(ThirdPartConfig.PUSH_APP_APPID_IOS,ThirdPartConfig.PUSH_APP_SECRE_KEY_IOS);
			result=xinge.pushSingleDevice(token,(MessageIOS)msg, XingeApp.IOSENV_DEV);
			formalresult=xinge.pushSingleDevice(token,(MessageIOS)msg, XingeApp.IOSENV_PROD);
		}
		
		
		
		if (formalresult.getInt("ret_code") == 0) 
		{
			log.info("正式环境已成功发送消息"  );
		}
		else
		{
			log.error(result.getString("err_msg")+"正式环境");
		}
		
		if (result.getInt("ret_code") == 0) 
		{
			log.info("已成功发送消息"  );
			return Result.successResult().setMsg("已成功发送消息至");
		}
		else
		{
			log.error(result.getString("err_msg"));
			return Result.errorResult().setMsg(result.getString("err_msg"));
		}
		
		
	}

	public static void main(String[] args) {
		XingeApp xinge = null; 
		JSONObject result = null;



		MessageIOS message = new MessageIOS();
		/*message.setAlert("{\"content-available\", 1}");
		message.setBadge(1);
		message.setExpireTime(86400);*/


		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();
		JSONObject obj = new JSONObject();
		
		aps.put("content-available", 1);
		alert.put("body", "{\"content-available\", 1}");
		
		obj.put("aps", aps); 
		aps.put("alert", alert); 
		message.setRaw(obj.toString());
		
		xinge = new XingeApp(2200318371L,"98aea322c1467bac2b709311d0cfad80");
		result=xinge.pushSingleDevice("11df37daacc35a6b3cd635b002f16a8a9179ac3acc61972eafbef358a750133d",message, XingeApp.IOSENV_DEV);
		System.out.println(result);

	}
}
