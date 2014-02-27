package email.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public interface PanelConst {
	// 登录界面长宽

	// 主界面长宽 -> 实际上在normalSetting中已经将主界面大小，设为屏幕大小的长宽各1/4
	// 写邮件界面长宽

	// 常用字体
	public static final Font LOGIN_WARN_FONT = new Font("黑体", Font.BOLD, 20);

	// 常用颜色
	public static final Color LOGIN_BG_COLOR = new Color(135, 240, 100);

	// 常用字符串
	public static final String LOGIN_FAIL_MSG = "验证失败：邮箱地址或密码错误";
	public static final String LOGIN_QQMAIN_FAIL_MSG = "验证失败：邮箱地址或密码错误\n如果设置了独立密码，请输入独立密码";

	// 邮件正则验证
	public static final String REGEX_EMAIL_ADDRESS = "^[a-zA-Z0-9_]([\\.]?[a-zA-Z0-9_])*@([a-zA-Z0-9])+\\.(com|cn|net)$";
}
