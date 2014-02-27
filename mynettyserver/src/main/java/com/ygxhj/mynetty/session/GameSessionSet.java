package com.ygxhj.mynetty.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.ygxhj.mynetty.core.Constants;

public class GameSessionSet {
	private static final GameSessionSet instance = new GameSessionSet();
	private Map<Integer, GameChannel> allChannel = new ConcurrentHashMap<Integer, GameChannel>();

	private GameSessionSet() {
	}

	public static GameSessionSet getInstance() {
		return instance;
	}

	public void addChannel(Channel channel) {
		if (channel == null) {
			return;
		}
		if (allChannel.containsKey(channel.getId())) {
			GameChannel gc = allChannel.get(channel.getId());
			gc.setChannel(channel);
			gc.setLastMessage(System.currentTimeMillis());
			allChannel.put(channel.getId(), gc);
		}else {
			GameChannel gc = new GameChannel();
			gc.setChannel(channel);
			gc.setLastMessage(System.currentTimeMillis());
			allChannel.put(channel.getId(), gc);
		}
	}

	public void removeChannel(Channel channel) {
		allChannel.remove(channel.getId());
	}

	public void cleanTimeOutChannel(){
		List<Integer> timeOutList = new ArrayList<Integer>();
		for (GameChannel gc : allChannel.values()) {
			if (System.currentTimeMillis() - gc.getLastMessage() > Constants.TIMEOUT) {
				timeOutList.add(gc.getChannel().getId());
			}
		}
		for (int id : timeOutList) {
			allChannel.remove(id);
		}
	}
	
	public List<Channel> getAllChannel() {
		List<Channel> all = new ArrayList<Channel>();
		for (GameChannel gc : allChannel.values()) {
			all.add(gc.getChannel());
		}
		return all;
	}
	
	private class GameChannel {
		
		private Channel channel;
		
		private long lastMessage;

		public Channel getChannel() {
			return channel;
		}

		public void setChannel(Channel channel) {
			this.channel = channel;
		}

		public long getLastMessage() {
			return lastMessage;
		}

		public void setLastMessage(long lastMessage) {
			this.lastMessage = lastMessage;
		}
	}
}
