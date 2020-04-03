package com.hooro.ri.util;

import java.util.Hashtable;
import java.util.Map;

public class OnLlineUtil implements Runnable {
	
	public static Integer  count=1;
    public static Map<String,Long>  onLine=new Hashtable<String,Long>();
    private static OnLlineUtil onLlineUtil=new OnLlineUtil();
    public  OnLlineUtil(){}
    public  static OnLlineUtil getOnLlineUtil()
    {
    	return onLlineUtil;
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
    
    
}
