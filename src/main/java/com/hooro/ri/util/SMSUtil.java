package com.hooro.ri.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.hooro.ri.config.ThirdPartConfig;

/**
 * 消息发送工具类，包含环信实时消息发送及短信发送
 * @author WKJ
 *
 */
@Component
public class SMSUtil
{

	private static Map<String, Map<String, Object>> codeInfo = new Hashtable<String, Map<String, Object>>();
	/**
	 * 发送短信验证码
	 * @param phone
	 * @return 是否发送成功
	 */
	public static boolean sendSMSCode(String phone)
	{
		String code = MD5Util.getRandomNumString(6);
		Timestamp time = new Timestamp(new Date().getTime());

		Map<String, Object> codeMap = new HashMap<String, Object>();
		codeMap.put("code", code);
		codeMap.put("time", time);
		codeInfo.put(phone, codeMap);

		String msg = "感谢您使用老人机助手，您的短信验证码为：" + code + "，有效期为3分钟。【鸡跑跑】";

		String status = sendSMS(phone, msg);
		System.out.println(status+"====");
		if (status != null && status.equals("ok")) return true;

		return false;
	}

	/**
	 * 检查验证码
	 * @param phone
	 * @param code
	 * @return 0：错误，1：成功，2：过期
	 */
	public static Integer checkSMSCode(String phone, String code)
	{
		if (codeInfo.get(phone) == null) return 0;

		Map<String, Object> codeMap = codeInfo.get(phone);
		Timestamp nowTime = new Timestamp(new Date().getTime());
		Timestamp codeTime = (Timestamp)codeMap.get("time");

		if (nowTime.getTime() - codeTime.getTime() > 180000)
		{
			codeInfo.remove(phone);
			return 2;
		}

		if (!codeMap.get("code").equals(code)) return 0;

		codeInfo.remove(phone);
		return 1;
	}

	/**
	 * 发送任意内容短信
	 * @param phone
	 * @param message
	 * @return 发送状态描述
	 */
	public static String sendSMS(String phone, String message)
	{
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(ThirdPartConfig.SMS_SEND_URL);
		target = target.register(HttpAuthenticationFeature.basic("api", ThirdPartConfig.SMS_APP_KEY));

		Form formData = new Form();
		formData.param("mobile", phone);
		formData.param("message", message);

		String textEntity = target.request().post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED), String.class);

		Map<String, Object> responseMap = JsonUtils.toMap(textEntity, Object.class);
		return (String) responseMap.get("msg");
	}
	public static void main(String[] args) {
		sendSMS("17665368506","测试");
	}
}
