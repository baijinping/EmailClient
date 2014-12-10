package email.client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import email.client.dao.po.TAccount;
import email.client.util.GlobalDataManager;
/**
 * 账户数据操作
 * @author baikkp
 * 
 * 2014-3-25 下午9:47:28
 *
 */
@SuppressWarnings("unchecked")
public class TAccountDao {
    
    /**
     * 添加新账户到数据库中,并设置tAccount实例的id
     * @param tAccount
     */
    public static void addNewAccount(TAccount tAccount) {
	try {
	    
	    ArrayList<TAccount> accountList = (ArrayList<TAccount>) GlobalDataManager.getData("accountList");
	    for (Iterator iterator = accountList.iterator(); iterator.hasNext();) {
		TAccount e = (TAccount) iterator.next();
		// 如果该邮箱账户已存在，则返回
		if(e.getEmailAddress().equals(tAccount.getEmailAddress())) {
		    tAccount.setId(e.getId());
		    return;
		}
	    }
	    
	    // 该账户没有存在数据库中
	    Connection conn = DBConnection.getConnection();
	    PreparedStatement ps = conn.prepareStatement("insert into tAccount(emailAddress) values(?)");
	    ps.setString(1, tAccount.getEmailAddress());
	    ps.executeUpdate();
	    ps.close();
	    
	    // 获取新账户id并设置到参数中
	    ps = conn.prepareStatement("select id from TAccount where id = (select max(id) from TAccount)");
	    ResultSet rs = ps.executeQuery();
	    if(rs.next()) {
		tAccount.setId(rs.getInt("id"));
	    }
	    System.out.println("新");
	    rs.close();
	    ps.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
