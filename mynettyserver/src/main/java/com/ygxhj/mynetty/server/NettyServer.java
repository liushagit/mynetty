package com.ygxhj.mynetty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import com.ygxhj.mynetty.handler.DefultHandler;

public class NettyServer {

	public void init(String host,int port) {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addFirst("decoder", new LengthFieldBasedFrameDecoder(
						32767, 0, 4, 0, 4));
				pipeline.addLast("handler", new DefultHandler());
				return pipeline;
			}
		});
		bootstrap.bind(new InetSocketAddress(host,port));
	}


}
