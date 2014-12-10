package email.client.gui.send;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import email.client.gui.PanelConst;
import email.client.handler.send.EmailToSendInfo;
import email.client.handler.send.SimpleEmailSender;
import email.client.util.GlobalDataManager;
import email.client.util.PathManager;

/**
 * 写邮件的界面
 * 
 * @author baikkp
 * 
 *         2013-11-24 下午6:49:50
 * 
 */
public class SendEmailPanel extends JFrame {

    private JFrame frame = this;

    private JTextPane txtEditor;

    private JButton btnSend; // 发送邮件

    private JLabel lblReceiverEmail;

    private JTextField txtReceiverEmail;

    private JLabel lblSubject;

    private JTextField txtSubject;

    public SendEmailPanel(String sendPanelTitle) {
	super(sendPanelTitle);

	setLayout(new BorderLayout());

	normalSetting();

	initComponment();
	initComponmentListener();

	setVisible(true);		
	// 设置窗口icon
	BufferedImage image = null;
	try {
	    image = ImageIO.read(new File(PathManager.getImageFilePath() + File.separator
			+ "client_logo.png"));
	} catch(IOException e) {
	    e.printStackTrace();
	}
	this.setIconImage(image);
    }

    private void initComponment() {

	GridLayout topLayout = new GridLayout(2, 2);
	JPanel topPanel = new JPanel(topLayout);
	add(topPanel, BorderLayout.NORTH);

	lblReceiverEmail = new JLabel("收件人：");
	topPanel.add(lblReceiverEmail);

	txtReceiverEmail = new JTextField(50);
	topPanel.add(txtReceiverEmail);

	lblSubject = new JLabel("主题：");
	// lblSubject.setBounds(50, 80, 100, 20);
	topPanel.add(lblSubject);

	txtSubject = new JTextField(50);
	topPanel.add(txtSubject);

	txtEditor = new JTextPane();
	txtEditor.setBounds(100, 100, 300, 400);
	add(txtEditor, BorderLayout.CENTER);

	// 底部按钮
	JPanel bottomJPanel = new JPanel();
	btnSend = new JButton("发邮件");
	bottomJPanel.add(btnSend);

	add(bottomJPanel, BorderLayout.SOUTH);
    }

    private void normalSetting() {
	try {
	    // 设置Nimbus皮肤
	    UIManager
		    .setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	} catch (ClassNotFoundException e) {
	    // 表示运行环境非JRE7
	    e.printStackTrace();
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (UnsupportedLookAndFeelException e) {
	    e.printStackTrace();
	}
	SwingUtilities.updateComponentTreeUI(this);

	// 点击红叉关闭窗口的行为
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	// 设置窗体位置大小为：水平垂直居中，大小为1/4
	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension screenSize = kit.getScreenSize();
	setBounds(screenSize.width / 4, screenSize.height / 4,
		screenSize.width / 2, screenSize.height / 2);
    }

    private void initComponmentListener() {
	// 发邮件按钮
	btnSend.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSend) {
		    String receiverEmail = txtReceiverEmail.getText();
		    String subject = txtSubject.getText();
		    String emailContent = txtEditor.getText();
		    if (null == receiverEmail
			    || "".equals(receiverEmail)
			    || null == subject
			    || "".equals(subject)
			    || null == emailContent
			    || "".equals(emailContent)
			    || receiverEmail
				    .matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$") == false) {
			System.out.println("error check data");

			// 提示错误(在本类中创建一个专门的输入校验方法)

			return;
		    }

		    EmailToSendInfo mail = new EmailToSendInfo();
		    mail.setSenderAddress((String) GlobalDataManager
			    .getData("localEmailAddress"));
		    mail.setSenderPassword((String) GlobalDataManager
			    .getData("localEmailPassword"));
		    mail.setReceiverAddress(receiverEmail);
		    mail.setSubject(subject);
		    mail.setSendDate(new Date());
		    mail.setContent(emailContent);

		    boolean succeed = false;
		    String errorMsg = "";
		    try {
			succeed = SimpleEmailSender.sendMail(mail);
		    } catch (Exception e1) {
			e1.printStackTrace();
			errorMsg = e1.getMessage();
		    }
		    if (succeed) {
			JOptionPane.showMessageDialog(null, "发送成功");
			frame.setVisible(false);
			frame.dispose();
		    } else {
			JOptionPane.showMessageDialog(null, "发送失败:" + errorMsg);
		    }
		}
	    }
	});

    }

    /**
     * 设置收件人
     * 
     * @param receiver
     *            收件人邮箱地址
     */
    public void setReceiver(String receiver) {
	txtReceiverEmail.setText(receiver);
    }

    public static void main(String[] args) {
	// 添加测试数据，方便测试
	GlobalDataManager.addData("localEmailHost", "yeah.net");
	GlobalDataManager
		.addData("localEmailAddress", "formytest_123@yeah.net");
	GlobalDataManager.addData("localEmailPassword", "formytest");
	new SendEmailPanel("test").setVisible(true);

    }

}
