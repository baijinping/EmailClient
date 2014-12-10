package email.client.gui.blacklist;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import email.client.util.PathManager;

public class BlackListUI extends JFrame {

    private BlackListPanel panel;

    public BlackListUI() {
	super();
	setTitle("暖冬 Email Client -> 黑名单管理");

	panel = new BlackListPanel();
	add(panel);

	normalSetting();

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

    private void normalSetting() {
	try {
	    // 设置Nimbus皮肤
	    UIManager
		    .setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

	} catch (ClassNotFoundException | InstantiationException
		| IllegalAccessException | UnsupportedLookAndFeelException e) {
	    // 表示运行环境非JRE7
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

    @Override
    @Deprecated
    public void show() {
	super.show();
	panel.updateBlackList();
	panel.showBlackListInfo(0, false);
    }

}
