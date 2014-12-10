package email.client.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import email.client.dao.DBConnection;
import email.client.dao.po.TAccount;
import email.client.dao.po.TBlackList;
import email.client.dao.po.TEmail;
import email.client.dao.po.TLinkMan;
import email.client.util.GlobalDataManager;
import email.client.util.PathManager;

/**
 * 负责在登录之后，加载数据库数据到GlobalDataManager中。
 * 
 * @author baikkp
 * 
 *         2014-3-3 上午9:15:17
 */
public final class CacheDataLoader {
    private CacheDataLoader() {
    }

    /**
     * 加载邮件数据,放入GlobalDataManager.email中
     * 
     * @throws SQLException
     */
    private static void loadEmail() throws SQLException {
	Connection conn = DBConnection.getConnection();
	String sql = "SELECT * FROM TEmail where accountId = ?";
	List<TEmail> emailData = new ArrayList<>();
	Map<String, TEmail> emailMap = new HashMap<>();
	PreparedStatement ps = conn.prepareStatement(sql);
	ps.setInt(1, (int) GlobalDataManager.getData("localAccountId"));
	ResultSet rs = ps.executeQuery();

	int rowCount = 0;
	while (rs.next()) {
	    rowCount++;

	    TEmail tEmail = new TEmail();
	    tEmail.setId(rs.getInt("id"));
	    tEmail.setAccountId(rs.getInt("accountId"));
	    tEmail.setSubject(rs.getString("subject"));
	    tEmail.setContent(rs.getString("content"));
	    tEmail.setUuid(rs.getString("uuid"));
	    tEmail.setType(rs.getInt("type"));
	    tEmail.setStatus(rs.getInt("status"));
	    tEmail.setSendDate(rs.getString("sendDate"));
	    tEmail.setFromEmail(rs.getString("fromEmail"));
	    tEmail.setFromName(rs.getString("fromName"));

	    if (!emailMap.containsKey(tEmail.getUuid())) {
		System.out.printf("load Email(%d, %s, %s)\n", tEmail.getId(),
			tEmail.getSubject(), tEmail.getUuid());
		emailData.add(tEmail);
		emailMap.put(tEmail.getUuid(), tEmail);
	    }
	}

	System.out.println("加载邮件数据：共找到" + rowCount + "封邮件...");

	GlobalDataManager.addData("email", emailData);
	GlobalDataManager.addData("emailMap", emailMap);

	rs.close();
	ps.close();
    }

    /**
     * 加载联系人数据
     * 
     * @throws SQLException
     */
    private static void loadLinkMan() throws SQLException {
	Connection conn = DBConnection.getConnection();
	String sql = "select * from TLinkMan where accountId = ?";
	PreparedStatement ps = conn.prepareStatement(sql);
	ps.setInt(1, (int) GlobalDataManager.getData("localAccountId"));
	ResultSet rs = ps.executeQuery();
	ArrayList<TLinkMan> tLinkManList = new ArrayList<>(rs.getFetchSize());

	int rowCount = 0;
	while (rs.next()) {
	    rowCount++;

	    TLinkMan tLinkMan = new TLinkMan();
	    tLinkMan.setId(rs.getInt("id"));
	    tLinkMan.setAccountId(rs.getInt("accountId"));
	    tLinkMan.setName(rs.getString("name"));
	    tLinkMan.setEmailAddress(rs.getString("emailAddress"));
	    tLinkMan.setPhoneNumber(rs.getString("phoneNumber"));
	    tLinkMan.setComment(rs.getString("comment"));

	    tLinkManList.add(tLinkMan);
	}

	System.out.println("加载联系人数据：共找到" + rowCount + "个联系人...");

	// 将联系人数据放入全局
	GlobalDataManager.addData("linkMan", tLinkManList);

    }

    /**
     * 加载黑名单数据
     * 
     * @throws SQLException
     */
    private static void loadBlackList() throws SQLException {
	Connection conn = DBConnection.getConnection();
	String sql = "select * from TBlackList where accountId = ?";
	PreparedStatement ps = conn.prepareStatement(sql);
	ps.setInt(1, (int) GlobalDataManager.getData("localAccountId"));
	ResultSet rs = ps.executeQuery();
	ArrayList<TBlackList> tBlackListList = new ArrayList<>(
		rs.getFetchSize());

	int rowCount = 0;
	while (rs.next()) {
	    rowCount++;

	    TBlackList tBlackList = new TBlackList();
	    tBlackList.setId(rs.getInt("id"));
	    tBlackList.setAccountId(rs.getInt("accountId"));
	    tBlackList.setEmailAddress(rs.getString("emailAddress"));
	    tBlackList.setComment(rs.getString("comment"));

	    tBlackListList.add(tBlackList);
	}

	System.out.println("加载黑名单数据：共找到" + rowCount + "个黑名单...");

	// 将联系人数据放入全局
	GlobalDataManager.addData("blackList", tBlackListList);
    }

    /**
     * 加载账户数据
     * 
     * @throws SQLException
     */
    public static void loadAccount() throws SQLException {
	Connection conn = DBConnection.getConnection();
	String sql = "select * from TAccount";
	PreparedStatement ps = conn.prepareStatement(sql);
	ResultSet rs = ps.executeQuery();

	ArrayList<TAccount> accountList = new ArrayList<>();
	while (rs.next()) {
	    TAccount tAccount = new TAccount();
	    tAccount.setId(rs.getInt("id"));
	    tAccount.setEmailAddress(rs.getString("emailAddress"));

	    accountList.add(tAccount);
	}
	rs.close();
	ps.close();

	// 将账户数据放入全局
	GlobalDataManager.addData("accountList", accountList);

    }

    /**
     * 加载屏蔽字库
     * 
     * @throws FileNotFoundException
     */
    public static void loadPBWord() throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(
		PathManager.getResourcePath() + "PBWord.txt"));
	
	ArrayList<String> pbWordList = new ArrayList<>();
	String line = reader.readLine();
	while(line != null) {
	    pbWordList.add(line);
	    line = reader.readLine();
	}
	
	reader.close();
	
	// 将屏蔽字库放入全局
	GlobalDataManager.addData("pbWordList", pbWordList);
    }

    /**
     * 加载数据库数据到全局变量
     * 
     * @throws SQLException
     * @throws IOException 
     */
    public static void loadCacheData() throws SQLException, IOException {
	loadEmail();
	loadLinkMan();
	loadBlackList();
	loadPBWord();
    }
}
