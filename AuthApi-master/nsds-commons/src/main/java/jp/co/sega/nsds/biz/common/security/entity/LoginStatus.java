/**
 * 
 */
package jp.co.sega.nsds.biz.common.security.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author NakanoY
 *
 */
public class LoginStatus implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	private int type;
	
	private String value;
	
	private int count;
	
    private Date createDate;

    private String createUserId;

    private Date updateDate;

    private String updateUserId;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

}
