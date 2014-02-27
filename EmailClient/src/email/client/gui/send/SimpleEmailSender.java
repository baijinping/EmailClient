package email.client.gui.send;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import email.client.handler.login.MyAuthenticator;

/**
 * 负责处理邮件发送
 * 
 * @author baikkp
 * 
 *         2013-11-24 下午7:25:31
 * 
 */
public class SimpleEmailSender {

	public static boolean sendMain(EmailToSendInfo mailToSend) {
		// 根据邮件会话属性和验证信息构造一个发送邮件的Session
		Session session = Session.getDefaultInstance(
				mailToSend.getProperties(),
				new MyAuthenticator(mailToSend.getSenderAddress(), mailToSend
						.getSenderPassword()));
		try {
			// 根据Session创建一个邮件消息
			Message mailMessage = new MimeMessage(session);

			// 设置邮件发送者地址
			mailMessage.setFrom(new InternetAddress(mailToSend
					.getSenderAddress()));

			// 设置邮件接受者类型和地址
			mailMessage.setRecipient(Message.RecipientType.TO,
					new InternetAddress(mailToSend.getReceiverAddress()));

			// 设置邮件主题
			mailMessage.setSubject(mailToSend.getSubject());

			// 设置发送日期
			mailMessage.setSentDate(mailToSend.getSendDate());

			// 设置邮件正文
			mailMessage.setText(mailToSend.getContent());

			// 发送邮件
			Transport.send(mailMessage);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
