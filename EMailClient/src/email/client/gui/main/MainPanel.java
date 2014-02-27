package email.client.gui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import email.client.gui.PanelConst;
import email.client.gui.receive.ReceivedEmailStore;
import email.client.gui.receive.SimpleEmailReceiver;
import email.client.util.GlobalDataManager;
import email.client.util.PathManager;

/**
 * 登录后的主界面
 * @author baikkp
 * 
 * 2014-2-19 下午9:40:39
 *
 */
public class MainPanel extends JFrame {
    
    /**
     * 负责管理邮件list中的内容
     * @author baikkp
     * 
     * 2014-2-25 下午10:21:56
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
    
    private JTree accountJTree;
    
    private JList<Object> emailJList;		// 还没将它放入到JScrollPanel中
    private EmailListItem dlm = new EmailListItem();
    
    private JPanel contentPanel;	// 邮件内容正文可直接放入这里显示
    
    public MainPanel(String title) {
	super(title);

	// 布局设计
	// 整体布局为BorderLayout
	setLayout(new BorderLayout());
	
	normalSetting();
	initComponment();
	initComponmentListener();
	initWindowListener();
	

	setVisible(true);
    }

    private void normalSetting() {
	// Look & Feel 观感主题设置
	try {
	    // 设置Nimbus皮肤
	    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	    
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
	    // 表示运行环境非JRE7
	    e.printStackTrace();
	}
	SwingUtilities.updateComponentTreeUI(this);
	
	// 点击红叉关闭窗口的行为
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	
	// 设置窗体位置大小为：水平垂直居中，大小为1/4
	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension screenSize = kit.getScreenSize();
	setBounds(screenSize.width/4, (int)(screenSize.height * 0.125), screenSize.width/2, (int)(screenSize.height * 0.75));
	
	// 设置背景颜色-失败？
	setBackground(PanelConst.LOGIN_BG_COLOR);
    }
    
    public void initComponment() {
	// =============================   上方  =======================================
	// 初始化工具栏
	toolBar = new JToolBar();
	// 不可拖动
	toolBar.setFloatable(false);
	// 鼠标移动至组件上方才显示边框
	toolBar.setRollover(true);
	add(toolBar, BorderLayout.NORTH);
	
	// 收取邮件按钮
	tbtnReceiveJButton = new JButton("收取",new ImageIcon(PathManager.getImageFilePath() + File.separator + "menu_receive.png"));
	toolBar.add(tbtnReceiveJButton);
	// 写邮件按钮
	tbtnWriteJButton = new JButton("写邮件", new ImageIcon(PathManager.getImageFilePath() + File.separator + "menu_write.png"));
	toolBar.add(tbtnWriteJButton);
	
	// =============================   主体   ======================================
	JSplitPane centerContentPanel = new JSplitPane();
	JSplitPane rightContentPanel = new JSplitPane();
	add(centerContentPanel, BorderLayout.CENTER);
	
	// 左侧账户邮件选择
	DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(GlobalDataManager.getData("localEmailAddress").toString());
	node1.add(new DefaultMutableTreeNode("收件箱"));
	node1.add(new DefaultMutableTreeNode("草稿箱"));
	node1.add(new DefaultMutableTreeNode("垃圾箱"));
	
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("邮箱账户");
	top.add(node1);
	
	accountJTree = new JTree(top);
	accountJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);	// 只能同时选中一个节点
	accountJTree.setMinimumSize(new Dimension(70, 200));
	centerContentPanel.setLeftComponent(accountJTree);
	
	// 中间邮件列表
	JScrollPane scrollPane = new JScrollPane();
	emailJList = new JList<>(dlm);					// 数据都保存在dlm中
	scrollPane.getViewport().setView(emailJList);
	scrollPane.setMinimumSize(new Dimension(70, 200));
	rightContentPanel.setLeftComponent(scrollPane);
	
	// 右侧邮件正文
	contentPanel = new JPanel();
	contentPanel.setMinimumSize(new Dimension(250, 200));
	rightContentPanel.setRightComponent(contentPanel);
	centerContentPanel.setRightComponent(rightContentPanel);
	
    }
    public void initComponmentListener() {
	// 收取邮件按钮
	tbtnReceiveJButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SimpleEmailReceiver emailReceiver = new SimpleEmailReceiver("imap");

		try {
		    // 接收邮件-未完成
		    ReceivedEmailStore receivedEmailStore = emailReceiver.handlerRective("yeah.net", "bluebird_fly@yeah.net", "bluebird");
		    // 将接收到的邮件放入本地数据库-未完成
		    receivedEmailStore.saveIntoDB();
		    // 读取邮件信息，并向dlm插入值-未完成
		    String[] emailSubjects = receivedEmailStore.getAllSubjects();
		    for (String subject : emailSubjects) {
			dlm.addElement(subject);
		    }
		} catch (Exception e1) {
		    e1.printStackTrace();
		    JOptionPane.showMessageDialog(null, "未知错误", "Error！", JOptionPane.ERROR_MESSAGE);
		}
	    }
	});
	// 写邮件按钮
	tbtnWriteJButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		JOptionPane.showConfirmDialog(null, "开始写邮件？", "等待确认", JOptionPane.YES_NO_OPTION);
	    }
	});
	// 文件夹树形表
	accountJTree.addTreeSelectionListener(new TreeSelectionListener() {
	    
	    @Override
	    public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
			accountJTree.getLastSelectedPathComponent();
		/* if noting is selected */
		if(node == null) return;
		
		/* retrieve the node that was selected */
		Object nodeInfoObject = node.getUserObject();
		if (node.isLeaf()) {
		    System.out.println(nodeInfoObject.toString());
		    // 处理逻辑
		    // 向emailJList赋值
		    if("收件箱".equals(nodeInfoObject.toString())) {;
			if(dlm.getLastShowContent() == null ||  "收件箱".equals( dlm.getLastShowContent().toString() ) == false) {
	    			// 向dlm插入数据
			    	
			    
			    
	    			dlm.setLastShowContent("收件箱");
			}
		    }
		    
		}
	    }
	});
    }

    public void initWindowListener() {
	
    }
    
    // 收取邮件

    public static void main(String[] args) {
	JFrame frame = new MainPanel("暖冬 电子邮件客户端");
	
    }
}
