package com.ygxhj.mynetty.work;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.json.JSONException;
import org.json.JSONObject;

import com.ygxhj.mynetty.core.Constants;
import com.ygxhj.mynetty.message.OutputMessage;
import com.ygxhj.mynetty.server.command.Command;
import com.ygxhj.mynetty.server.command.CommandSet;
import com.ygxhj.mynetty.work.RequestPool.NotifyItem;



public class WorkThread extends Thread{
	private Logger log = Logger.getLogger(WorkThread.class);
	private int warnSize = 100;
	private int timeOut = 2000;
	@Override
	public void run() {
		LinkedBlockingQueue<NotifyItem> requestQueue = null;
		NotifyItem notify = null;
		while (true) {
			try {
				requestQueue = RequestPool.inst.getRequestQueue();
				notify = requestQueue.poll(100, TimeUnit.MICROSECONDS);
				while(notify != null) {
					if (System.currentTimeMillis() - notify.getRequestTime() < timeOut) {
						exec(notify);
					}else {
						String cmd = "cmd";
						String pid = "pid";
						JSONObject json = notify.getJson();
						try {
							cmd = json.getString(Constants.CMD);
							pid = json.getString(Constants.PID);
						} catch (Exception e) {
						}
						log.error("timeout:" + pid + "|" + cmd);
					}
					int size = requestQueue.size();
					if (size > warnSize) {
						log.warn("requestQueue size is:" + size + "..........");
					}
					notify = requestQueue.poll(100, TimeUnit.MICROSECONDS);
				}
				
			} catch (Exception e) {
				log.error("workThread exception", e);
			}
		}
	}
	
	private void exec(NotifyItem notify){
		try {
			String c = notify.getJson().getString(Constants.CMD);
			String cbegin = notify.getJson().getString(Constants.CMD);
			Command cmd = CommandSet.getInstance().getCmd(c);
			if (cmd == null) {
				log.error("cmd is null :" + c + "....");
				return;
			}
			long begin = System.currentTimeMillis();
			JSONObject result = cmd.exec(notify.getJson());
			if (!result.isNull(Constants.CMD)) {
				c = result.getString(Constants.CMD);
			}
			result.put(Constants.CMD, c);
			log.info("status|"+cbegin+"|" + c + "|" + result.getString(Constants.STATUS) + "|" + (System.currentTimeMillis() - begin));
			resp(notify, result);
		} catch (JSONException e) {
			log.error("exec exception", e);
		}
	}
	
	private void resp(NotifyItem notify, JSONObject json){
		Channel channel = notify.getChannel();
		OutputMessage out = new OutputMessage();
		out.putString(json.toString());
		OutputMessage result = new OutputMessage();
		byte b[] = out.getBytes();
		result.putInt(b.length);
		for (byte b1 : b) {
			result.putByte(b1);
		}
		channel.write(nextMessage(result));
	}
	
	private ChannelBuffer nextMessage(OutputMessage out) {
		return ChannelBuffers.wrappedBuffer(out.getBytes());
	}

}
