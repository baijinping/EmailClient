package email.client.util;

/**
 * 路径管理工具类
 * 
 * @author baikkp
 * 
 * 2014-2-15 下午8:15:28
 *
 */
public final class PathManager {

    /**
     *  得到sqlite数据库文件的路径（返回的路径前后均包含斜杠，不包含数据库文件名）
     */
    public static String getResourcePath() {
	String clsPath = PathManager.class.getResource("").getPath();
	clsPath = clsPath.replaceAll("bin/email/client/util", "res");
	return clsPath;
    }
    
    /**
     * 得到图片文件的路径
     * @return
     */
    public static String getImageFilePath() {
	return getResourcePath() + "/images";
    }
}
