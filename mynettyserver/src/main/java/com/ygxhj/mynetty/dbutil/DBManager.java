package com.ygxhj.mynetty.dbutil;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;

public class DBManager {

	private static final Logger log = Logger.getLogger(DBManager.class);
	/******************************************************************************************
	 * DAO 缓存
	 * ****************************************************************************************/
	private static Map<String, Object> daoMap = new HashMap<String, Object>();

	public static Object getDao(Class clazz) {
		Object o = daoMap.get(clazz.getName());
		if (o != null)
			return o;
		try {
			o = clazz.getConstructor(SqlMapClient.class).newInstance(SqlMapConfig.getSqlMapInstance());
			daoMap.put(clazz.getName(), o);
			return o;
		} catch (Exception e) {
			log.error("exception", e);
		}
		return null;
	}

}
