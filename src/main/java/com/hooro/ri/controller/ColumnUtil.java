package com.hooro.ri.controller;

import java.lang.reflect.Field;

import javax.persistence.Column;

import com.hooro.ri.model.AppUser;
import com.hooro.ri.util.FieldHeader;

public class ColumnUtil {
	    public static void main(String[] args) {
	    String[] str=getColumnValue(AppUser.class);
	        for (int i = 1; i <str.length ; i++) {
	            System.out.println(str[i]);
	        }
	    }
	    public static String[] getColumnValue(Class classz) {
	            Field[] fields=classz.getDeclaredFields();
	            Field field;
	            String[] value=new String[fields.length];
	            for (int i = 0; i <fields.length ; i++) {
	                fields[i].setAccessible(true);
	            }
	            for(int i = 1;i<fields.length ; i++){
	                try {
	                    field=classz.getDeclaredField(fields[i].getName());
	                    FieldHeader column=field.getAnnotation(FieldHeader.class); //获取指定类型注解
	                    if(column!=null){
	                        value[i]=column.fieldName();
	                    }
	                } catch (NoSuchFieldException e) {
	                    e.printStackTrace();
	                }
	            }
	            return value;
	    }
	}


