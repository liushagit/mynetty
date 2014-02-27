package com.ygxhj.mynetty.dbutil;

import java.io.Reader;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SqlMapConfig {

	private static final Logger log = Logger.getLogger(SqlMapConfig.class);
	private static SqlMapClient sqlMap;

	public static SqlMapClient getSqlMapInstance() {
		return sqlMap;
	}
	public static void init() {
		try {
			String resource = "com/ygxhj/mynetty/core/maps/SqlMapConfig.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
		} catch (Exception e) {
			log.error("exception", e);
			throw new RuntimeException("Error initializing: " + e);
		}
	}
}
