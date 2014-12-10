package email.client.gui.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.Flags.Flag;
import javax.mail.MessagingException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import email.client.dao.TLinkManDao;
import email.client.dao.po.TLinkMan;
import email.client.gui.send.SendEmailPanel;
import email.client.util.GlobalDataManager;

/**
 * 负责显示邮件内容，包括发件人，发送时间，主题，正文，回复按钮
 * 
 * @author baikkp
 * 
 *         2014-3-1 下午12:19:04
 * 
 */
public class EmailContentPanel extends JPanel {

    // ======== UI Data ===========
    private JPanel wrapperPanel;
    private JLabel lblFrom;
    private JLabel lblFromText;
    private JLabel lblSubject;
    private JLabel lblSubjectText;
    private JLabel lblSendDate;
    private JLabel lblSendDateText;
    private JButton btnAddToLinkMan;
    private JButton btnReply;
    private JButton btnDelete;
    private JEditorPane contentEditorPane;

    // ======== Logic Data ===========
    private String fromName;
    private String fromEmail;
    private String subject;
    private Message message;

    // ======== Call Back Data =========
    private MainPanel mainPanel;

    public EmailContentPanel(MainPanel mainPanel) {
	super();

	this.mainPanel = mainPanel;

	setLayout(new BorderLayout());

	initComponment();
	initComponmentListener();

	setVisible(true);
    }

    private void initComponment() {
	wrapperPanel = new JPanel(new BorderLayout());

	lblFrom = new JLabel("发件人：");
	lblSubject = new JLabel("主题：");
	lblSendDate = new JLabel("发送日期：");
	lblFromText = new JLabel();
	lblSubjectText = new JLabel();
	lblSendDateText = new JLabel();
	btnAddToLinkMan = new JButton("添加到联系人");
	btnReply = new JButton("回复");
	btnDelete = new JButton("删除");

	JPanel topPanel = new JPanel();
	GroupLayout layout = new GroupLayout(topPanel);
	topPanel.setLayout(layout);

	// 自动设定组件、组之间的间隙
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);

	// 第一列：标签，右对齐
	GroupLayout.ParallelGroup hpg1 = layout
		.createParallelGroup(GroupLayout.Alignment.TRAILING);
	hpg1.addComponent(lblFrom);
	hpg1.addComponent(lblSubject);
	hpg1.addComponent(lblSendDate);

	// 第二列：内容标签，左对齐
	GroupLayout.ParallelGroup hpg2 = layout
		.createParallelGroup(GroupLayout.Alignment.LEADING);
	hpg2.addComponent(lblFromText);
	hpg2.addComponent(lblSubjectText);
	hpg2.addComponent(lblSendDateText);

	// 第三列：操作按钮，居中
	GroupLayout.ParallelGroup hpg3 = layout
		.createParallelGroup(GroupLayout.Alignment.CENTER);
	hpg3.addComponent(btnAddToLinkMan);
	hpg3.addComponent(btnReply);
	hpg3.addComponent(btnDelete);

	// 保存水平设置
	layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(hpg1)
		.addGroup(hpg2).addGroup(hpg3));

	// 第一行
	GroupLayout.ParallelGroup vpg1 = layout
		.createParallelGroup(GroupLayout.Alignment.TRAILING);
	vpg1.addComponent(lblFrom);
	vpg1.addComponent(lblFromText);
	vpg1.addComponent(btnAddToLinkMan);

	// 第二行
	GroupLayout.ParallelGroup vpg2 = layout
		.createParallelGroup(GroupLayout.Alignment.LEADING);
	vpg2.addComponent(lblSubject);
	vpg2.addComponent(lblSubjectText);
	vpg2.addComponent(btnReply);

	// 第三行
	GroupLayout.ParallelGroup vpg3 = layout
		.createParallelGroup(GroupLayout.Alignment.BASELINE);
	vpg3.addComponent(lblSendDate);
	vpg3.addComponent(lblSendDateText);
	vpg3.addComponent(btnDelete);

	// 保存垂直设置
	layout.setVerticalGroup(layout.createSequentialGroup().addGroup(vpg1)
		.addGroup(vpg2).addGroup(vpg3));

	wrapperPanel.add(topPanel, BorderLayout.NORTH);

	contentEditorPane = new JEditorPane();
	contentEditorPane.setDoubleBuffered(true);
	contentEditorPane.setContentType("text/html");
	contentEditorPane.setEditable(false);
	JScrollPane scrollPane = new JScrollPane(contentEditorPane);
	wrapperPanel.add(scrollPane, BorderLayout.CENTER);

	add(wrapperPanel, BorderLayout.CENTER);
    }

    private void initComponmentListener() {
	// 添加到联系人按钮
	btnAddToLinkMan.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddToLinkMan) {
		    TLinkMan tLinkMan = new TLinkMan();
		    tLinkMan.setAccountId(1);
		    // 如果fromName为空，则将name设置为fromEmail
		    if (null == fromName || "".equals(fromName)) {
			tLinkMan.setName(fromEmail);
		    } else {
			tLinkMan.setName(fromName);
		    }
		    tLinkMan.setEmailAddress(fromEmail);
		    tLinkMan.setPhoneNumber("");
		    tLinkMan.setComment("");

		    // 添加到内存
		    ArrayList<TLinkMan> linkManList = (ArrayList<TLinkMan>) GlobalDataManager
			    .getData("linkMan");
		    linkManList.add(tLinkMan);

		    // 添加到数据库
		    TLinkManDao.addNewLinkMan(tLinkMan);

		    JOptionPane.showMessageDialog(null, "添加到联系人成功");
		}
	    }
	});
	// 回复邮件按钮
	btnReply.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnReply) {
		    SendEmailPanel sendPanel = new SendEmailPanel("回复 : "
			    + subject);
		    sendPanel.setReceiver(fromEmail);
		}
	    }
	});
	// 删除邮件按钮
	btnDelete.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnDelete) {
		    int operate = JOptionPane.showConfirmDialog(null,
			    "删除邮件后不可恢复，确定删除？", "操作确认",
			    JOptionPane.OK_CANCEL_OPTION);

		    if (operate == JOptionPane.OK_OPTION) {

			// 如果已经连上网络，则将删除更新到服务端
			if (message != null) {
			    System.out.println("更新到服务端");
			    try {
				message.setFlag(Flag.DELETED, true);
			    } catch (MessagingException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,
					"程序出错，删除失败！");
				return;
			    }
			}
			JOptionPane.showMessageDialog(null, "删除成功");
		    }
		    // 正文显示下一封邮件,true表示会同时删除当前邮件
		    System.out.println("删除本地");
		    mainPanel.setEmailContentToNext(true);
		}
	    }
	});
    }

    public void setNormal(boolean status) {
	wrapperPanel.setVisible(status);
    }

    /**
     * 设置发件人邮箱
     * 
     * @param from
     *            发件人邮箱
     */
    public void setFromText(String fromName, String fromEmail) {
	if (null != fromName && null != fromEmail) {
	    this.fromName = fromName;
	    this.fromEmail = fromEmail;
	    lblFromText.setText(fromName + "<" + fromEmail + ">");
	}
    }

    /**
     * 设置主题内容
     * 
     * @param subject
     *            主题内容
     */
    public void setSubjectText(String subject) {
	if (null != subject) {
	    this.subject = subject;
	    lblSubjectText.setText(subject);
	}
    }

    /**
     * 设置邮件正文
     * 
     * @param content
     *            邮件正文
     */
    public void setEmailContent(String content) {
	if (null != content) {
	    contentEditorPane.setText(content);
	}
    }

    /**
     * 设置发件日期
     * 
     * @param sendDate
     *            发件日期
     */
    public void setSendDate(String sendDate) {
	if (null != sendDate) {
	    lblSendDateText.setText(sendDate);
	}
    }

    public Message getMessage() {
	return message;
    }

    public void setMessage(Message message) {
	this.message = message;
    }

}
