package email.client;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import email.client.gui.login.LoginUI;
import email.client.handler.CacheDataLoader;

/**
 * 暖冬 电子邮件客户端 的启动程序
 * 
 * @author baikkp
 * 
 *         2013-11-23 下午4:50:07
 * 
 */
public class StartEmailClient {
    public static void main(String[] args) {
	/*
	 * #启动程序的工作#
	 * 
	 * 判断是否已经添加了邮箱账户 ——若没有，则打开LoginUI ——若有，则判断账户是否有效，比如可能修改了邮箱密码
	 * 若发现账户无效，则打开LoginUI，并设置警告信息 ——若有，并且账户有效，则打开EmailUI
	 */
	try {
	    CacheDataLoader.loadAccount();
	} catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(null,
		    "十分抱歉！加载数据时发生错误：" + e.getMessage(), "启动失败",
		    JOptionPane.ERROR_MESSAGE);
	    System.exit(1);
	}
	new LoginUI();
    }
}
