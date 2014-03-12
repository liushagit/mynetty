package com.ygxhj.mynetty.handler;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.json.JSONObject;

import com.ygxhj.mynetty.config.GlobalConfig;
import com.ygxhj.mynetty.core.Constants;
import com.ygxhj.mynetty.server.GameServer;
import com.ygxhj.mynetty.util.MD5;
import com.ygxhj.mynetty.work.RequestPool;

public class DefultHandler extends SimpleChannelHandler{

	private Logger log = Logger.getLogger(DefultHandler.class);
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		int index = 0;
		short begin = buffer.getShort(index);
		index += 2;
		byte[] begins = new byte[begin];
		buffer.getBytes(index, begins, 0, begin);
		index += begin;
		String b = new String(begins,"utf-8");
		if (!Constants.BEGIN.equals(b)) {
			buffer.clear();
			log.error("begin is error");
			return;
		}
		
		short contex = buffer.getShort(index);
		index += 2;
		byte[] contexs = new byte[contex];
		buffer.getBytes(index, contexs, 0, contex);
		index += contex;
		String js = new String(contexs,"utf-8");
		JSONObject json = new JSONObject(js);
		log.info("receive|" + json.toString());
		
		short sign = buffer.getShort(index);
		index += 2;
		byte[] signs = new byte[sign];
		buffer.getBytes(index, signs, 0, sign);
		String resSing = new String(signs,"utf-8");
		index += sign;
		
		short end = buffer.getShort(index);
		index += 2;
		byte[] ends = new byte[end];
		buffer.getBytes(index, ends, 0, end);
		
		String en = new String(ends,"utf-8");
		log.info("end|" + en);
		if (!Constants.END.equals(en)) {
			buffer.clear();
			log.error("end is error");
			return;
		}
		String key = GlobalConfig.getZone(GlobalConfig.zoneId).getMd5Key();
		
		String newSign = MD5.encode(js, key);
		newSign = newSign.toLowerCase();
		resSing = resSing.toLowerCase();
		
		if (!newSign.equals(resSing)) {
			log.error("MD5 is error");
			return;
		}
		RequestPool.inst.addRequest(json, ctx.getChannel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	@Override
	public void closeRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("channelConnected");
	}

}
