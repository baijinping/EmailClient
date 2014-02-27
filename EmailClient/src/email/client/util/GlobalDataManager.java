package email.client.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @singleton 全局数据管理类
 * 
 *            比如本地账户邮箱地址，登陆密码等数据。以下key表示响应的全局数据
 * 
 *            "localEmailAddress" 登陆的本地账户 "localEmailPassword" 本地账户的邮箱密码
 * 
 * @author baikkp
 * 
 *         2014-2-19 下午9:10:34
 * 
 */
public final class GlobalDataManager {
	// 使用线程安全的Map
	private static final Map<String, Object> globalData = new ConcurrentHashMap<String, Object>();

	private static final GlobalDataManager instance = new GlobalDataManager();

	// 公共数据
	public static final Map<String, String> appSetting = new ConcurrentHashMap<>();

	private GlobalDataManager() {
		// 添加测试数据，方便测试
		addData("localEmailHost", "yeah.net");
		addData("localEmailAddress", "formytest_123@yeah.net");
		addData("localEmailPassword", "formytest");

		// 真实需要的数据

		// 初始化设置数据
		initAppSetting();
	}

	private void initAppSetting() {

	}

	// 添加全局数据
	public static void addData(String key, Object value) {
		if (globalData.containsKey(key) == false) {
			globalData.put(key, value);
		}
	}

	// 获取全局数据值
	public static Object getData(String key) {
		if (globalData.containsKey(key)) {
			return globalData.get(key);
		}
		return null;
	}

	// 更新全局数据值
	public static void updateData(String key, Object newValue) {
		if (globalData.containsKey(key)) {
			globalData.put(key, newValue);
		}
	}

	// 获取GlobalDataManager实例
	/*
	 * public static GlobalDataManager getInstance() { return instance; }
	 */
}
