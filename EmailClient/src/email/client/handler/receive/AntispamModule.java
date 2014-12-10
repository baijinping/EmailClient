package email.client.handler.receive;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import email.client.dao.po.TBlackList;
import email.client.util.GlobalDataManager;

/**
 * 反垃圾邮件模块
 * 
 * @author baikkp
 * 
 *         2014-4-4 下午5:27:35
 * 
 */
public class AntispamModule {

    /**
     * 判断是否是黑名单中的邮件
     * 
     * @param emailAddress
     * @return
     */
    @SuppressWarnings("unchecked")
    private static boolean checkIsBlackList(String emailAddress) {
	if (null == emailAddress || "".equals(emailAddress)) {
	    return false;
	}
	ArrayList<TBlackList> list = (ArrayList<TBlackList>) GlobalDataManager
		.getData("blackList");
	for (Iterator<TBlackList> iterator = list.iterator(); iterator
		.hasNext();) {
	    TBlackList tBlackList = (TBlackList) iterator.next();
	    if (emailAddress.equals(tBlackList.getEmailAddress())) {
		return true;
	    }
	}

	return false;
    }

    /**
     * 黑名单发件人过滤
     * 
     * @param messages
     */
    public static void blackListFilter(List<Message> messageList) {
	for (Iterator<Message> iterator = messageList.iterator(); iterator
		.hasNext();) {
	    Message message = (Message) iterator.next();
	    // 获取发件人地址
	    String fromEmail = null;
	    try {
		fromEmail = SimpleEmailReceiver
			.getFromEmail((MimeMessage) message);
	    } catch (MessagingException e) {
		e.printStackTrace();
	    }
	    // 判断是否发件人地址黑名单成员
	    if (AntispamModule.checkIsBlackList(fromEmail)) {
		System.out.println("发件人" + fromEmail + "属于黑名单成员，不接受");
		// 若是，从邮件列表中删除
		messageList.remove(message);
	    }
	}
    }

    
    private static ArrayList<String> pbWordList = null;
    /**
     * 判断字符串中是否含有屏蔽字（即是否是垃圾邮件）
     * 
     * @param strForCheck
     * @return
     */
    @SuppressWarnings("unchecked")
    private static boolean checkHasPBWord(String strForCheck) {
	if(pbWordList == null) {
	    pbWordList = (ArrayList<String>) GlobalDataManager.getData("pbWordList");
	}
	for (Iterator<String> iterator = pbWordList.iterator(); iterator.hasNext();) {
	    String word = (String) iterator.next();
	    if(strForCheck.contains(word)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * 反垃圾邮件的检测和移除
     * 
     * @param messages
     * @throws MessagingException
     * @throws IOException
     */
    public static void antispamFilter(List<Message> messageList)
	    throws MessagingException, IOException {
	
	for (int i = 0; i < messageList.size(); i ++) {
	    Message message = messageList.get(i);

	    String subject = SimpleEmailReceiver
		    .getSubject((MimeMessage) message);
	    StringBuffer sb = new StringBuffer();
	    SimpleEmailReceiver.getMailTextContent(message, sb);

	    System.out.println("正在检测邮件 - " + subject);
	    
	    // 检测主题
	    if (checkHasPBWord(subject)) {
		System.out.println("检测为垃圾邮件（主题），删除该邮件");
		messageList.remove(message);
		continue;
	    }

	    // 检测正文
	    if (checkHasPBWord(sb.toString())) {
		System.out.println("检测为垃圾邮件（正文），删除该邮件");
		messageList.remove(message);
		continue;
	    }

	}
    }
}
