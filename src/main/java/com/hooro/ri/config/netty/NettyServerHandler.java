package com.hooro.ri.config.netty;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eryansky.common.model.Result;
import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.config.SpringBeanUtil;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.service.impl.NavigationServiceImpl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.sf.json.JSONObject;

/**
 * 
 * Title: HelloServerHandler
 * Description:  服务端业务逻辑
 * Version:1.0.0  
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	//多线程中无法自动注入、这里手动注入
	private	static NavigationServiceImpl navigationService=(NavigationServiceImpl)SpringBeanUtil.getBeanByName("navigationService"); 

	//多线程中无法自动注入、这里手动注入
	private	static MobileServiceImpl mobileService=(MobileServiceImpl)SpringBeanUtil.getBeanByName("mobileService"); 

	public static  Map< String,ChannelHandlerContext> map=new ConcurrentHashMap< String,ChannelHandlerContext>();
	
	public static  Map< String,Long>  overtimeMap =new ConcurrentHashMap< String,Long>();
	 /**
     * 心跳丢失次数
     */
    private int counter = 0;
    
    
	/**
	 * 超时处理
	 * 如果60秒没有接受客户端的心跳，就触发;
	 * 如果超过两次，则直接关闭;
	 */

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE) || event.state().equals(IdleState.WRITER_IDLE)){
                    // 空闲60s之后触发 (心跳包丢失)
                	String key = getKey(ctx);
                	map.remove(key);
                    ctx.channel().close().sync();
                    log.error(key+"已与"+ctx.channel().remoteAddress()+"断开连接");
                    System.out.println(key+"已与"+ctx.channel().remoteAddress()+"断开连接");
               
            }
 
        }

    }

	/**
	 * 业务逻辑处理
	 */
	@Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
		log.info("接收的内容\n"+msg);
		ReceiveData data = null;
		try {
			JSONObject jsonObject=JSONObject.fromObject(msg);
			data = (ReceiveData) JSONObject.toBean(jsonObject, ReceiveData.class);
			
			if(!data.checkData().equals(ReceiveData.STATUS)) {
				log.info("非法参数");
				ctx.writeAndFlush(JSONObject.fromObject(Result.warnResult().setMsg("faile,Illegal parameter")).toString());
				return ;
			}
			if(StringUtils.isNotEmpty(data.getAppUser())) {
				overtimeMap.put(data.getAppUser(), System.currentTimeMillis());
				map.put(data.getAppUser(), ctx);
				ctx.writeAndFlush(JSONObject.fromObject(Result.successResult().setMsg("success")).toString());
				return ;
			}
			
			Mobile mobile=mobileService.findUniqueBy("simMark", data.getSimMark());
			if(mobile == null) {
				ctx.writeAndFlush(JSONObject.fromObject(Result.warnResult().setMsg("Invalid simmark,disconnected")).toString());
				return ;
			}
			data.setMobile(mobile);
		}catch(Exception exception) {
			ctx.writeAndFlush(JSONObject.fromObject(Result.warnResult().setMsg("Please fill in JSON format parameters")).toString());
			return ;
		}
		//连接放入map
		overtimeMap.put(data.getSimMark(), System.currentTimeMillis());
		map.put(data.getSimMark(),ctx);
		navigationService.save(data.toNavigation());
		ctx.writeAndFlush(JSONObject.fromObject(Result.successResult().setMsg("success")).toString());
		
	}  

	/**
	 * 异常处理
	 */
	@Override  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
		String key=getKey(ctx);
		if(key!=null)
			map.remove(key);

		System.out.println("出现异常连接");
		cause.printStackTrace();  
		ctx.close();  
	} 
	/*
	 * 建立连接时，返回消息
	 * 
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
		log.info("建立连接成功"+new Date());
		super.channelActive(ctx);
	}

	/** 根据当前连接获得key
	 * @param ctx
	 * @return
	 */
	synchronized public static String getKey(ChannelHandlerContext ctx)
	{
		for (Map.Entry<String,ChannelHandlerContext > entry : map.entrySet()) { 
			if(entry.getValue().equals(ctx))
			{
				return  entry.getKey();
			}
		}
		return null;
	}





}



