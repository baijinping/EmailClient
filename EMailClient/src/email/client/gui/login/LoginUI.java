package email.client.gui.login;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import email.client.gui.PanelConst;

public class LoginUI extends JFrame {

    private LoginPanel panel;
    
    public LoginUI() {
	panel = new LoginPanel();
	add(panel);
	
	normalSetting();
	
	setTitle("暖冬 电子邮件客户端");
	
	setVisible(true);
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
    
    public void setWarningMsg(String msg) {
	panel.setWarningMsg(msg);
    }
    
    public static void main(String[] args) {
	new LoginUI();
    }
}
