package com.github.wangyi.commons.model;

import java.util.Date;


/**
 * 用户:存储认证/资源服务器的用户信息，即资源的拥有者
 * @author wangyi
   @date 2016-9-8
 *
 */
public class Oauth2User {
    
	private Long id;

    private String username;

    private String password;

    private String salt;
    
    private Date create_date;

    private Date update_date;

    private Boolean is_valid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Boolean getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(Boolean is_valid) {
		this.is_valid = is_valid;
	}
    
    
}