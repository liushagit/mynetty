package com.ygxhj.mynetty.client;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class ClientSet {

	private static final ClientSet instance = new ClientSet();
	private LinkedBlockingQueue<Client> requestQueue = new LinkedBlockingQueue<Client>();
	private ClientSet(){}
	public static ClientSet getInstance() {
		return instance;
	}
	
	public Client getClient(){
		try {
			return requestQueue.poll(100, TimeUnit.MICROSECONDS);
		} catch (InterruptedException e) {
		}
		return null;
	}
	
	public void backClient(Client client){
		requestQueue.add(client);
	}
}
