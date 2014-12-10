package email.client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class FrameTemplate extends JFrame {
    private PanelTemplate panel;

    public FrameTemplate() {
	panel = new PanelTemplate();
	add(panel);

	normalSetting();

	setVisible(true);
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
}
