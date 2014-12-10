package email.client.gui;

import javax.swing.JPanel;

/**
 * UI面板的模板（normal setting在frame下而非panel中）
 * @author baikkp
 * 
 * 2014-3-24 下午10:28:56
 *
 */
public class PanelTemplate extends JPanel {
    public PanelTemplate() {
	super();

	initComponment();
	initComponmentListener();
	initWindowListener();

	setVisible(true);
    }

    private void initComponment() {

    }

    private void initComponmentListener() {

    }

    private void initWindowListener() {

    }

}
