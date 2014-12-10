package email.client.gui.receive;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.sun.mail.imap.IMAPMessage;

import email.client.dao.DBConnection;
import email.client.dao.TEmailDao;
import email.client.dao.po.TEmail;
import email.client.handler.main.MainUIHandler;
import email.client.handler.receive.AntispamModule;
import email.client.handler.receive.SimpleEmailReceiver;
import email.client.util.GlobalDataManager;
import email.client.util.email.EmailDataUtil;

/**
 * 用来保存SimpleEmailReceiver.handlerReceive()返回的邮件数据
 * 
 * 能够帮助管理这些邮件数据
 * 
 * 保存每次收取邮件时，获取到的连接
 * 
 * @author baikkp 饿汉式单例 2014-2-26 下午11:01:52
 * 
 */
@SuppressWarnings("unchecked")
public class ReceivedEmailStore {

    private static final ReceivedEmailStore instance = new ReceivedEmailStore();

    private Folder folder;
    private Message[] messages;

    private ReceivedEmailStore() {
    }

    public static ReceivedEmailStore getInstance() {
	return instance;
    }

    /**
     * 同步服务器的邮件数据到本地
     */
    public void saveIntoDB() {
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String keyStr = "";
	    StringBuffer mailTextContent = null;
	    /**
	     * 步骤一：如果有新邮件，添加到本地
	     */
	    for (int i = 0; i < messages.length; i++) {
		mailTextContent = new StringBuffer();

		IMAPMessage message = (IMAPMessage) messages[i];
		keyStr = message.getMessageID();
		System.out.println("uuid = " + keyStr);
		try {
		    SimpleEmailReceiver.getMailTextContent(message,
			    mailTextContent);
		} catch (Exception e) {
		    e.printStackTrace();
		    continue;
		}
		// 如果uuid为0，则不处理
		// 如果该邮件已被放入数据库，则不添加到数据库中
		if (keyStr == null
			|| EmailDataUtil.checkEmailExistWithUuid(keyStr)) {
		    System.out.printf(
			    "email(%s) uuid(%s) has already in db.\n",
			    message.getSubject(), keyStr);
		    continue;
		}

		TEmail tEmail = new TEmail();
		tEmail.setAccountId((int) GlobalDataManager
			.getData("localAccountId"));
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
		tEmail.setFromEmail(SimpleEmailReceiver
			.getFromEmail((MimeMessage) message));
		tEmail.setFromName(SimpleEmailReceiver
			.getFromName((MimeMessage) message));

		// 添加到当前缓存
		System.out.printf("add new uuid(%s) into email and emailMap\n",
			keyStr);
		EmailDataUtil.addNewEmail(tEmail);
		// 添加到数据库
		TEmailDao.addNewEmail(tEmail);
	    }

	    /**
	     * 步骤二：如果某些邮件已经被删除，则删除本地的对应邮件
	     */
	    HashSet<String> keySet = new HashSet<>(messages.length);
	    int index = 0;
	    for (Message message : messages) {
		IMAPMessage msg = (IMAPMessage) message;
		keySet.add(msg.getMessageID());
		index += 1;
	    }

	    // 遍历本地邮件数据，如果不在服务器下载的邮件列表中，则记录到待删除列表中，遍历完后删除
	    ArrayList<String> forDeleteList = new ArrayList<>();
	    Map<String, TEmail> emailMap = (HashMap<String, TEmail>) GlobalDataManager
		    .getData("emailMap");
	    for (Iterator iter = emailMap.keySet().iterator(); iter.hasNext();) {
		String key = (String) iter.next();
		if (false == keySet.contains(key)) {
		    forDeleteList.add(key);
		}
	    }
	    // 从待删除列表中删除本地邮件数据
	    for (Iterator iter = forDeleteList.iterator(); iter.hasNext();) {
		String key = (String) iter.next();

		System.out.printf("delete Email(uuid=%s) from db.\n", key);

		// 从运行时数据中删除
		EmailDataUtil.deleteOldEmail(EmailDataUtil.getEmail(key));

		// 从数据库中删除
		TEmailDao.deleteOldEmail(key);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * 返回所有邮件主题
     * 
     * @return 所有邮件的主题
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public String[] getAllSubjects() throws UnsupportedEncodingException,
	    MessagingException {
	String[] allSubject = new String[messages.length];

	for (int i = 0; i < messages.length; i++) {
	    allSubject[i] = SimpleEmailReceiver
		    .getSubject((MimeMessage) messages[i]);
	}

	return allSubject;
    }

    /**
     * 返回所有邮件数据
     * 
     * @return
     */
    public List<TEmail> getAllTEmail() {
	List<TEmail> tEmailList = new ArrayList<>(messages.length);
	try {
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
		tEmail.setFromEmail(SimpleEmailReceiver
			.getFromEmail((MimeMessage) message));
		tEmail.setFromName(SimpleEmailReceiver
			.getFromName((MimeMessage) message));

		tEmailList.add(tEmail);
	    }

	} catch (MessagingException | IOException e) {
	    e.printStackTrace();
	}
	return tEmailList;
    }

    /**
     * @return 邮件集包含多少邮件
     */
    public int getMessageCount() {
	return messages.length;
    }

    /**
     * @return 邮件集中有几封<b>未读邮件</b>
     * @throws MessagingException
     */
    public int getUnreadMessageCount() throws MessagingException {
	return folder.getUnreadMessageCount();
    }

    /**
     * @return 邮件集中包含多少<b>新邮件</b>
     * @throws MessagingException
     */
    public int getNewMessageCount() throws MessagingException {
	return folder.getNewMessageCount();
    }

    /**
     * @return 邮件集中包含多少<b>已删除邮件</b>
     * @throws MessagingException
     */
    public int getDeletedMessageCount() throws MessagingException {
	return folder.getDeletedMessageCount();
    }

    /**
     * 根据MessageID返回指定的Message实例
     * 
     * @param uuid
     * @throws MessagingException
     */
    public Message getMessage(String uuid) {
	try {
	    if (messages == null) {
		return null;
	    }
	    for (int i = 0; i < messages.length; i++) {
		IMAPMessage msg = (IMAPMessage) messages[i];
		if (msg.isExpunged() == false) {
		    if (uuid.equals(msg.getMessageID())) {
			return msg;
		    }
		}
	    }
	} catch (MessagingException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public Folder getFolder() {
	return folder;
    }

    public void setFolder(Folder folder) {
	this.folder = folder;
    }

    public Message[] getMessages() {
	return messages;
    }

    public void setMessages(Message[] messages) {
	this.messages = messages;
    }

}
