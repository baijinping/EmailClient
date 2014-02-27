package email.client.gui.main;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

class EmailListModel extends AbstractListModel<Object> {

    private String[] elements;
    
    public EmailListModel(String[] elements) {
	super();
	this.elements = elements; 
    }
    
    @Override
    public int getSize() {
	return elements.length;
    }

    @Override
    public Object getElementAt(int index) {
	if(index >= 0 && index < elements.length) { 
	    return elements[index];
	}
	return null;
    }
}

public class ListDemo extends JFrame{
    
    private JList emailList;
    
    public ListDemo() {
	String[] strData = {"你好", "还行", "还行", "还行", "还行", "还行", "还行", "还行", "还行", "还行", "还行", "还行", "还行"};
	emailList = new JList(new EmailListModel(strData));
	add(emailList);
	
	JScrollPane scrollPane = new JScrollPane(emailList);
	add(scrollPane);
	
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	setVisible(true);
    }
    
    public static void main(String[] args) {
	new ListDemo();
    }
}