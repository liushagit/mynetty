package com.ygxhj.mynetty.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.json.JSONObject;

import com.ygxhj.mynetty.client.handel.DefultHandel;
import com.ygxhj.mynetty.config.GlobalConfig;
import com.ygxhj.mynetty.core.Constants;
import com.ygxhj.mynetty.message.InputMessage;
import com.ygxhj.mynetty.message.OutputMessage;
import com.ygxhj.mynetty.util.MD5;

public class Client {

	private String serverIp;
	private int serverPort;

	public Client(String serverIp, int serverPort) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		connect();
	}

	private DefultHandel handler = new DefultHandel();

	private void connect() {
		if (handler.getChannel() != null && handler.getChannel().isWritable()) {
			return;
		}
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addFirst("decoder", new LengthFieldBasedFrameDecoder(
						32767, 0, 4, 0, 4));
				pipeline.addLast("handler", handler);
				return pipeline;
			}
		});

		bootstrap.connect(new InetSocketAddress(serverIp, serverPort));
	}

	public void sendMessage(JSONObject message) {
		if (handler.getChannel() == null || !handler.getChannel().isWritable()) {
			synchronized (handler) {
				try {
					handler.wait(3000);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
		
		OutputMessage om = new OutputMessage();
		om.putString(Constants.BEGIN);
		String msg = message.toString();
		om.putString(msg);
		String sign = MD5.encode(msg, GlobalConfig.getZone(GlobalConfig.zoneId).getMd5Key());
		om.putString(sign);
		om.putString(Constants.END);
		OutputMessage om1 = new OutputMessage();
		om1.putInt(om.getBytes().length);
		for (byte ii : om.getBytes()) {
			om1.putByte(ii);
		}
		handler.getChannel().write(nextMessage(om1.getBytes()));
	}

	private ChannelBuffer nextMessage(byte[] message) {
		return ChannelBuffers.wrappedBuffer(message);
	}
}
