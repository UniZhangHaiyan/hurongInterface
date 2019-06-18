package demo;

import java.util.ResourceBundle;

public class Config {
	

	private static Object lock              = new Object();
	private static Config config     = null;
	private static ResourceBundle rb        = null;
	private static final String CONFIG_FILE = "youhuoInfo";
	
	private Config() {
		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}
	
	public static Config getInstance() {
		synchronized(lock) {
			if(null == config) {
				config = new Config();
			}
		}
		return (config);
	}
	
	public String getValue(String key) {
		return (rb.getString(key));
	}
}