package email.client.dao.po;

/**
 * 用来保存邮件数据，作为emailJList的元素值
 * @author baikkp
 *
 * 2014-2-28 下午3:54:55
 */
public class TEmail {

    private Integer id;
    private Integer accountId;
    private Integer status = 0;
    private Integer type;
    private String uuid;
    private String subject;
    private String content;
    private String sendDate;
    private String fromEmail;
    private String fromName;
    
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
     * @param status 邮件状态，如是否已读等
     * @param fromEmail 发件人邮箱
     */
    public TEmail(Integer id, Integer accountId, Integer status, Integer type, String subject, String content, String sendDate, String uuid, String fromEmail) {
        this.id = id;
        this.accountId = accountId;
        this.uuid = uuid;
        this.type = type;
        this.subject = subject;
        this.content = content;
        this.sendDate = sendDate;
        this.status = status;
        this.fromEmail = fromEmail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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
    
    public String getFromEmail() {
	return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
	this.fromEmail = fromEmail;
    }
    
    public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
     * 返回subject
     */
    public String toString() {
    	return subject;
    }

}
