package com.ygxhj.mynetty.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.ygxhj.mynetty.core.dao.PlayerDAO;
import com.ygxhj.mynetty.core.dao.PlayerDAOImpl;
import com.ygxhj.mynetty.core.model.Player;
import com.ygxhj.mynetty.core.model.PlayerExample;
import com.ygxhj.mynetty.dbutil.DBManager;

public class PlayerSet {

	private static final PlayerSet instance = new PlayerSet();

	private Logger log = Logger.getLogger(PlayerSet.class);
	private Map<Integer, Player> playerSet = new ConcurrentHashMap<Integer, Player>();
	
	private PlayerSet(){}
	public static PlayerSet getInstance() {
		return instance;
	}
	
	public Player getPlayer(int playerId){
		Player player = playerSet.get(playerId);
		if (player == null) {
			player = loadPlayerById(playerId);
			if (player != null) {
				playerSet.put(player.getId(), player);
			}
		}
		return player;
	}
	
	private Player loadPlayerById(int playerId){
		PlayerDAO dao = (PlayerDAO)DBManager.getDao(PlayerDAOImpl.class);
		PlayerExample example = new PlayerExample();
		example.createCriteria().andIdEqualTo(playerId);
		Player player = null;
		try {
			List<Player> list = dao.selectByExample(example);
			if (list != null && list.size() > 0) {
				player = list.get(0);
			}
		} catch (SQLException e) {
			log.error("loadPlayer exception :" , e);
		}
		return player;
	}
	
	public void cleanPlayer(){
		List<Integer> offlinePlayer = new ArrayList<Integer>();
		
		for (Player player : playerSet.values()) {
			if (System.currentTimeMillis() - player.getLastMessage() > Constants.TIMEOUT) {
				offlinePlayer.add(player.getId());
			}
		}
		for (int id : offlinePlayer) {
			playerSet.remove(id);
		}
	}
	
}
