package com.ygxhj.mynetty.server.command;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.ygxhj.mynetty.core.Constants;
import com.ygxhj.mynetty.core.model.Player;
import com.ygxhj.mynetty.message.OutputMessage;


public abstract class BaseCmd extends Command{

	Logger log = Logger.getLogger(BaseCmd.class);
	
	public JSONObject exec(JSONObject json) throws JSONException{
		Player player = new Player();
		String cmd = "cmd";
		try {
			player.setId(Integer.parseInt(json.getString(Constants.PID)));
			cmd = json.getString(Constants.CMD);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		log.info("status|" + cmd + "|" + player.getId());
		
		return done(player, json);
	}

	public abstract JSONObject done(Player player, JSONObject json) throws JSONException;

}
