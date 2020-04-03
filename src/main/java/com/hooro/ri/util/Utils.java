package com.hooro.ri.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 工具类
 * @author Sunset
 *
 */
public class Utils {
	
	/**
	 * Object 转 Map
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Object> objectToMap(Object obj) throws Exception {    
        if(obj == null){    
            return null;    
        }   
  
        HashMap<String, Object> map = new HashMap<String, Object>();    
  
        Field[] declaredFields = obj.getClass().getDeclaredFields();    
        for (Field field : declaredFields) {    
            field.setAccessible(true);  
            map.put(field.getName(), field.get(obj));  
        }    
  
        return map;  
    } 
	
	/**
	 * 时间格式转字符串
	 * @param time
	 * @param format
	 * @return
	 */
	public static String DateToString(Date time,String format)
	{
		if(time == null)
		{
			return "";
		}
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(time);
	}

}
