package wang.wangby.dao.routingDataSource;

import org.springframework.util.SystemPropertyUtils;

public class DbContextHolder {

	public static final String DEFAULT="default";
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>() {
		protected String initialValue() {
			return DEFAULT;
		}
	};

	public static String getDbType() {
		return contextHolder.get();
	}
	
	public static void setDb(String key) {
		contextHolder.set(key);
	}

}
