package email.client.util.email;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import email.client.dao.po.TEmail;
import email.client.util.GlobalDataManager;

/**
 * 负责管理GlobalDataManager中的email邮件数据
 * 
 * @author baikkp
 * 
 *         2014-3-3 上午9:35:48
 */
@SuppressWarnings("unchecked")
public class EmailDataUtil {

    /**
     * @return 所有邮件数据
     */
    public static List<TEmail> getAllEmail() {
	List<TEmail> emailList = (List<TEmail>) GlobalDataManager
		.getData("email");
	return emailList;
    }

    /**
     * 根据uuid获取本地邮件数据
     * @param uuid
     * @return
     */
    public static TEmail getEmail(String uuid) {
	Map<String, TEmail> emailMap = (HashMap<String, TEmail>) GlobalDataManager
		.getData("emailMap");
	return emailMap.get(uuid);
    }
    
    /**
     * 加入新邮件到内存,更新email和emailMap
     * 
     * @param tEmail
     */
    public static void addNewEmail(TEmail tEmail) {
	List<TEmail> emailList = (List<TEmail>) GlobalDataManager
		.getData("email");
	emailList.add(tEmail);

	Map<String, TEmail> emailMap = (HashMap<String, TEmail>) GlobalDataManager
		.getData("emailMap");
	emailMap.put(tEmail.getUuid(), tEmail);
    }

    /**
     * 删除旧邮件，更新email和emailMap
     * @param tEmail
     */
    public static void deleteOldEmail(TEmail tEmail) {
	List<TEmail> emailList = (List<TEmail>) GlobalDataManager
		.getData("email");
	emailList.remove(tEmail);

	Map<String, TEmail> emailMap = (HashMap<String, TEmail>) GlobalDataManager
		.getData("emailMap");
	emailMap.remove(tEmail.getUuid());
    }

    /**
     * 根据全局数据"emailMap"判断邮件是否存在
     * 
     * @param uuid
     * @return 该uuid是否存在
     */
    public static boolean checkEmailExistWithUuid(String uuid) {
	if (uuid != null && !"".equals(uuid)) {
	    Map<String, TEmail> emailMap = (HashMap<String, TEmail>) GlobalDataManager
		    .getData("emailMap");
	    if (emailMap != null && emailMap.containsKey(uuid)) {
		return true;
	    }
	}
	return false;
    }
}
