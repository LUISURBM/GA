package siga.ga.dao;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class TwitterStatus implements Serializable {
	private String message;
	private long id;
	private Date createdDate;
	private String createdUser;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

}
