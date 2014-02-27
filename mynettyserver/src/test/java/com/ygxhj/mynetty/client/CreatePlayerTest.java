package com.ygxhj.mynetty.client;

/**********************************************
 * @author  Simon.Hoo(simon.hoo.it@gmail.com)
 * @contact MSN: simon_hoo@msn.com
 * @Create  July 8, 2011
 ********************************************/

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.Executors;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.json.JSONObject;

import com.ygxhj.mynetty.client.handel.DefultHandel;
import com.ygxhj.mynetty.core.Constants;
import com.ygxhj.mynetty.message.OutputMessage;
import com.ygxhj.mynetty.util.MD5;

public class CreatePlayerTest extends AbstractJavaSamplerClient {
	private String serverIp;
	private int serverPort;
	private DefultHandel handler = new DefultHandel();
	
	public SampleResult runTest(JavaSamplerContext sc) {
		SampleResult sr = new SampleResult();
		try {
		//System.out.println("test===========");
		serverIp = sc.getParameter("serverIp");
		serverPort = Integer.valueOf(sc.getParameter("serverPort"));
		
		sr.setSampleLabel("Socket Test");
		byte b[] = getStringss();
		handler.setB(b);
		connect();
		synchronized (handler) {
			handler.wait(3000);
		}
		//System.out.println("test==" + handler.getChannel().isWritable());
		handler.getChannel().write(handler.nextMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sr;
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("serverIp", "192.168.1.243");
		params.addArgument("serverPort", "8092");
		return params;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {
		super.setupTest(context);
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		super.teardownTest(context);
	}

	public static void main(String[] args) {
		CreatePlayerTest testSocket = new CreatePlayerTest();
		Arguments params = new Arguments();
		params.addArgument("serverIp", "192.168.1.243");
		params.addArgument("serverPort", "8092");
		JavaSamplerContext sc = new JavaSamplerContext(params);
		testSocket.runTest(sc);
	}
	public void connect() {
		//System.out.println("connect===========begin");
		if (handler.getChannel() != null && handler.getChannel().isWritable()) {
			return;
		}
		//System.out.println("connect===========begin11");
		// Client服务启动器
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		// 设置一个处理服务端消息和各种消息事件的类(Handler)
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addFirst("decoder", new LengthFieldBasedFrameDecoder(
						32767, 0, 4, 0, 4));
				pipeline.addLast("handler", handler);
				return pipeline;
			}
		});
		
		//System.out.println("connect===========begin222");
		bootstrap.connect(new InetSocketAddress(serverIp, serverPort));
		//System.out.println("connect===========begin333");
	}

	public byte[] getStringss() {
		JSONObject json = new JSONObject();
		try {
			json.put(Constants.CMD, "u_CP");
			json.put("name", "name" + new Random().nextInt(10000));
			String begin = "1000";
			String end = "2000";

			OutputMessage om = new OutputMessage();
			om.putString(begin);
			//System.out.println("====" + json.toString());
			String msg = json.toString();
			om.putString(msg);
			String sign = MD5.encode(msg, "mynetty");
			//System.out.println("msg|" + msg);
			om.putString(sign);
			om.putString(end);
			OutputMessage om1 = new OutputMessage();
			om1.putInt(om.getBytes().length);
			for (byte ii : om.getBytes()) {
				om1.putByte(ii);
			}

			return om1.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[1];
		}
	}

}