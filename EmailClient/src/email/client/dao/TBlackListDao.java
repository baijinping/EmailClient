package email.client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import email.client.dao.po.TBlackList;
import email.client.util.GlobalDataManager;

/**
 * 管理黑名单数据
 * @author baikkp
 * 
 * 2014-3-26 下午2:41:39
 *
 */
public class TBlackListDao {
    /**
     * 添加新黑名单
     * @param tBlackList
     */
    public static void addNewBlackList(TBlackList tBlackList) {
	try {
    	Connection conn = DBConnection.getConnection();
    	PreparedStatement ps = conn.prepareStatement("insert into tBlackList(accountId, emailAddress, comment)" +
    			" values(?,?,?)");
    	if(tBlackList.getAccountId() == null) {
    	    tBlackList.setAccountId((int)GlobalDataManager.getData("localAccountId"));
    	}
    	ps.setInt(1, tBlackList.getAccountId());
    	ps.setString(2, tBlackList.getEmailAddress());
    	ps.setString(3, tBlackList.getComment());
    	
    	ps.executeUpdate();
    	ps.close();
    	/*
    	 * 插入数据库后即获得一个id，将该id放到传入的TLinkMan实例中
    	 */
    	ps = conn.prepareStatement("select id from tBlackList where id=(select max(id) from tBlackList)");
    	ResultSet rs = ps.executeQuery();
    	if(rs.next()) {
    	    tBlackList.setId(rs.getInt("id"));
    	}
    	
    	ps.close();
	} catch(SQLException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * 删除黑名单
     * @param tBlackList
     */
    public static void deleteBlackList(TBlackList tBlackList) {
	try {
	    Connection conn = DBConnection.getConnection();
	    PreparedStatement ps = conn
		    .prepareStatement("delete from TBlackList where id = ?");
	    ps.setInt(1, tBlackList.getId());

	    ps.executeUpdate();
	    ps.close();
	} catch(SQLException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * 更新黑名单数据
     * @param tBlackList
     */
    public static void updateBlackList(TBlackList tBlackList) {
	try {
	    Connection conn = DBConnection.getConnection();
	    PreparedStatement ps = conn
		    .prepareStatement("update TBlackList set accountId=?, emailAddress=?, comment=? where id=?");
	    ps.setInt(1, tBlackList.getAccountId());
	    ps.setString(2, tBlackList.getEmailAddress());
	    ps.setString(3, tBlackList.getComment());
	    ps.setInt(4, tBlackList.getId());

	    ps.executeUpdate();
	    ps.close();
	} catch(SQLException e) {
	    e.printStackTrace();
	}
    }
}
