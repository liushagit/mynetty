package com.ygxhj.mynetty.server;

import org.apache.log4j.Logger;

import com.ygxhj.mynetty.config.GlobalConfig;
import com.ygxhj.mynetty.config.Zone;
import com.ygxhj.mynetty.server.command.CommandSet;
import com.ygxhj.mynetty.work.WorkThread;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class GameServer {
	
	
	
	
	

	private static Logger log = Logger.getLogger(GameServer.class);
	public static void main(String[] args) {
		
		long begin = System.currentTimeMillis();
		long beginPre = begin;
		//1、加载cmd信息
		log.debug("init command begin");
		beginPre = System.currentTimeMillis();
		CommandSet cmdSet = CommandSet.getInstance();
		cmdSet.loadCommand();
		log.info("command : " + cmdSet.printCmdSet());
		log.debug("init command end " + (System.currentTimeMillis() - beginPre) + "ms");
		
		//2、初始化配置文件
		log.debug("init GlobalConfig begin");
		beginPre = System.currentTimeMillis();
		GlobalConfig config = new GlobalConfig();
		config.init();
		Zone zone = GlobalConfig.getZone(GlobalConfig.zoneId);
		log.debug("init GlobalConfig end " + (System.currentTimeMillis() - beginPre) + "ms");
		
		//3、初始化netty
		log.debug("init GlobalConfig begin");
		beginPre = System.currentTimeMillis();
		NettyServer server = new NettyServer();
		server.init(zone.getProxyHost() ,zone.getProxyPort());
		log.info("init netty server :" + zone.getProxyHost() + ":" + zone.getProxyPort());
		log.debug("init NettyServer end " + (System.currentTimeMillis() - beginPre) + "ms");
		
		//4、初始化DB
		DBInit.initDB();
		//5、初始化资源
		log.info("init GameServer end " + (System.currentTimeMillis() - begin) + "ms");
		
		
		//6、开启workthread
		for (int i = 0; i < 8; i++) {
			new WorkThread().start();
		}
	}
	
	
	
	
//	public static void main(String[] args) {
//		File file = new File("d:\\phoneName.txt");
//		BufferedReader buffer;
//		PrintWriter pw;
//		try {
//			buffer = new BufferedReader(new FileReader(file));
//			String s = null;
//			Map<String, String> map = new HashMap<String, String>();
//			String sssss[];
//			while ((s = buffer.readLine()) != null) {
//				System.out.println(s);
//				sssss = s.split("_");
//				map.put(sssss[1], sssss[1]);
//			}
//			
//			pw = new PrintWriter(new File("d:\\phoneName_t.txt"));
//			for (String ss : map.values()) {
//				pw.println(ss);
//			}
//			pw.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//		}
//		
//	}
	
}
