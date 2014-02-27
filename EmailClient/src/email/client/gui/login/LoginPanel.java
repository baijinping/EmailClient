package email.client.gui.login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import email.client.gui.PanelConst;
import email.client.handler.login.EmailAccountCheck;

public class LoginPanel extends JPanel {

	private JLabel lblEmailAddress;

	private JLabel lblEmailPassword;
	// 邮箱地址输入框
	private JTextField txtEmailAddress;
	// 支持的邮箱服务器
	private JComboBox<String> cbxEmailHost;
	// 邮箱密码输入框
	private JPasswordField txtEmailPassword;
	// 错误信息显示
	private JLabel txtErrorMsg;
	// 登录按钮
	private JButton btnLogin;

	// 组件是否初始化
	private boolean isComponmentInit = false;

	// 逻辑处理类
	private EmailAccountCheck accountCheck = new EmailAccountCheck();

	public LoginPanel() {

		initComponment();
		initComponmentListener();
		initWindowListener();

		setVisible(true);
	}

	private void initComponment() {
		lblEmailAddress = new JLabel("E-Mail地址 : ");

		cbxEmailHost = new JComboBox<String>(new String[] { "126.com",
				"163.com", "qq.com", "yeah.net" });

		lblEmailPassword = new JLabel("密码 : ");

		txtEmailAddress = new JTextField(20);

		txtEmailPassword = new JPasswordField(20);

		txtErrorMsg = new JLabel("");

		btnLogin = new JButton("登录");

		setLayout(new BorderLayout(5, 5));

		// 中间的显示区域, 包括2行输入框，1行错误信息显示
		GridBagLayout centerLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		JPanel centerPanel = new JPanel(centerLayout);

		c.gridwidth = 1;
		centerLayout.setConstraints(lblEmailAddress, c);
		centerLayout.setConstraints(txtEmailAddress, c);
		centerPanel.add(lblEmailAddress);
		centerPanel.add(txtEmailAddress);
		c.gridwidth = GridBagConstraints.REMAINDER;
		centerLayout.setConstraints(cbxEmailHost, c);
		centerPanel.add(cbxEmailHost);

		c.gridwidth = 1;
		centerLayout.setConstraints(lblEmailPassword, c);
		centerPanel.add(lblEmailPassword);
		c.gridwidth = GridBagConstraints.REMAINDER;
		centerLayout.setConstraints(txtEmailPassword, c);
		centerPanel.add(txtEmailPassword);
		centerLayout.setConstraints(txtErrorMsg, c);
		centerPanel.add(txtErrorMsg);

		add(centerPanel, BorderLayout.CENTER);

		// 下方的按钮区域
		add(btnLogin, BorderLayout.SOUTH);

		// 初始化完毕
		isComponmentInit = true;
	}

	private void initComponmentListener() {
		if (!isComponmentInit) {
			return;
		}

		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ("".equals(txtEmailAddress.getText())
						|| "".equals(txtEmailPassword.getText())) {
					return;
				}
				String host = cbxEmailHost.getItemAt(cbxEmailHost
						.getSelectedIndex());
				boolean result = EmailAccountCheck.checkEmailAccount(host,
						"smtp", txtEmailAddress.getText() + "@" + host,
						txtEmailPassword.getText());
				if (result) {
					int select = JOptionPane.showConfirmDialog(null,
							"成功创建账号，马上进入？", "", JOptionPane.OK_CANCEL_OPTION);
					if (select == JOptionPane.OK_OPTION) {
						JOptionPane.showMessageDialog(null, "进入成功！");
					}
				} else {
					if (host.contains("qq.com")) {
						setWarningMsg(PanelConst.LOGIN_QQMAIN_FAIL_MSG);
					} else {
						setWarningMsg(PanelConst.LOGIN_FAIL_MSG);
					}
				}
			}
		});
	}

	private void initWindowListener() {

	}

	public void setWarningMsg(String msg) {
		txtErrorMsg.setFont(PanelConst.LOGIN_WARN_FONT);
		txtErrorMsg.setText(msg);
	}

}
