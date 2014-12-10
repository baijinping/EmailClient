package email.client.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @singleton 全局数据管理类
 * 
 *          比如本地账户邮箱地址，登陆密码等数据。以下key表示响应的全局数据<key, type, what>
 * 
 *			localEmailHost(String)			邮箱账户对应的邮箱服务器地址
 *			localEmailAddress(String)		登陆的本地账户
 *			localEmailPassword(String)		本地账户的邮箱密码
 *			localAccountId				当前登录账户的id
 *
 *			email 		List(TEmail)  		本地账户的所有邮件（一个用户）
 *			emailMap 	Map(Uuid, TEmail) 	本地账户的所有邮件（一个用户），用uuid来获取，提高读取效率
 *			linkMan		List(TLinkMan)	本地账户的联系人数据（一个用户）
 *			blackList		List(TBlackList)	本地账户的黑名单数据（一个用户）
 *			accountList	List(TAccount)	本地所保存的账户数据
 *
 *			pbWordList	List(String)		过滤垃圾邮件的屏蔽字库
 * 
 * @author baikkp
 * 
 *         2014-2-19 下午9:10:34
 * 
 */
public final class GlobalDataManager {
	// 使用线程安全的Map
	private static final Map<String, Object> globalData = new ConcurrentHashMap<String, Object>();

	// 公共数据
	public static final Map<String, String> appSetting = new ConcurrentHashMap<>();

	private GlobalDataManager() {
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

}
