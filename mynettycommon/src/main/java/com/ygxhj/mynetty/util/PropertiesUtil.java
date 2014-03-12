package com.ygxhj.mynetty.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertiesUtil {
	private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
	/**
	 * Load properties
	 * 
	 * @param @param fileName
	 * @param @return
	 * @return Properties
	 * @throws
	 */
	public static Properties loadProperties(String fileName) {
		Properties properties = new Properties();

		InputStream inputStream = null;
		try {
			inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
			if (inputStream == null) {
				inputStream = new FileInputStream(new File(fileName));
			}
			properties.load(inputStream);
		} catch (Exception e) {
			log.error("exception", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error("exception", e);
				}
		}

		return properties;
	}
}
