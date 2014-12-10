package email.client.gui.main;

import javax.swing.text.*;

/**
 * 控制JTextField的最大输入字符长度
 * 
 * 使用示例：
 * 	textField = new JTextField(15);
 * 	// 设置最大输入长度为10
 * 	textField.setDocument( new JTextFieldLimit(10));
 * 
 * @author baikkp
 * 
 * 2014-4-6 下午8:04:06
 *
 */
public class JTextFieldLimit extends PlainDocument {

    private int limit;

    public JTextFieldLimit(int limit) {
	super();
	this.limit = limit;
    }
    
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
	if(str == null) return;
	
	if((getLength() + str.length()) <= limit) {
	    super.insertString(offset, str, attr);
	}
    }
}
