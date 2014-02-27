package email.client.handler.login;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
    private String mailAddress;
    private String mailPassword;

    public MyAuthenticator(String _mailAddress, String _mailPassword) {
	mailAddress = _mailAddress;
	mailPassword = _mailPassword;
    }

    public String getMailAddress() {
	return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
	this.mailAddress = mailAddress;
    }

    public String getMailPassword() {
	return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
	this.mailPassword = mailPassword;
    }
    
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mailAddress, mailPassword);
    }
}
