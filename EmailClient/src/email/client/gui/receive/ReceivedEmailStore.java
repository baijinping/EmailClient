package email.client.gui.receive;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import email.client.dao.DBConnection;
import email.client.dao.po.TEmail;

/**
 * 用来保存SimpleEmailReceiver.handlerReceive()返回的邮件数据
 * 
 * 能够帮助管理这些邮件数据
 * 
 * @author baikkp
 * 
 *         2014-2-26 下午11:01:52
 * 
 */
public class ReceivedEmailStore {

    private Message[] messages;

    /**
     * 用接收到的Message[]数据进行创建
     * 
     * @param messages
     *            接收到的Message
     */
    public ReceivedEmailStore(Message[] messages) {
	this.messages = messages;
    }

    /**
     * 将这些邮件数据存入到数据库中
     */
    public void saveIntoDB() {
	Connection conn = null;
	PreparedStatement ps = null;
	String sql = "insert into tEmail(accountId,type,uuid,subject,content,sendDate,status) values(?,?,?,?,?,?,?)";
	try {
	    conn = DBConnection.getConnection();
	    ps = conn.prepareStatement(sql);

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String keyStr = "";
	    StringBuffer mailTextContent = null;
	    for (int i = 0; i < messages.length; i++) {
		mailTextContent = new StringBuffer();

		Message message = messages[i];
		keyStr = (message.getSubject() + sdf.format(message
			.getSentDate())).hashCode() + "";
		System.out.println("uuid = " + keyStr);
		SimpleEmailReceiver
			.getMailTextContent(message, mailTextContent);

		TEmail tEmail = new TEmail();
		tEmail.setAccountId(1);
		tEmail.setType(1);
		tEmail.setSubject(message.getSubject());
		tEmail.setSendDate(sdf.format(message.getSentDate()));
		tEmail.setUuid(keyStr);
		tEmail.setContent(mailTextContent.toString());
		if (SimpleEmailReceiver.isSeen((MimeMessage) message)) {
		    tEmail.setStatus(1);
		} else {
		    tEmail.setStatus(0);
		}

		ps.setInt(1, tEmail.getAccountId());
		ps.setInt(2, tEmail.getType());
		ps.setString(3, tEmail.getUuid());
		ps.setString(4, tEmail.getSubject());
		ps.setString(5, tEmail.getContent());
		ps.setString(6, tEmail.getSendDate());
		ps.setInt(7, tEmail.getStatus());
		ps.executeUpdate();
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (ps != null) {
		    ps.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }

	}
    }

    /**
     * 返回所有邮件主题
     * 
     * @return 所有邮件的主题
     * @throws MessagingException 
     * @throws UnsupportedEncodingException 
     */
    public String[] getAllSubjects() throws UnsupportedEncodingException, MessagingException {
	String[] allSubject = new String[messages.length];
	
	for (int i = 0; i < messages.length; i++) {
	    allSubject[i] = SimpleEmailReceiver.getSubject((MimeMessage) messages[i]);
	}
	
	return allSubject;
    }

}
