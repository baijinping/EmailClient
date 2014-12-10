package email.client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import email.client.dao.po.TEmail;

/**
 * 邮件TEmail表的数据操作
 * 
 * @author baikkp
 * 
 *         2014-3-24 下午6:24:52
 * 
 */
public class TEmailDao {

    /**
     * 添加新邮件
     * @param tEmail
     */
    public static final void addNewEmail(TEmail tEmail) {
	Connection conn = DBConnection.getConnection();
	PreparedStatement ps = null;;
	try {
	    ps = conn
		    .prepareStatement("insert into tEmail(accountId,type,uuid,subject,content,sendDate,status,fromEmail,fromName) values(?,?,?,?,?,?,?,?,?)");

	    ps.setInt(1, tEmail.getAccountId());
	    ps.setInt(2, tEmail.getType());
	    ps.setString(3, tEmail.getUuid());
	    ps.setString(4, tEmail.getSubject());
	    ps.setString(5, tEmail.getContent());
	    ps.setString(6, tEmail.getSendDate());
	    ps.setInt(7, tEmail.getStatus());
	    ps.setString(8, tEmail.getFromEmail());
	    ps.setString(9, tEmail.getFromName());
	    ps.executeUpdate();
	    ps.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 删除邮件
     * @param uuid 邮件的uuid
     */
    public static final void deleteOldEmail(String uuid) {
	Connection conn = DBConnection.getConnection();
	PreparedStatement ps = null;;
	try {
	    ps = conn
		    .prepareStatement("delete from tEmail where uuid = ?");

	    ps.setString(1, uuid);
	    ps.executeUpdate();
	    ps.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
