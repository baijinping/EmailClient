package email.client.gui.send;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

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
import email.client.util.GlobalDataManager;

/**
 * 写邮件的界面
 * 
 * @author baikkp
 * 
 *         2013-11-24 下午6:49:50
 * 
 */
public class SendEmailPanel extends JFrame {
    
    private JTextPane txtEditor;
    
    private JButton btnSend;
    
    private JLabel lblReceiverEmail;
    
    private JTextField txtReceiverEmail;
    
    private JLabel lblSubject;
    
    private JTextField txtSubject;

    public SendEmailPanel() {
	
	setLayout(new BorderLayout());
	
	normalSetting();
	
	initComponment();
	initComponmentListener();
	
	setVisible(true);
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
	//lblSubject.setBounds(50, 80, 100, 20);
	topPanel.add(lblSubject);
	
	txtSubject = new JTextField(50);
	topPanel.add(txtSubject);
	
	txtEditor = new JTextPane();
	txtEditor.setBounds(100, 100, 300, 400);
	add(txtEditor, BorderLayout.CENTER);
	
	btnSend = new JButton("发邮件");
	btnSend.setBounds(200, 450, 20, 25);
	add(btnSend, BorderLayout.SOUTH);
	
    }

    private void normalSetting() {
	try {
	    // 设置Nimbus皮肤
	    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
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
	setBounds(screenSize.width/4, screenSize.height/4, screenSize.width/2, screenSize.height/2);
	
	// 设置背景颜色-失败？
	setBackground(PanelConst.LOGIN_BG_COLOR);
    }
    
    private void initComponmentListener() {
	btnSend.addActionListener(new ActionListener() {
	   @Override
	    public void actionPerformed(ActionEvent e) {
	       if(e.getSource() == btnSend) {
		   String receiverEmail = txtReceiverEmail.getText();
		   String subject = txtSubject.getText();
		   String emailContent = txtEditor.getText();
		   if (null == receiverEmail 
			   || "".equals(receiverEmail) 
			   || null == subject 
			   || "".equals(subject)
			   || null == emailContent
			   || "".equals(emailContent)
			   || receiverEmail.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$") == false) {
		       System.out.println("error check data");
		       return;
		   }
		   
		   EmailToSendInfo mail = new EmailToSendInfo();
		   mail.setSenderAddress((String) GlobalDataManager.getData("localEmailAddress"));
		   mail.setSenderPassword((String) GlobalDataManager.getData("localEmailPassword"));
		   mail.setReceiverAddress(receiverEmail);
		   mail.setSubject(subject);
		   mail.setSendDate(new Date());
		   mail.setContent(emailContent);
		   if(SimpleEmailSender.sendMain(mail)) {
		       JOptionPane.showMessageDialog(null, "发送成功");
		   } else {
		       JOptionPane.showMessageDialog(null, "发送失败");
		   }
	       }
	    } 
	});
    }

    public static void main(String[] args) {
	GlobalDataManager.addData("localEmailAddress", "bluebird_fly@yeah.net");
	GlobalDataManager.addData("localEmailPassword", "bluebird");
	new SendEmailPanel();
    }
}
