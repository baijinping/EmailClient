package email.client.gui.main;

import java.awt.BorderLayout;
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

public class MainUI extends JFrame {

	private MainPanel mainPanel;
	
	public MainUI(String uiName) {
		super(uiName);
	
		setLayout(new BorderLayout());
		
		normalSetting();
		initComponment();
		
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
		// Look & Feel 观感主题设置
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
		setBounds((int)(screenSize.width * 0.125), 0,
				(int) (screenSize.width * 0.75), (int)(screenSize.height * 0.8));

	}

	public void initComponment() {
		mainPanel = new MainPanel();
		add(mainPanel, BorderLayout.CENTER);
	}
	
}
