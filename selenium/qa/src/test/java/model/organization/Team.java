package model.organization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import model.User;

public class Team implements Serializable{

	private static final long serialVersionUID = 5492225800868949746L;
	
	@JsonProperty("org_admin_user")
	private String orgAdminKey;
	
	@JsonProperty("box_office_users")
	private List<String> boxOfficeUserKeys;
	
	private User orgAdminUser;
	
	private List<User> boxOfficeUsers;

	public String getOrgAdminKey() {
		return orgAdminKey;
	}

	public void setOrgAdminKey(String orgAdminKey) {
		this.orgAdminKey = orgAdminKey;
	}

	public List<String> getBoxOfficeUserKeys() {
		return boxOfficeUserKeys;
	}

	public void setBoxOfficeUserKeys(List<String> boxOfficeUserKeys) {
		this.boxOfficeUserKeys = boxOfficeUserKeys;
	}

	public User getOrgAdminUser() {
		if (this.orgAdminUser == null && orgAdminKey != null && !orgAdminKey.isEmpty()) {
			this.orgAdminUser = User.generateUserFromJson(orgAdminKey);
		}
		return this.orgAdminUser;
	}

	public void setOrgAdminUser(User orgAdminUser) {
		this.orgAdminUser = orgAdminUser;
	}

	public List<User> getBoxOfficeUsers() {
		if (boxOfficeUsers == null || boxOfficeUsers.isEmpty()) {
			if (boxOfficeUserKeys != null && !boxOfficeUserKeys.isEmpty()) {
				boxOfficeUsers = new ArrayList<User>();
				for(String key : boxOfficeUserKeys) {
					User boxOfficeUser = User.generateUserFromJson(key);
					boxOfficeUsers.add(boxOfficeUser);
				}
			}
		}
		return boxOfficeUsers;
	}

	public void setBoxOfficeUsers(List<User> boxOfficeUsers) {
		this.boxOfficeUsers = boxOfficeUsers;
	}
}
