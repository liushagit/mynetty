package com.ygxhj.mynetty.server.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ygxhj.mynetty.util.PropertiesUtil;


public class CommandSet {
	private Logger log = Logger.getLogger(CommandSet.class);
	private static final String COMMAND_FILE = "command.properties";
	private static final CommandSet instance = new CommandSet();
	private Map<String, Command> cmdSet = new HashMap<String, Command>();
	private CommandSet(){}
	
	public Command getCmd(String cmd){
		return cmdSet.get(cmd);
	}
	
	public static CommandSet getInstance() {
		return instance;
	}

	public void loadCommand() {
		Properties properties = PropertiesUtil.loadProperties(COMMAND_FILE);
		// Load command objects here
		for (Object key : properties.keySet()) {
			String clazz = properties.getProperty((String) key);
			if (clazz == null)
				continue;
			try {
				String[] k = ((String) key).split(",");
				Command cmd = (Command) Class.forName(clazz).getConstructor().newInstance();
				for (String name : k) {
					cmdSet.put(name, cmd);
				}
			} catch (Exception e) {
				log.error("exception", e);
			}
		}
	}
	
	public String printCmdSet(){
		return cmdSet.toString();
	}
}
