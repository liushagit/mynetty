package com.ygxhj.mynetty.client.handel;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class DefultHandel extends SimpleChannelHandler {
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		int index = 0;
		short contex = buffer.getShort(index);
		index += 2;
		byte[] contexs = new byte[contex];
		buffer.getBytes(index, contexs, 0, contex);
		index += contex;
//		String js = new String(contexs, "utf-8");

	}

	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Channel c = e.getChannel();
		synchronized (this) {
			notify();
		}
		this.channel = c;
	}

	byte b[];

	public void setB(byte[] b) {
		this.b = b;
	}

	public ChannelBuffer nextMessage() {
		return ChannelBuffers.wrappedBuffer(b);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		super.exceptionCaught(ctx, e);
		System.out.println(e.getCause());
	}
	
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
}
