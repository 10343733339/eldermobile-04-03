package com.hooro.ri.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.config.netty.NettyServerHandler;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.LoginInfo;
import com.hooro.ri.service.impl.AppUserServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.util.OnLlineUtil;
import com.hooro.ri.util.PushNotiUtil;

@Component
@Configurable
@EnableScheduling
public class ScheduledTasks{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired AppUserServiceImpl appUserService;
	@Autowired LoginInfoServiceImpl loginInfoService;
	
    @Scheduled(fixedRate = 1000 * 60)
    public void reportCurrentTime(){
    	//log.info("\t 执行时间:"+dateFormat ().format (new Date ()) +"\n");
    	for(String key : NettyServerHandler.overtimeMap.keySet()) {
    		if (System.currentTimeMillis() - NettyServerHandler.overtimeMap.get(key) >= 1000 * 180  ) {
                // 空闲60s之后触发 (心跳包丢失)
    			if (NettyServerHandler.map.containsKey(key)) {
    				try {
    					NettyServerHandler.map.get(key).channel().close().sync();
            			log.error(key+"强制与"+NettyServerHandler.map.get(key).channel().remoteAddress()+"断开连接");
            			NettyServerHandler.map.remove(key);
					} catch (Exception e) {
						// TODO: handle exception
						log.error(key+"强制与"+NettyServerHandler.map.get(key).channel().remoteAddress()+"断开连接fail");
					}
    				
				}
    			
                
			}
    	}
    }

    //每2分钟执行一次
   //@Scheduled(cron = "0 *///2 *  * * * ")
  //  public void reportCurrentByCron(){
    	
   //     System.out.println ("Scheduling Tasks Examples By Cron: The time is now " + dateFormat ().format (new Date ()));
  //  }
    
    private SimpleDateFormat dateFormat(){
        return new SimpleDateFormat ("HH:mm:ss");
    }
    public static void main(String[] args) throws Exception {
		 System.out.println(System.currentTimeMillis());
		 System.out.println(System.currentTimeMillis());
	}

}