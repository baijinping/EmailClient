package email.client.gui.blacklist;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import email.client.dao.TBlackListDao;
import email.client.dao.TLinkManDao;
import email.client.dao.po.TBlackList;
import email.client.dao.po.TLinkMan;
import email.client.gui.send.SendEmailPanel;
import email.client.util.GlobalDataManager;

public class BlackListPanel extends JPanel {

    // 左面板
    private JPanel leftPanel;
    private JList blackListList;
    private DefaultListModel<TBlackList> listModel;
    private JButton btnAddNewBlackList;

    // 右面板
    private JPanel rightPanel;
    private JLabel lblEmailAddress;
    private JLabel lblComment;
    private JTextField txtEmailAddress;
    private JTextArea txtComment;
    private JButton btnEditAndSave; // 点击”编辑“按钮之后，这个按钮会变成”保存“按钮
    private JButton btnDelete;;

    // 黑名单数据
    private final ArrayList<TBlackList> blackListData = (ArrayList<TBlackList>) GlobalDataManager
	    .getData("blackList");
    // 当前显示的黑名单数据对象
    private TBlackList curBlackList;
    private boolean isEditing = false;

    public BlackListPanel() {
	super();

	initComponment();
	initComponmentListener();

	setVisible(true);
	// 设置第一个黑名单被选中
	if (blackListData.size() > 0) {
	    blackListList.setSelectionInterval(0, 0);
	}
    }

    private void initComponment() {

	setLayout(new BorderLayout());

	lblEmailAddress = new JLabel("邮箱地址 : ");
	lblComment = new JLabel("备注 : ");
	txtEmailAddress = new JTextField(250);
	txtComment = new JTextArea(5, 20);
	btnEditAndSave = new JButton("编辑");
	btnDelete = new JButton("删除");
	btnAddNewBlackList = new JButton("新黑名单");

	JSplitPane splitPanel = new JSplitPane();
	add(splitPanel, BorderLayout.CENTER);
	/*
	 * 左面板
	 */
	leftPanel = new JPanel(new BorderLayout());
	leftPanel.setBorder(BorderFactory.createTitledBorder("黑名单列表"));
	splitPanel.setLeftComponent(leftPanel);

	JScrollPane scrollPane = new JScrollPane();
	listModel = new DefaultListModel<>();
	blackListList = new JList<>(listModel);
	scrollPane.getViewport().setView(blackListList);
	updateBlackList();
	leftPanel.add(scrollPane, BorderLayout.CENTER);

	leftPanel.add(btnAddNewBlackList, BorderLayout.SOUTH);

	/*
	 * 右面板
	 */
	rightPanel = new JPanel(new BorderLayout());
	rightPanel.setBorder(BorderFactory.createTitledBorder("黑名单信息"));
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
	hpg1.addComponent(lblEmailAddress);
	hpg1.addComponent(lblComment);

	// 书写文本框，左对齐
	GroupLayout.ParallelGroup hpg2 = layout
		.createParallelGroup(GroupLayout.Alignment.LEADING);
	hpg2.addComponent(txtEmailAddress);
	hpg2.addComponent(txtComment);

	// 保存水平设置
	layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(hpg1)
		.addGroup(hpg2));

	// 第一行
	GroupLayout.ParallelGroup vpg1 = layout
		.createParallelGroup(GroupLayout.Alignment.CENTER);
	vpg1.addComponent(lblEmailAddress);
	vpg1.addComponent(txtEmailAddress);

	// 第二行
	GroupLayout.ParallelGroup vpg2 = layout
		.createParallelGroup(GroupLayout.Alignment.CENTER);
	vpg2.addComponent(lblComment);
	vpg2.addComponent(txtComment);

	// 保存垂直设置
	layout.setVerticalGroup(layout.createSequentialGroup().addGroup(vpg1)
		.addGroup(vpg2));

	/*
	 * 添加底部按钮
	 */
	JPanel rightSouthPanel = new JPanel();
	rightSouthPanel.add(btnEditAndSave);
	rightSouthPanel.add(btnDelete);
	rightPanel.add(rightSouthPanel, BorderLayout.SOUTH);

    }

    private void initComponmentListener() {
	// 黑名单列表
	blackListList.addListSelectionListener(new ListSelectionListener() {
	    public void valueChanged(ListSelectionEvent e) {
		int selectedIndex = blackListList.getSelectedIndex();
		System.out.println("选中的是第" + selectedIndex + "项");
		if (selectedIndex != -1 && blackListData.size() > 0) {
		    TBlackList tBlackList = blackListData.get(selectedIndex);
		    curBlackList = tBlackList;
		    showBlackListInfo(tBlackList, false);
		}
	    }
	});
	// 新黑名单按钮
	btnAddNewBlackList.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddNewBlackList) {
		    // 创建新黑名单实例
		    TBlackList newBlackList = new TBlackList();
		    newBlackList.setEmailAddress("");
		    newBlackList.setComment("");
		    // 插入到列表中
		    listModel.addElement(newBlackList);
		    // 放入到内存中
		    blackListData.add(newBlackList);
		    // 放入到数据库中
		    TBlackListDao.addNewBlackList(newBlackList);
		    // 设置name="新黑名单", 并设为可编辑
		    showBlackListInfo(newBlackList, true);
		    // 在列表中选中该项
		    blackListList.setSelectionInterval(listModel.getSize() - 1,
			    listModel.getSize() - 1);
		}
	    }
	});
	// 删除黑名单按钮
	btnDelete.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnDelete) {
		    /*
		     * 删除黑名单，并显示下一个黑名单数据
		     */
		    int curSelectIndex = blackListList.getSelectedIndex();
		    // 从列表中删除
		    listModel.removeElement(curBlackList);
		    // 从内存中删除
		    blackListData.remove(curBlackList);
		    // 从数据库中删除
		    if (curBlackList.getId() != null) { // 如果是新黑名单，则没有id属性，则不用从数据库中删除
			TBlackListDao.deleteBlackList(curBlackList);
		    }
		    // 更新列表
		    updateBlackList();
		    // 显示下一个黑名单
		    if (blackListData.size() != 0) {
			// 如果删除的是最后一个黑名单，则索引要减1
			if (curSelectIndex >= blackListData.size()) {
			    curSelectIndex--;
			}
			System.out.println("删除黑名单成功并显示第" + curSelectIndex
				+ "黑名单");
			blackListList.setSelectionInterval(curSelectIndex,
				curSelectIndex);
			TBlackList tBlackList = blackListData
				.get(curSelectIndex);
			showBlackListInfo(tBlackList, false);
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
		    curBlackList.setEmailAddress(txtEmailAddress.getText());
		    curBlackList.setComment(txtComment.getText());
		    // 提交更改到数据库
		    TBlackListDao.updateBlackList(curBlackList);

		    JOptionPane.showMessageDialog(null, "更新成功");
		}
	    }
	});
    }

    /**
     * 显示指定黑名单的信息，并初始化编辑状态
     * 
     * @param index
     *            显示的索引
     * @param editable
     */
    public void showBlackListInfo(int index, boolean editable) {
	if(blackListData.size() == 0) {
	    setEditable(false);
	    return;
	}
	TBlackList tBlackList = listModel.get(index);
	showBlackListInfo(tBlackList, editable);
    }

    /**
     * 显示指定黑名单的信息，并初始化编辑状态
     * 
     * @param tBlackList
     */
    public void showBlackListInfo(TBlackList tBlackList, boolean editable) {
	txtEmailAddress.setText(tBlackList.getEmailAddress());
	txtComment.setText(tBlackList.getComment());

	if (editable) {
	    btnEditAndSave.setText("保存");
	} else {
	    btnEditAndSave.setText("编辑");
	}
	setEditable(editable);
	isEditing = editable;
	curBlackList = tBlackList;
    }

    /**
     * 更新黑名单列表
     */
    public void updateBlackList() {
	listModel.removeAllElements();
	for (Iterator<TBlackList> iterator = blackListData.iterator(); iterator
		.hasNext();) {
	    TBlackList tBlackList = (TBlackList) iterator.next();
	    listModel.addElement(tBlackList);
	}
    }

    /**
     * 设置文本框是否可编辑
     * 
     * @param editable
     */
    private void setEditable(boolean editable) {

	txtEmailAddress.setEnabled(editable);
	txtComment.setEnabled(editable);
    }
}
