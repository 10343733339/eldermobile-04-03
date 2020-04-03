package com.hooro.ri.util;

import java.util.List;

import com.eryansky.common.utils.StringUtils;

/**
 * 参数校验工具类
 * @author zzl
 *
 */
public class ValidateUtil {
	
	
	
	/**
	 * 判断参数是否为空
	 * @param params
	 * @return 有为空的参数返回true 否则false
	 */
	public static boolean isBlank(List<Object> params)
	{
		if(null == params || params.size()<1)
		{
			return true;
		}
		
		for (Object object : params) {
			if(null == object || StringUtils.isBlank(object.toString()))
			{
				return true;
			}
		}
		
		return false;
	}

}
