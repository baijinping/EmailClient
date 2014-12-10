package email.client.dao.po;

/**
 * 联系人表LinkMan的数据
 * @author baikkp
 * 
 * 2014-3-24 下午5:59:18
 *
 */
public class TLinkMan {
    private Integer id;
    private Integer accountId;
    private String name;
    private String emailAddress;
    private String phoneNumber;
    private String comment;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /**
     * 返回name
     */
    public String toString() {
	return name;
    }
}
