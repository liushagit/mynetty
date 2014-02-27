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
		// 设置一个处理客户端消息和各种消息事件的类(Handler)
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addFirst("decoder", new LengthFieldBasedFrameDecoder(
						32767, 0, 4, 0, 4));
				pipeline.addLast("handler", new DefultHandler());
				return pipeline;
			}
		});
		// 开放8000端口供客户端访问。
		bootstrap.bind(new InetSocketAddress(host,port));
	}

	// private static class HelloServerHandler extends SimpleChannelHandler {
	//
	// /**
	// * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."
	// *
	// * @alia OneCoder
	// * @author lihzh
	// */
	// @Override
	// public void channelConnected(ChannelHandlerContext ctx,
	// ChannelStateEvent e) {
	// }
	//
	// @Override
	// public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	// throws Exception {
	// System.out.println("Hello world, I'm server.messageReceived");
	// ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
	// int index = 0;
	// short begin = buffer.getShort(index);
	// index += 2;
	// byte[] begins = new byte[begin];
	// buffer.getBytes(index, begins, 0, begin);
	// index += begin;
	// String b = new String(begins,"utf-8");
	// if (!"1000".equals(b)) {
	// buffer.clear();
	// System.err.println("begin is error");
	// return;
	// }
	// System.out.println("==="+b);
	//
	// short cmd = buffer.getShort(index);
	// index += 2;
	// byte[] cmds = new byte[cmd];
	// buffer.getBytes(index, cmds, 0, cmd);
	// index += cmd;
	// System.out.println("==="+new String(cmds,"utf-8"));
	//
	//
	// short contex = buffer.getShort(index);
	// int iiii = Short.MAX_VALUE;
	// index += 2;
	// byte[] contexs = new byte[contex];
	// buffer.getBytes(index, contexs, 0, contex);
	// index += contex;
	// String js = new String(contexs,"utf-8");
	// System.out.println("==="+js);
	// JSONObject json = new JSONObject(js);
	//
	// short sign = buffer.getShort(index);
	// index += 2;
	// byte[] signs = new byte[sign];
	// buffer.getBytes(index, signs, 0, sign);
	// index += sign;
	// System.out.println("====" + new String(signs,"utf-8"));
	//
	// short end = buffer.getShort(index);
	// index += 2;
	// byte[] ends = new byte[end];
	// buffer.getBytes(index, ends, 0, end);
	// System.out.println("==="+new String(ends,"utf-8"));
	//
	//
	// Channel c = e.getChannel();
	//
	// // while (c.isWritable()) {
	// c.write(nextMessage());
	// }
	//
	// private ChannelBuffer nextMessage() {
	// return ChannelBuffers.wrappedBuffer(getStringss());
	// }
	//
	// public static byte[] getStringss(){
	// JSONObject json = new JSONObject();
	// try {
	// for (int i = 0; i <2000; i++) {
	// json.put("" + i, "" + i);
	// }
	//
	// } catch (JSONException e) {
	// }
	// try {
	// byte bb[] = json.toString().getBytes("utf-8");
	// String begin = "1000";
	// String cmd = "test";
	// String end = "2000";
	//
	// OutputMessage om = new OutputMessage();
	// om.putString(begin);
	// om.putString(cmd);
	// System.out.println("===="+json.toString());
	// om.putString(json.toString());
	// String sign = MD5.encode(json.toString());
	// om.putString(sign);
	// om.putString(end);
	// OutputMessage om1 = new OutputMessage();
	// om1.putInt(om.getBytes().length);
	// for (byte ii : om.getBytes()) {
	// om1.putByte(ii);
	// }
	//
	// //
	// return om1.getBytes();
	// } catch (Exception e) {
	// e.printStackTrace();
	// return new byte[1];
	// }
	// }
	//
	// @Override
	// public void closeRequested(ChannelHandlerContext ctx,
	// ChannelStateEvent e) throws Exception {
	// }
	//
	// @Override
	// public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	// throws Exception {
	// super.exceptionCaught(ctx, e);
	// }
	// }

}
