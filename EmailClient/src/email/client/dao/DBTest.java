package email.client.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import email.client.util.PathManager;

public class DBTest {
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		Class.forName("org.sqlite.JDBC");
		System.out.println(PathManager.getResourcePath());
		Connection conn = DriverManager.getConnection("jdbc:sqlite:/"
				+ PathManager.getResourcePath() + "maildb.db");

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from test");
		while (rs.next()) {
			Integer id = rs.getInt("id");
			String name = rs.getString("name");
			System.out.println("id(" + id + "), name(" + name + ")");
		}
		stmt.close();
		conn.close();
	}
}
