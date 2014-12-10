package email.client.gui.login;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import email.client.dao.TAccountDao;
import email.client.dao.po.TAccount;
import email.client.gui.PanelConst;
import email.client.gui.main.MainUI;
import email.client.handler.CacheDataLoader;
import email.client.handler.login.EmailAccountCheck;
import email.client.util.GlobalDataManager;

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

	private JFrame parent;

	public LoginPanel(JFrame parent) {
		super();
		this.parent = parent;

		initComponment();
		initComponmentListener();

		setVisible(true);
	}

	private void initComponment() {
		lblEmailAddress = new JLabel("E-Mail地址 : ");

		cbxEmailHost = new JComboBox<String>(new String[] { "@yeah.net",
				"@126.com", "@163.com", "@qq.com" });

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
				host = host.substring(1);
				String emailAddress = txtEmailAddress.getText() + "@" + host;
				String password = txtEmailPassword.getText();
				boolean result = EmailAccountCheck.checkEmailAccount(host,
						"smtp", emailAddress, password);
				if (result) {
					// 添加新账户到数据库
					TAccount tAccount = new TAccount();
					tAccount.setEmailAddress(emailAddress);
					TAccountDao.addNewAccount(tAccount);
					System.out.println("添加账户" + emailAddress + " 到数据库,id = "
							+ tAccount.getId());
					// 添加账户数据到内存
					System.out.println("host = " + host);
					System.out.println("emailAddress = " + emailAddress);
					System.out.println("password = " + password);
					GlobalDataManager.addData("localEmailHost", host);
					GlobalDataManager
							.addData("localEmailAddress", emailAddress);
					GlobalDataManager.addData("localEmailPassword", password);
					GlobalDataManager.addData("localAccountId",
							tAccount.getId());

					enterMainUI();
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

	public void setWarningMsg(String msg) {
		txtErrorMsg.setFont(PanelConst.LOGIN_WARN_FONT);
		txtErrorMsg.setText(msg);
	}

	/**
	 * 进入主界面
	 */
	private void enterMainUI() {
		parent.setVisible(false);

		try {
			CacheDataLoader.loadCacheData();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"十分抱歉！加载数据时发生错误：" + e.getMessage(), "启动失败",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		new MainUI("暖冬 EmailClient");
		parent.dispose();
	}

}
