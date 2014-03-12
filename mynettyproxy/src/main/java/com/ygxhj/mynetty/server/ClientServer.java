package com.ygxhj.mynetty.server;

import org.apache.log4j.Logger;

import com.ygxhj.mynetty.client.Client;
import com.ygxhj.mynetty.client.ClientSet;
import com.ygxhj.mynetty.config.GlobalConfig;
import com.ygxhj.mynetty.config.Zone;

public class ClientServer {
	private Logger log = Logger.getLogger(ClientServer.class);

	public void initClient() {
		long begin = System.currentTimeMillis();
		long beginPre = begin;

		// 1、初始化配置
		GlobalConfig config = new GlobalConfig();
		config.init();
		Zone zone = GlobalConfig.getZone(GlobalConfig.zoneId);
		log.debug("init GlobalConfig end "
				+ (System.currentTimeMillis() - beginPre) + "ms");

		// 2、初始化netty
		beginPre = System.currentTimeMillis();
		Client client = new Client(zone.getProxyHost(), zone.getProxyPort());
		ClientSet.getInstance().backClient(client);

		log.debug("init GlobalConfig end "
				+ (System.currentTimeMillis() - beginPre) + "ms");
		log.info("======== init client end ===============");

	}
}
