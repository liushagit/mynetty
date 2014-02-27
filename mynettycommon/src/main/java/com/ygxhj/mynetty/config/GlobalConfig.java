package com.ygxhj.mynetty.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.ygxhj.mynetty.util.XMLUtil;

public class GlobalConfig {
	private Logger log = Logger.getLogger(GlobalConfig.class);
	private String global_config = "global_config.xml";
	private static Map<String, Zone> zones = new HashMap<String, Zone>();
	public static String zoneId;
	public void init() {

		try {
			Element element = XMLUtil.getRoot(global_config, true);
			Element zoneIdElement = element.element("zone_id");
			zoneId = zoneIdElement.attributeValue("id");
			List<Element> elements = element.elements("zone");
			for (Element e : elements) {
				Zone zone = new Zone();
				zone.setId(e.attributeValue("id"));
				zone.setName(e.attributeValue("name"));
				zone.setMaxPlayerNum(Integer.parseInt(e
						.attributeValue("maxPlayerNum")));

				Element db = e.element("db");
				zone.setDbHost(db.attributeValue("host"));
				zone.setDbPort(db.attributeValue("port"));
				zone.setDbName(db.attributeValue("dbname"));
				zone.setDbUserName(db.attributeValue("username"));
				zone.setDbPassword(db.attributeValue("password"));
				zone.setDbDelaytime(Integer.parseInt(db
						.attributeValue("delaytime")));

				Element res = e.element("db");
				zone.setResHost(res.attributeValue("host"));
				zone.setResPort(res.attributeValue("port"));
				zone.setResName(res.attributeValue("dbname"));
				zone.setResUserName(res.attributeValue("username"));
				zone.setResPassword(res.attributeValue("password"));

				Element proxy = e.element("proxy");
				zone.setProxyHost(proxy.attributeValue("host"));
				zone.setProxyPort(Integer.parseInt(proxy.attributeValue("port")));
				zone.setMd5Key(proxy.attributeValue("md5Key"));

				zones.put(zone.getId(), zone);
			}
		} catch (Exception e) {
			log.error("init golbal_config exception", e);
		}

	}

	public static Zone getZone() {
		return getZone("0");
	}

	public static Zone getZone(String zoneId) {
		return zones.get(zoneId);
	}

}
