package com.ygxhj.mynetty.server.command;

import org.json.JSONException;
import org.json.JSONObject;

import com.ygxhj.mynetty.core.model.Player;
import com.ygxhj.mynetty.message.OutputMessage;

public abstract class Command {

	public abstract JSONObject exec(JSONObject json) throws JSONException;
	
	public abstract JSONObject done(Player player,JSONObject json) throws JSONException;
}
