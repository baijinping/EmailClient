package email.client.gui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.mail.Message;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import email.client.dao.po.TEmail;
import email.client.gui.UIInstance;
import email.client.gui.blacklist.BlackListUI;
import email.client.gui.linkman.LinkManUI;
import email.client.gui.receive.ReceivedEmailStore;
import email.client.gui.send.SendEmailPanel;
import email.client.handler.receive.SimpleEmailReceiver;
import email.client.util.GlobalDataManager;
import email.client.util.PathManager;
import email.client.util.email.EmailDataUtil;

/**
 * 登录后的主界面
 * 
 * @author baikkp
 * 
 *         2014-2-19 下午9:40:39
 * 
 */
public class MainPanel extends JPanel {

    /**
     * 负责管理邮件list中的内容
     * 
     * @author baikkp
     * 
     *         2014-2-25 下午10:21:56
     * 
     */
    class EmailListItem extends DefaultListModel {
	// 其值将被设置为左侧树形列表的一个子节点
	// 当点击一个子节点时（如收件箱），则会判断当前是否显示该子节点对应的内容
	// 如果是的话，就不会再重新渲染这个列表了
	private Object lastShowContent = null;

	public EmailListItem() {
	    super();
	}

	public Object getLastShowContent() {
	    return lastShowContent;
	}

	public void setLastShowContent(Object lastShowContent) {
	    this.lastShowContent = lastShowContent;
	}

    }

    private JToolBar toolBar;
    private JButton tbtnReceiveJButton;
    private JButton tbtnWriteJButton;
    private JButton tbtnLinkManJButton;
    private JButton tbtnBlackListJButton;

    private JTextField searchField;
    private JButton searchJButton;
    
    private JTree accountJTree;

    private JList<Object> emailJList; // 还没将它放入到JScrollPanel中
    private EmailListItem dlm = new EmailListItem();

    private EmailContentPanel emailContentPanel;

    public MainPanel() {
	super();

	// 布局设计
	// 整体布局为BorderLayout
	setLayout(new BorderLayout());

	initComponment();
	initComponmentListener();

	setVisible(true);
    }

    public void initComponment() {
	// ========== 上方 ==========
	JPanel topPanel = new JPanel();
	add(topPanel, BorderLayout.NORTH);
	
	/*
	 *  左边的按钮工具栏
	 */
	toolBar = new JToolBar();
	// 不可拖动
	toolBar.setFloatable(false);
	// 鼠标移动至组件上方才显示边框
	toolBar.setRollover(true);

	// 收取邮件按钮
	tbtnReceiveJButton = new JButton("收取", new ImageIcon(
		PathManager.getImageFilePath() + File.separator
			+ "mail_exchange.png"));
	toolBar.add(tbtnReceiveJButton);
	// 写邮件按钮
	tbtnWriteJButton = new JButton("写邮件", new ImageIcon(
		PathManager.getImageFilePath() + File.separator
			+ "mail_write.png"));
	toolBar.add(tbtnWriteJButton);
	// 联系人按钮
	tbtnLinkManJButton = new JButton("联系人", new ImageIcon(
		PathManager.getImageFilePath() + File.separator
			+ "mails.png"));
	toolBar.add(tbtnLinkManJButton);
	// 黑名单按钮
	tbtnBlackListJButton = new JButton("黑名单", new ImageIcon(
		PathManager.getImageFilePath() + File.separator
			+ "mail_filter.png"));
	toolBar.add(tbtnBlackListJButton);

	topPanel.add(toolBar);
	
	/*
	 * 右边的检索输入
	 */
	// 最长支持50长度的字符检索
	searchField = new JTextField(20);
	searchField.setDocument(new JTextFieldLimit(20));
	topPanel.add(searchField);
	searchJButton = new JButton("全文检索");
	topPanel.add(searchJButton);
	
	// ============================= 主体
	// ======================================
	JSplitPane centerContentPanel = new JSplitPane();
	JSplitPane rightContentPanel = new JSplitPane();
	add(centerContentPanel, BorderLayout.CENTER);

	// 左侧账户邮件选择
	DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
		GlobalDataManager.getData("localEmailAddress").toString());
	node1.add(new DefaultMutableTreeNode("收件箱"));

	DefaultMutableTreeNode top = new DefaultMutableTreeNode("邮箱账户");
	top.add(node1);

	accountJTree = new JTree(top);
	accountJTree.getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION); // 只能同时选中一个节点
	accountJTree.setMinimumSize(new Dimension(70, 200));
	centerContentPanel.setLeftComponent(accountJTree);

	// 中间邮件列表
	JScrollPane scrollPane = new JScrollPane();
	emailJList = new JList<>(dlm); // 数据都保存在dlm中
	scrollPane.getViewport().setView(emailJList);
	scrollPane.setMinimumSize(new Dimension(70, 200));
	rightContentPanel.setLeftComponent(scrollPane);

	// 右侧邮件正文
	emailContentPanel = new EmailContentPanel(this);
	emailContentPanel.setMinimumSize(new Dimension(250, 200));
	emailContentPanel.setNormal(false); // 设置邮件正文不可见

	rightContentPanel.setRightComponent(emailContentPanel);
	centerContentPanel.setRightComponent(rightContentPanel);
    }

    public void initComponmentListener() {
	/*
	 * 工具栏按钮
	 */
	// 收取邮件按钮
	tbtnReceiveJButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tbtnReceiveJButton) {
		    handleReceiveEmail();
		}
	    }
	});
	// 写邮件按钮
	tbtnWriteJButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		new SendEmailPanel("发送邮件");
	    }
	});
	// 联系人按钮
	tbtnLinkManJButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (UIInstance.linkManUI == null) {
		    System.out.println("新建联系人界面");
		    UIInstance.linkManUI = new LinkManUI();
		} else {
		    System.out.println("使用旧的联系人界面");
		    UIInstance.linkManUI.show();
		}
	    }
	});
	// 黑名单按钮
	tbtnBlackListJButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (UIInstance.blackListUI == null) {
		    System.out.println("新建黑名单界面");
		    UIInstance.blackListUI = new BlackListUI();
		} else {
		    System.out.println("使用旧的黑名单界面");
		    UIInstance.blackListUI.show();
		}
	    }
	});

	/*
	 * 检索
	 */
	// 全文搜索按钮
	searchJButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String wordForSearch = searchField.getText();
		
		// 搜索词为空时不处理
		if(null == wordForSearch || "".equals(wordForSearch)) {
		    return;
		}
		
		System.out.println("开始进行全文搜索，查询：" + wordForSearch);
		
		// 删除邮件列表
		dlm.removeAllElements();
		// 遍历邮件列表，将主题或正文中包含检索字符串的邮件放入邮件列表
		List<TEmail> emailList = EmailDataUtil.getAllEmail();
		for (Iterator iterator = emailList.iterator(); iterator
			.hasNext();) {
		    TEmail tEmail = (TEmail) iterator.next();
		    if(tEmail.getSubject() != null && tEmail.getSubject().contains(wordForSearch) && tEmail.getContent() != null && tEmail.getContent().contains(wordForSearch)) {
			System.out.println("检索到符合，插入邮件 -- " + tEmail.getSubject());
			dlm.addElement(tEmail);
		    }
		}
		
		System.out.println("全文检索完毕");
	    }
	});
	// 搜索框
	// 当搜索框内的文字被清空，则还原所有邮件到列表
	searchField.getDocument().addDocumentListener(new DocumentListener() {
	    public void removeUpdate(DocumentEvent e) {
		handle();
	    }
	    
	    public void insertUpdate(DocumentEvent e) {
		handle();
	    }
	    
	    public void changedUpdate(DocumentEvent e) {
		handle();
	    }
	    
	    private void handle() {
		if("".equals(searchField.getText())) {
		    updateEmailList(0);
		}
	    }
	});
	
	/*
	 * 大控件
	 */
	// 文件夹树形表
	accountJTree.addTreeSelectionListener(new TreeSelectionListener() {

	    @Override
	    public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) accountJTree
			.getLastSelectedPathComponent();
		/* if noting is selected */
		if (node == null)
		    return;

		/* retrieve the node that was selected */
		Object nodeInfoObject = node.getUserObject();
		if (node.isLeaf()) {
		    String leafName = nodeInfoObject.toString();
		    String[] folderName = new String[] { "收件箱", "草稿箱", "垃圾箱" };
		    System.out.println("选中了" + leafName);
		    // 处理逻辑
		    int flag = 0;
		    for (int i = 0; i < folderName.length; ++i) {
			if (folderName[i].equals(leafName)) {
			    flag = i;
			    break;
			}
		    }

		    String curFolderName = folderName[flag];

		    if (dlm.getLastShowContent() == null
			    || curFolderName.equals(dlm.getLastShowContent()
				    .toString()) == false) {
			// 更新邮件列表
			System.out.println("flag = " + flag);
			updateEmailList(flag);
			dlm.setLastShowContent(curFolderName);
		    }

		    /*
		     * if ("收件箱".equals(leafName)) { if
		     * (dlm.getLastShowContent() == null ||
		     * "收件箱".equals(dlm.getLastShowContent() .toString()) ==
		     * false) { // 更新邮件列表 updateEmailList();
		     * dlm.setLastShowContent("收件箱"); } }
		     */

		}
	    }
	});
	// 邮件列表
	emailJList.addListSelectionListener(new ListSelectionListener() {

	    @Override
	    public void valueChanged(ListSelectionEvent event) {
		int selectedIndex = emailJList.getSelectedIndex();
		if (selectedIndex != -1) {
		    TEmail tEmail = (TEmail) dlm.getElementAt(selectedIndex);
		    System.out.println("subject = " + tEmail.getSubject());
		    emailContentPanel.setSubjectText(tEmail.getSubject());
		    emailContentPanel.setFromText(tEmail.getFromName(),
			    tEmail.getFromEmail());
		    emailContentPanel.setEmailContent(tEmail.getContent());
		    emailContentPanel.setSendDate(tEmail.getSendDate());
		    Message msg = ReceivedEmailStore.getInstance().getMessage(
			    tEmail.getUuid());
		    emailContentPanel.setMessage(msg);

		    // 显示邮件正文
		    emailContentPanel.setNormal(true);

		}
	    }
	});
	
    }

    /**
     * 处理接收邮件，作为收件按钮的处理函数
     */
    public void handleReceiveEmail() {
	SimpleEmailReceiver emailReceiver = new SimpleEmailReceiver("imap");
	try {
	    // 如果不是第一次接收邮件，则关闭上一次接收邮件时创建的连接
	    if (ReceivedEmailStore.getInstance().getFolder() != null) {
		ReceivedEmailStore.getInstance().getFolder().close(true); // 关闭为true，表示把本地的更改同步到服务端
	    }

	    // 接收邮件
	    ReceivedEmailStore receivedEmailStore = emailReceiver
		    .handlerRective(GlobalDataManager.getData("localEmailHost")
			    .toString(),
			    GlobalDataManager.getData("localEmailAddress")
				    .toString(),
			    GlobalDataManager.getData("localEmailPassword")
				    .toString());
	    // 将接收到的邮件放入本地数据库
	    receivedEmailStore.saveIntoDB();
	    // 更新邮件列表
	    updateEmailList(0);
	    // 关闭连接
	    // emailReceiver.close();
	} catch (Exception e1) {
	    e1.printStackTrace();
	    JOptionPane.showMessageDialog(null, "错误！[" + e1.getMessage() + "]",
		    "Error！", JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * 更新邮件列表 参数约定： flag(int): 0:表示选择的是收件箱，删除原有值，插入新值 1:表示选择的是其他文件夹，删除原有值，不插入值
     */
    public void updateEmailList(int flag) {
	dlm.removeAllElements();
	if (flag == 0) {
	    // 读取邮件信息，并向dlm插入值
	    List<TEmail> tEmailList = EmailDataUtil.getAllEmail();

	    for (TEmail tEmail : tEmailList) {
		
		if (!dlm.contains(tEmail)) {
		    dlm.addElement(tEmail);
		} else {
		    System.out.println(tEmail.getSubject()
			    + " already exists in JList");
		}
	    }
	}
    }

    /**
     * 让邮件正文显示列表中的下一封邮件。 如果到达邮件末尾，则不显示正文。
     * 
     * @param needDelete
     *            是否删除当前选中的邮件
     */
    public void setEmailContentToNext(boolean needDelete) {
	System.out.println("setEmailContentToNext(" + needDelete + "):");

	// 保存当前选中的邮件
	int selectedIndex = emailJList.getSelectedIndex();
	System.out.println("selectedIndex = " + selectedIndex);

	// 如果是手动删除，那么首先找到当前选中的邮件，从内存和数据库中删除。
	// 然后重新加载邮件列表
	if (needDelete) {
	    TEmail tEmail = (TEmail) dlm.remove(selectedIndex);
	    EmailDataUtil.deleteOldEmail(tEmail);
	}
	// 如果不是手动删除，仅仅是想要显示下一个邮件信息，则索引加1
	else {
	    selectedIndex += 1;
	}

	// 如果索引超界，则显示最后一个邮件
	if (selectedIndex >= dlm.getSize()) {
	    selectedIndex = dlm.getSize() - 1;
	}

	if (selectedIndex != -1) {
	    TEmail tEmail = (TEmail) dlm.getElementAt(selectedIndex);
	    System.out.println("subject = " + tEmail.getSubject());
	    emailContentPanel.setSubjectText(tEmail.getSubject());
	    emailContentPanel.setFromText(tEmail.getFromName(),
		    tEmail.getFromEmail());
	    emailContentPanel.setEmailContent(tEmail.getContent());
	    emailContentPanel.setSendDate(tEmail.getSendDate());
	    emailContentPanel.setMessage(ReceivedEmailStore.getInstance()
		    .getMessage(tEmail.getUuid()));

	    // 显示邮件正文
	    emailContentPanel.setNormal(true);

	    // 该邮件项在列表中被选中
	    emailJList.setSelectionInterval(selectedIndex, selectedIndex);
	}
	// 如果邮件列表为空，则不显示邮件内容
	else {
	    emailContentPanel.setNormal(false);
	}
    }

}
