package com.hooro.ri.config.netty;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 
* Title: NettyServer
* Description: Netty服务端
* 心跳测试
* Version:1.0.0  
 */
public class NettyServer implements CommandLineRunner {
        private static final int port = 9825; //设置服务端端口
        private static  EventLoopGroup group = new NioEventLoopGroup();   // 通过nio方式来接收连接和处理连接   
        private static  ServerBootstrap b = new ServerBootstrap();

        /**
         * Netty创建全部都是实现自AbstractBootstrap。
         * 客户端的是Bootstrap，服务端的则是    ServerBootstrap。
         **/
		public void run(String... arg0) throws Exception {
			// TODO Auto-generated method stub

			try {
                b.group(group);
                b.channel(NioServerSocketChannel.class);
                b.childHandler(new NettyServerFilter()); //设置过滤器
                // 服务器绑定端口监听
                ChannelFuture f = b.bind(port).sync();
                System.out.println("服务端启动成功,端口是:"+port);
                // 监听服务器关闭监听
                f.channel().closeFuture().sync();
            } catch (Exception e) {
				// TODO: handle exception
            	e.printStackTrace();
			}
			finally {
            	
                group.shutdownGracefully(); //关闭EventLoopGroup，释放掉所有资源包括创建的线程  
            }
		}
		
		public void startNetty()
		{
			try {
                b.group(group);
                b.channel(NioServerSocketChannel.class);
                b.childHandler(new NettyServerFilter()); //设置过滤器
                // 服务器绑定端口监听
                ChannelFuture f = b.bind(port).sync();
                System.out.println("服务端启动成功,端口是:"+port);
                // 监听服务器关闭监听
                f.channel().closeFuture().sync();
            } catch (Exception e) {
				// TODO: handle exception
            	e.printStackTrace();
			}
			finally {
            	
                group.shutdownGracefully(); //关闭EventLoopGroup，释放掉所有资源包括创建的线程  
            }
		}
		
}
