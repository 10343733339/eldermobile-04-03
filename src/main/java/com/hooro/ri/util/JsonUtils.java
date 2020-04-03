package com.hooro.ri.util;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import ikidou.reflect.TypeBuilder;


public class JsonUtils 
{
    private static final Gson gson = new Gson();

    /**
     * 对象转json字符串
     * @param data
     * @return
     */
    public static String toJson(Object data) 
    {
    	return gson.toJson(data);
    }
    
    /**
     * json字符串转Pojo类
     * @param json
     * @param beanType
     * @return
     */
    public static <T> T toPojo(String json, Class<T> typeClazz) 
    {
        return gson.fromJson(json, typeClazz);
    }
    
    /**
     * json转List<T>
     * @param json
     * @param typeClazz
     * @return
     */
    public static <T>List<T> toList(String json, Class<T> typeClazz) 
    {
    	Type type = TypeBuilder
    			.newInstance(List.class)
    			.addTypeParam(typeClazz)
    			.build();
    	return gson.fromJson(json, type);
    }
    
    /**
     * json转Map<String, T>
     * @param json
     * @param typeClazz
     * @return
     */
    public static <T>Map<String, T> toMap(String json, Class<T> typeClazz) 
    {
    	Type type = TypeBuilder
    			.newInstance(Map.class)
    			.addTypeParam(String.class)
    			.addTypeParam(typeClazz)
    			.build();
    	return gson.fromJson(json, type);
    }
}
