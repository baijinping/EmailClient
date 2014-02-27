package email.client.handler.login;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;

import email.client.util.GlobalDataManager;

/**
 * 获取邮件账号授权
 * 
 * ============ 问题列表 ============== 1.验证失败有几种可能，要给调用者充足的信息 2.如果没有打开smtp，也要给提示
 * 
 * 
 * @author baikkp
 * 
 *         2013-11-21 下午10:14:48
 * 
 */
public class EmailAccountCheck {

	/**
	 * 根据邮箱地址、密码、协议、服务器地址验证邮箱账户是否正确
	 * 
	 * @param host
	 * @param protocol
	 * @param email
	 * @param password
	 * @return
	 */
	public static boolean checkEmailAccount(String host, String protocol,
			String email, String password) {
		Properties props = new Properties();
		props.put("mail.smtp.host", protocol + "." + host);
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		Transport transport = null;
		try {
			transport = session.getTransport(protocol);
			transport.connect(email, password);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		System.out.println(transport.getURLName());
		try {
			return transport.isConnected();
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.yeah.net");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		Transport transport = session.getTransport("smtp");
		// 测试账号
		transport.connect(GlobalDataManager.getData("localEmailAddress")
				.toString(), GlobalDataManager.getData("localEmailPassword")
				.toString());
		System.out.println(transport.getURLName());
		transport.close();
	}
}
