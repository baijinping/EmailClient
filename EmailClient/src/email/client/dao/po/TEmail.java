package email.client.dao.po;

public class TEmail {

    private int id;
    private int accountId;
    private int status = 0;
    private int type;
    private String uuid;
    private String subject;
    private String content;
    private String sendDate;
    
    public TEmail() {
    }
    
    /**
     * 
     * @param id
     * @param accountId
     * @param uuid 唯一标识符，用来标识一封唯一的邮件防止存入时重复，其值=(subject + sendDate).hashCode()，计算算法同String.hashCode()
     * @param type
     * @param subject
     * @param content
     * @param sendDate 字符串格式 = "yyyy-MM-dd HH:mm:ss" 
     */
    public TEmail(int id, int accountId, int status, int type, String subject, String content, String sendDate, String uuid) {
        this.id = id;
        this.accountId = accountId;
        this.uuid = uuid;
        this.type = type;
        this.subject = subject;
        this.content = content;
        this.sendDate = sendDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
    
}
