package com.ygxhj.mynetty.test;

import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.ygxhj.mynetty.client.Client;
import com.ygxhj.mynetty.client.ClientSet;
import com.ygxhj.mynetty.core.Constants;
import com.ygxhj.mynetty.server.ClientServer;

public class ClientTest {

	public static void main(String[] args) {
		ClientServer clientServer = new ClientServer();
		clientServer.initClient();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Client client2 = ClientSet.getInstance().getClient();
		JSONObject json = new JSONObject();
		try {
			json.put(Constants.CMD, "u_CP");
			json.put("name", "name_" + new Random().nextInt(100000));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		client2.sendMessage(json);
	}
}
