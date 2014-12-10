package email.client.gui.receive;

import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

import email.client.util.PathManager;

public class JEditPaneTest extends JFrame {
	public JEditPaneTest() throws Exception {
		super();
		setSize(200, 200);
		setVisible(true);
		
		JEditorPane pane = new JEditorPane();
		pane.setContentType("text/html");
		String path = "file://" + PathManager.getResourcePath() + "test.htm";
		System.out.println(path);
		pane.setPage(new URL(path));
		//pane.setPage("http://www.baidu.com");
		add(pane);
	}
	
	public static void main(String[] args) throws Exception {
		new JEditPaneTest();
	}
}
