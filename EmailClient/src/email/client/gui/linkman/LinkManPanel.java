package email.client.gui.linkman;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import email.client.dao.TLinkManDao;
import email.client.dao.po.TLinkMan;
import email.client.gui.send.SendEmailPanel;
import email.client.handler.CacheDataLoader;
import email.client.util.GlobalDataManager;

/**
 * 联系人管理界面
 * 
 * @author baikkp
 * 
 *         2014-3-24 下午10:12:53
 * 
 */
@SuppressWarnings("unchecked")
public class LinkManPanel extends JPanel {

    // 左面板
    private JPanel leftPanel;
    private JList linkManList;
    private DefaultListModel<TLinkMan> listModel;
    private JButton btnAddLinkMan;

    // 右面板
    private JPanel rightPanel;
    private JLabel lblName;
    private JLabel lblEmailAddress;
    private JLabel lblPhoneNumber;
    private JLabel lblComment;
    private JTextField txtName;
    private JTextField txtEmailAddress;
    private JTextField txtPhoneNumber;
    private JTextArea txtComment;
    private JButton btnEditAndSave; // 点击”编辑“按钮之后，这个按钮会变成”保存“按钮
    private JButton btnDelete;;
    private JButton btnSendEmail;

    // 联系人数据
    private final ArrayList<TLinkMan> linkManData = (ArrayList<TLinkMan>) GlobalDataManager
	    .getData("linkMan");
    // 当前显示的联系人数据对象
    private TLinkMan curLinkMan;
    private boolean isEditing = false;

    public LinkManPanel() {
	super();

	initComponment();
	initComponmentListener();

	setVisible(true);
	// 设置第一个联系人被选中
	if(linkManData.size() > 0) {
	    linkManList.setSelectionInterval(0, 0);
	}
    }

    private void initComponment() {

	setLayout(new BorderLayout());

	lblName = new JLabel("姓名 : ");
	lblEmailAddress = new JLabel("邮箱地址 : ");
	lblPhoneNumber = new JLabel("移动电话 : ");
	lblComment = new JLabel("备注 : ");
	txtName = new JTextField(20);
	txtEmailAddress = new JTextField(250);
	txtPhoneNumber = new JTextField(64);
	txtComment = new JTextArea(5, 20);
	btnEditAndSave = new JButton("编辑");
	btnDelete = new JButton("删除");
	btnSendEmail = new JButton("发送邮件");
	btnAddLinkMan = new JButton("新联系人");

	JSplitPane splitPanel = new JSplitPane();
	add(splitPanel, BorderLayout.CENTER);
	/*
	 * 左面板
	 */
	leftPanel = new JPanel(new BorderLayout());
	leftPanel.setBorder(BorderFactory.createTitledBorder("联系人列表"));
	splitPanel.setLeftComponent(leftPanel);

	JScrollPane scrollPane = new JScrollPane();
	listModel = new DefaultListModel<>();
	linkManList = new JList<>(listModel);
	scrollPane.getViewport().setView(linkManList);
	updateLinkManList();
	leftPanel.add(scrollPane, BorderLayout.CENTER);

	leftPanel.add(btnAddLinkMan, BorderLayout.SOUTH);

	/*
	 * 右面板
	 */
	rightPanel = new JPanel(new BorderLayout());
	rightPanel.setBorder(BorderFactory.createTitledBorder("联系人信息"));
	splitPanel.setRightComponent(rightPanel);

	// 设置布局方式
	JPanel rightCenterPanel = new JPanel();
	GroupLayout layout = new GroupLayout(rightCenterPanel);
	rightCenterPanel.setLayout(layout);
	rightPanel.add(rightCenterPanel, BorderLayout.CENTER);

	/*
	 * 进行布局
	 */
	// 自动设定组件、组之间的间隙
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);

	// 标签，右对齐
	GroupLayout.ParallelGroup hpg1 = layout
		.createParallelGroup(GroupLayout.Alignment.TRAILING);
	hpg1.addComponent(lblName);
	hpg1.addComponent(lblEmailAddress);
	hpg1.addComponent(lblPhoneNumber);
	hpg1.addComponent(lblComment);

	// 书写文本框，左对齐
	GroupLayout.ParallelGroup hpg2 = layout
		.createParallelGroup(GroupLayout.Alignment.LEADING);
	hpg2.addComponent(txtName);
	hpg2.addComponent(txtEmailAddress);
	hpg2.addComponent(txtPhoneNumber);
	hpg2.addComponent(txtComment);

	// 保存水平设置
	layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(hpg1)
		.addGroup(hpg2));

	// 第一行
	GroupLayout.ParallelGroup vpg1 = layout
		.createParallelGroup(GroupLayout.Alignment.CENTER);
	vpg1.addComponent(lblName);
	vpg1.addComponent(txtName);

	// 第二行
	GroupLayout.ParallelGroup vpg2 = layout
		.createParallelGroup(GroupLayout.Alignment.CENTER);
	vpg2.addComponent(lblEmailAddress);
	vpg2.addComponent(txtEmailAddress);

	// 第三行
	GroupLayout.ParallelGroup vpg3 = layout
		.createParallelGroup(GroupLayout.Alignment.CENTER);
	vpg3.addComponent(lblPhoneNumber);
	vpg3.addComponent(txtPhoneNumber);

	// 第四行
	GroupLayout.ParallelGroup vpg4 = layout
		.createParallelGroup(GroupLayout.Alignment.CENTER);
	vpg4.addComponent(lblComment);
	vpg4.addComponent(txtComment);

	// 保存垂直设置
	layout.setVerticalGroup(layout.createSequentialGroup().addGroup(vpg1)
		.addGroup(vpg2).addGroup(vpg3).addGroup(vpg4));

	/*
	 * 添加底部按钮
	 */
	JPanel rightSouthPanel = new JPanel();
	rightSouthPanel.add(btnSendEmail);
	rightSouthPanel.add(btnEditAndSave);
	rightSouthPanel.add(btnDelete);
	rightPanel.add(rightSouthPanel, BorderLayout.SOUTH);

    }

    private void initComponmentListener() {
	// 联系人列表
	linkManList.addListSelectionListener(new ListSelectionListener() {
	    public void valueChanged(ListSelectionEvent e) {
		int selectedIndex = linkManList.getSelectedIndex();
		System.out.println("选中的是第" + selectedIndex + "项");
		if (selectedIndex != -1 && linkManData.size() > 0) {
		    TLinkMan tLinkMan = linkManData.get(selectedIndex);
		    curLinkMan = tLinkMan;
		    showLinkManInfo(tLinkMan, false);
		}
	    }
	});
	// 新联系人按钮
	btnAddLinkMan.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddLinkMan) {
		    // 创建新联系人实例
		    TLinkMan newLinkMan = new TLinkMan();
		    newLinkMan.setName("新联系人");
		    newLinkMan.setEmailAddress("");
		    newLinkMan.setPhoneNumber("");
		    newLinkMan.setComment("");
		    // 插入到列表中
		    listModel.addElement(newLinkMan);
		    // 放入到内存中
		    linkManData.add(newLinkMan);
		    // 放入到数据库中
		    TLinkManDao.addNewLinkMan(newLinkMan);
		    // 设置name="新联系人", 并设为可编辑
		    showLinkManInfo(newLinkMan, true);
		    // 在列表中选中该项
		    linkManList.setSelectionInterval(listModel.getSize() - 1,
			    listModel.getSize() - 1);
		}
	    }
	});
	// 发送邮件按钮
	btnSendEmail.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSendEmail) {
		    SendEmailPanel sendPanel = new SendEmailPanel("发送邮件至 : "
			    + curLinkMan.getName());
		    sendPanel.setReceiver(curLinkMan.getEmailAddress());
		}
	    }
	});
	// 删除联系人按钮
	btnDelete.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnDelete) {
		    int operate = JOptionPane.showConfirmDialog(null,
			    "删除联系人后无法恢复，确认删除？", "操作确认",
			    JOptionPane.OK_CANCEL_OPTION);
		    if (JOptionPane.OK_OPTION == operate) {
			/*
			 * 删除联系人，并显示下一个联系人数据
			 */
			int curSelectIndex = linkManList.getSelectedIndex();
			// 从列表中删除
			listModel.removeElement(curLinkMan);
			// 从内存中删除
			linkManData.remove(curLinkMan);
			// 从数据库中删除
			if (curLinkMan.getId() != null) { // 如果是新联系人，则没有id属性，则不用从数据库中删除
			    TLinkManDao.deleteLinkMan(curLinkMan);
			}
			// 更新列表
			updateLinkManList();
			// 显示下一个联系人
			if (linkManData.size() != 0) {
			    // 如果删除的是最后一个联系人，则索引要减1
			    if(curSelectIndex >= linkManData.size()) {
				curSelectIndex --;
			    }
			    System.out.println("删除联系人成功并显示第" + curSelectIndex + "个联系人");
			    linkManList.setSelectionInterval(curSelectIndex,
				    curSelectIndex);
			    TLinkMan tLinkMan = linkManData.get(curSelectIndex);
			    showLinkManInfo(tLinkMan, false);
			}

		    }
		}
	    }
	});
	// 编辑&保存按钮
	btnEditAndSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		txtEmailAddress.setToolTipText("这就是ToolTipText");

		if (!isEditing) { // 如果此时显示"编辑"
		    isEditing = true;
		    setEditable(true);
		    btnEditAndSave.setText("保存");
		} else { // 如果此时显示"保存"
		    isEditing = false;
		    setEditable(false);
		    btnEditAndSave.setText("编辑");

		    // 更改内存中数据
		    curLinkMan.setName(txtName.getText());
		    curLinkMan.setEmailAddress(txtEmailAddress.getText());
		    curLinkMan.setPhoneNumber(txtPhoneNumber.getText());
		    curLinkMan.setComment(txtComment.getText());
		    // 提交更改到数据库
		    TLinkManDao.updateLinkMan(curLinkMan);

		    JOptionPane.showMessageDialog(null, "更新成功");
		}
	    }
	});
    }

    /**
     * 显示指定联系人的信息，并初始化编辑状态
     * @param index 显示的索引
     * @param editable
     */
    public void showLinkManInfo(int index, boolean editable) {
	if(linkManData.size() == 0) {
	    setEditable(false);
	    return;
	}
	TLinkMan tLinkMan = listModel.get(index);
	showLinkManInfo(tLinkMan, editable);
    }
    
    /**
     * 显示指定联系人的信息，并初始化编辑状态
     * 
     * @param tLinkMan
     */
    public void showLinkManInfo(TLinkMan tLinkMan, boolean editable) {
	txtName.setText(tLinkMan.getName());
	txtEmailAddress.setText(tLinkMan.getEmailAddress());
	txtPhoneNumber.setText(tLinkMan.getPhoneNumber());
	txtComment.setText(tLinkMan.getComment());

	if(editable) {
	    btnEditAndSave.setText("保存");
	} else {
	    btnEditAndSave.setText("编辑");
	}
	setEditable(editable);
	isEditing = editable;
	curLinkMan = tLinkMan;
    }

    /**
     * 更新联系人列表
     */
    public void updateLinkManList() {
	listModel.removeAllElements();
	for (Iterator<TLinkMan> iterator = linkManData.iterator(); iterator
		.hasNext();) {
	    TLinkMan tLinkMan = (TLinkMan) iterator.next();
	    listModel.addElement(tLinkMan);
	}
    }

    /**
     * 设置文本框是否可编辑
     * 
     * @param editable
     */
    private void setEditable(boolean editable) {
	/*
	 * txtName.setEditable(editable); txtEmailAddress.setEditable(editable);
	 * txtPhoneNumber.setEditable(editable);
	 * txtComment.setEditable(editable);
	 */

	txtName.setEnabled(editable);
	txtEmailAddress.setEnabled(editable);
	txtPhoneNumber.setEnabled(editable);
	txtComment.setEnabled(editable);
    }

}
