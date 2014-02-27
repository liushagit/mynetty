package com.ygxhj.mynetty.work;

import java.util.concurrent.LinkedBlockingQueue;

import org.jboss.netty.channel.Channel;
import org.json.JSONObject;

public class RequestPool {

	public static final RequestPool inst = new RequestPool();
	public LinkedBlockingQueue<NotifyItem> requestQueue = new LinkedBlockingQueue<NotifyItem>();

	private RequestPool(){}

	public class NotifyItem {

		private long requestTime;
		private JSONObject json;
		private Channel channel;
		public long getRequestTime() {
			return requestTime;
		}
		public void setRequestTime(long requestTime) {
			this.requestTime = requestTime;
		}
		public JSONObject getJson() {
			return json;
		}
		public void setJson(JSONObject json) {
			this.json = json;
		}
		public Channel getChannel() {
			return channel;
		}
		public void setChannel(Channel channel) {
			this.channel = channel;
		}
		
	}

	/**
	 * 添加个请求到队列
	 * 
	 * @param session
	 *            请求对应的Session
	 * @param request
	 *            请求数据
	 * @param recvTime
	 *            接收时间
	 */
	public void addRequest(JSONObject json,Channel channel) {
		NotifyItem ni = new NotifyItem();
		ni.setRequestTime(System.currentTimeMillis());
		ni.setJson(json);
		ni.setChannel(channel);
		requestQueue.add(ni);
	}

	public LinkedBlockingQueue<NotifyItem> getRequestQueue() {
		return requestQueue;
	}
}
