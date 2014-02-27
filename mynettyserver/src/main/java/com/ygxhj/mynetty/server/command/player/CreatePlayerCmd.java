package com.ygxhj.mynetty.server.command.player;

import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.ygxhj.mynetty.core.Constants;
import com.ygxhj.mynetty.core.model.Player;
import com.ygxhj.mynetty.dbutil.DBService;
import com.ygxhj.mynetty.dbutil.GlobalGenerator;
import com.ygxhj.mynetty.message.OutputMessage;
import com.ygxhj.mynetty.server.command.BaseCmd;

public class CreatePlayerCmd extends BaseCmd{

	@Override
	public JSONObject exec(JSONObject json) throws JSONException {
		String name = json.getString("name");
		Player player = new Player();
		Date date = new Date();
		player.setId(GlobalGenerator.getInstance().getReusedIdForNewObj(Player.TABLE_NAME));
		player.setCreateTime(date);
		player.setLoginTime(date);
		player.setName(name);
		DBService.commitNoCacheUpdate(player);
		log.info(player.getId() + "|" + player.getName() + "|" + player.getCreateTime() + "|" + player.getLoginTime());
		JSONObject result = new JSONObject();
		result.put(Constants.STATUS, Constants.STATUS_SUCC);
		return result;
	}
	private Logger log = Logger.getLogger(CreatePlayerCmd.class);
	@Override
	public JSONObject done(Player player, JSONObject json) {
		log.info("CreatePlayerCmd|" + player.getId());
		OutputMessage out = new OutputMessage();
		out.putString(json.toString());
		return json;
	}

}
