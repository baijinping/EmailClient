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

	/**
	 * 取得数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:/"
					+ PathManager.getResourcePath() + "maildb.db");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
