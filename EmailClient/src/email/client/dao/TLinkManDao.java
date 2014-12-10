package email.client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import email.client.dao.po.TLinkMan;
import email.client.util.GlobalDataManager;

/**
 * 负责管理联系人LinkMan表的数据操作
 * @author baikkp
 * 
 * 2014-3-24 下午5:58:20
 *
 */
public class TLinkManDao {
    
    /**
     * 插入新的联系人数据
     * @param tLinkMan
     */
    public static final void addNewLinkMan(TLinkMan tLinkMan) {
	try {
        	Connection conn = DBConnection.getConnection();
        	PreparedStatement ps = conn.prepareStatement("insert into tLinkMan(accountId, name, emailAddress, phoneNumber, comment)" +
        			" values(?,?,?,?,?)");
        	if(tLinkMan.getAccountId() == null) {
        	    tLinkMan.setAccountId((int)GlobalDataManager.getData("localAccountId"));
        	}
        	ps.setInt(1, tLinkMan.getAccountId());
        	ps.setString(2, tLinkMan.getName());
        	ps.setString(3, tLinkMan.getEmailAddress());
        	ps.setString(4, tLinkMan.getPhoneNumber());
        	ps.setString(5, tLinkMan.getComment());
        	
        	ps.executeUpdate();
        	ps.close();
        	/*
        	 * 插入数据库后即获得一个id，将该id放到传入的TLinkMan实例中
        	 */
        	ps = conn.prepareStatement("select id from tLinkMan where id=(select max(id) from tLinkMan)");
        	ResultSet rs = ps.executeQuery();
        	if(rs.next()) {
        	    tLinkMan.setId(rs.getInt("id"));
        	}
        	
        	ps.close();
	} catch(SQLException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * 删除联系人数据
     * @param tLinkMan
     */
    public static final void deleteLinkMan(TLinkMan tLinkMan) {
	try {
	    Connection conn = DBConnection.getConnection();
	    PreparedStatement ps = conn
		    .prepareStatement("delete from TLinkMan where id = ?");
	    ps.setInt(1, tLinkMan.getId());

	    ps.executeUpdate();
	    ps.close();
	} catch(SQLException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * 更新对应id的联系人数据
     * @param tLinkMan
     */
    public static final void updateLinkMan(TLinkMan tLinkMan) {
	try {
	    Connection conn = DBConnection.getConnection();
	    PreparedStatement ps = conn
		    .prepareStatement("update TLinkMan set accountId=?, name=?, emailAddress=?, phoneNumber=?, comment=? where id=?");
	    ps.setInt(1, tLinkMan.getAccountId());
	    ps.setString(2, tLinkMan.getName());
	    ps.setString(3, tLinkMan.getEmailAddress());
	    ps.setString(4, tLinkMan.getPhoneNumber());
	    ps.setString(5, tLinkMan.getComment());
	    ps.setInt(6, tLinkMan.getId());

	    ps.executeUpdate();
	    ps.close();
	} catch(SQLException e) {
	    e.printStackTrace();
	}
    }
}
