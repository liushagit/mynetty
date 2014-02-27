package com.ygxhj.mynetty.server;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.ygxhj.mynetty.dbutil.DBEngine;
import com.ygxhj.mynetty.dbutil.DBService;
import com.ygxhj.mynetty.dbutil.DBThread;
import com.ygxhj.mynetty.dbutil.SqlMapConfig;
import com.ygxhj.mynetty.util.FileUtil;

public class DBInit {

	private static Logger log = Logger.getLogger(DBInit.class);

	public static void initDB() {
		log.info("init db --- ");
		InputStream in = DBInit.class.getClassLoader().getResourceAsStream("db.properties");
		FileUtil.createNewFile(System.getProperty("user.dir") + File.separator + "db.properties", in);
		SqlMapConfig.init();

		DBEngine.initialize();
		DBThread thread = new DBThread();
		thread.start();
		DBService.initialize();
	}
}
