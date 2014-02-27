package com.ygxhj.mynetty.client;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.json.JSONException;
import org.json.JSONObject;

import com.ygxhj.mynetty.config.GlobalConfig;
import com.ygxhj.mynetty.core.Constants;
import com.ygxhj.mynetty.message.OutputMessage;
import com.ygxhj.mynetty.server.GameServer;
import com.ygxhj.mynetty.util.MD5;

public class HelloClient {

//	public static void main(String args[]) {
//		final HelloClientHandler handler = new HelloClientHandler();
//		byte b[] = getStringss();
//		handler.setB(b);
////		while (true) {
//			// Client服务启动器
//			ClientBootstrap bootstrap = new ClientBootstrap(
//					new NioClientSocketChannelFactory(
//							Executors.newCachedThreadPool(),
//							Executors.newCachedThreadPool()));
//			// 设置一个处理服务端消息和各种消息事件的类(Handler)
//			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
//				public ChannelPipeline getPipeline() throws Exception {
//					ChannelPipeline pipeline = Channels.pipeline();
//					pipeline.addFirst("decoder", new LengthFieldBasedFrameDecoder(32767,0,4,0,4));
//					pipeline.addLast("handler", handler);
//					return pipeline;
//				}
//			});
//			// 连接到本地的8000端口的服务端
//			bootstrap.connect(new InetSocketAddress("127.0.0.1", 8092));
////		}
//		
//	}

	private static class HelloClientHandler extends SimpleChannelHandler {

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			
			System.out.println("Hello world, I'm server.messageReceived");
			ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
			int index = 0;
//			short begin = buffer.getShort(index);
//			index += 2;
//			byte[] begins = new byte[begin];
//			buffer.getBytes(index, begins, 0, begin);
//			index += begin;
//			String b = new String(begins,"utf-8");
//			if (!"1000".equals(b)) {
//				buffer.clear();
//				System.err.println("begin is error");
//				return;
//			}
//			System.out.println("==="+b);
//			
//			short cmd = buffer.getShort(index);
//			index += 2;
//			byte[] cmds = new byte[cmd];
//			buffer.getBytes(index, cmds, 0, cmd);
//			index += cmd;
//			System.out.println("==="+new String(cmds,"utf-8"));
			
			
			short contex = buffer.getShort(index);
			int iiii = Short.MAX_VALUE;
			index += 2;
			byte[] contexs = new byte[contex];
			buffer.getBytes(index, contexs, 0, contex);
			index += contex;
			String js = new String(contexs,"utf-8");
			System.out.println("==="+js);
			JSONObject json = new JSONObject(js);
			
//			short sign = buffer.getShort(index);
//			index += 2;
//			byte[] signs = new byte[sign];
//			buffer.getBytes(index, signs, 0, sign);
//			index += sign;
//			System.out.println("====" + new String(signs,"utf-8"));
//			
//			short end = buffer.getShort(index);
//			index += 2;
//			byte[] ends = new byte[end];
//			buffer.getBytes(index, ends, 0, end);
//			System.out.println("==="+new String(ends,"utf-8"));
			
		}

		/**
		 * 当绑定到服务端的时候触发，打印"Hello world, I'm client."
		 * 
		 * @alia OneCoder
		 * @author lihzh
		 */
		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) {
//			System.out.println("Hello world, I'm client.");
			Channel c = e.getChannel();
			
//			while (c.isWritable()) {
				c.write(nextMessage());
//				try {
//					Thread.sleep(1);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
			
//			System.out.println(e.getChannel().isWritable());
//				c.close();
				System.out.println(e.getChannel().isWritable());
//				c.write(nextMessage());
		}

		byte b[];

		public void setB(byte[] b) {
			this.b = b;
		}

		private ChannelBuffer nextMessage() {
			System.out.println("-----"+b.length);
			return ChannelBuffers.wrappedBuffer(b);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
				throws Exception {
			super.exceptionCaught(ctx, e);
			System.out.println(e.getCause());
		}

	}
	
	public static byte[] getStringss(){
		JSONObject json = new JSONObject();
		try {
			json.put(Constants.CMD, "u_CP");
			json.put("name", "name" + new Random().nextInt(10000));
			
		} catch (JSONException e) {
		}
		try {
			byte bb[] = json.toString().getBytes("utf-8");
			String begin = "1000";
			String cmd = "test";
			String end = "2000";
			
			OutputMessage om = new OutputMessage();
			om.putString(begin);
//			om.putString(cmd);
			System.out.println("===="+json.toString());
			String msg = json.toString();
			om.putString(msg);
			String sign = MD5.encode(msg,"mynetty");
			System.out.println("msg|" + msg);
			om.putString(sign);
			om.putString(end);
			OutputMessage om1 = new OutputMessage();
			om1.putInt(om.getBytes().length);
			for (byte ii : om.getBytes()) {
				om1.putByte(ii);
			}
			
//			
			return om1.getBytes(); 
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[1];
		}
	}
	
	public static JSONObject getJson(){
		JSONObject json = new JSONObject();
		try {
			json.put("1", "1");
			json.put("2", "2");
			json.put("3", "3");
			json.put("4", "4");
		} catch (JSONException e) {
		}
		return json;
	}
}
