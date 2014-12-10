package email.client.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import email.client.util.PathManager;

/**
 * 维持sqlite数据库连接
 * 
 * @author baikkp
 * 
 *         2014-2-26 下午11:17:03
 * 
 */
public class DBConnection {

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static Connection conn = null;
	
	/**
	 * 取得数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		
		try {
		    if(conn == null || conn.isClosed()) {
			conn = DriverManager.getConnection("jdbc:sqlite:/"
					+ PathManager.getResourcePath() + "maildb.db");
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接
	 */
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
