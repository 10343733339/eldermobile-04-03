package com.hooro.ri.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

/**
 * 极光推送工具类
 * 
 */
@Component
public class JPushUtil {
	private static final Logger Log = LoggerFactory.getLogger(JPushUtil.class);
	// 设置好账号的app_key和masterSecret是必须的
	private static String APP_KEY ;
	private static String MASTER_SECRET ;


	//极光推送>>Android
	//Map<String, String> parm是我自己传过来的参数,可以自定义参数
	public static void jpushAndroid(String registrationId,String msg) {
		//创建JPushClient(极光推送的实例)
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		//推送的关键,构造一个payload
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.android())//指定android平台的用户
				.setAudience(Audience.registrationId(registrationId))//registrationId指定用户
	            .setMessage(Message.newBuilder()
	                        .setMsgContent(msg)
	                        //.addExtra("url", extrasparam) //释放该字段会发送两次消息，第二次消息内容是扩展字段
	                        .build())
				//.setNotification(Notification.android(parm.get("msg"), parm.get("title"), parm))  //发送内容
				.setOptions(Options.newBuilder().setApnsProduction(true).setTimeToLive(7200).build())
				// apnProduction指定开发环境 true为生产模式 false 为测试模式 (android不区分模式,ios区分模式) 不用设置也没关系
				// TimeToLive 两个小时的缓存时间
				.build();
		
		try {
			PushResult pu = jpushClient.sendPush(payload);
			Log.info("success 极光info:{}",registrationId);
			//log.  pu.toString());
		} catch (APIConnectionException e) {
			Log.error("fail 极光info:{}",e);
		} catch (APIRequestException e) {
			Log.error("fail 极光info:{}",e);
		}
	}


	  public static void main(String[] args) {
	        JPushUtil.jpushAndroid("1104a8979230d89ef5a","这里填写消息体");
	    }




    @Value("${APP_KEY}")
	public  void setAPP_KEY(String aPP_KEY) {
		APP_KEY = aPP_KEY;
	}




    @Value("${MASTER_SECRET}")
	public  void setMASTER_SECRET(String mASTER_SECRET) {
		MASTER_SECRET = mASTER_SECRET;
	}

	  
	  

}