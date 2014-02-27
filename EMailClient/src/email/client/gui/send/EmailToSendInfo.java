package email.client.gui.send;

import java.util.Date;
import java.util.Properties;

import email.client.gui.PanelConst;

/**
 * 保存待发送邮件的信息
 * @author baikkp
 * 
 * 2013-11-24 下午7:32:45
 *
 */
public class EmailToSendInfo {

    // 发送者的SMTP服务器地址
    private String senderHost;
    // 接受者的SMTP服务器地址
    private String receiverHost;
    // 发送者邮件地址
    private String senderAddress;
    // 发送者邮件密码
    private String senderPassword;
    // 接受者邮件地址
    private String receiverAddress;
    // 邮件主题
    private String subject;
    // 邮件正文
    private String content;
    // 发送时间
    private Date sendDate;
    
    public String getSenderAddress() {
        return senderAddress;
    }
    public void setSenderAddress(String senderAddress) {
	if(!senderAddress.matches(PanelConst.REGEX_EMAIL_ADDRESS)) {
	    throw new IllegalArgumentException("Wrong Sender Email Address : " + senderAddress);
	}
	this.senderAddress = senderAddress;
	this.senderHost = "smtp." + senderAddress.substring(senderAddress.indexOf("@") + 1);
    }
    public String getSenderPassword() {
	return senderPassword;
    }
    public void setSenderPassword(String senderPassword) {
	this.senderPassword = senderPassword;
    }
    public String getReceiverAddress() {
        return receiverAddress;
    }
    public void setReceiverAddress(String receiverAddress) {
	if(!receiverAddress.matches(PanelConst.REGEX_EMAIL_ADDRESS)) {
	    throw new IllegalArgumentException("Wrong Receiver Email Address : " + receiverAddress);
	}
        this.receiverAddress = receiverAddress;
        this.receiverHost = "smtp." + receiverAddress.substring(receiverAddress.indexOf("@") + 1);
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    
    public Properties getProperties() {
	Properties props = new Properties();
	props.put("mail.smtp.host", this.senderHost);
	props.put("mail.smtp.auth", true);
	return props;
    }
}
