package com.github.elgleidson.multi.tenant.database.domain.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Tenant {
	
	@Id
	private Long id;
	
	@NotBlank
	@Column
	private String name;

	@NotBlank
	@Column
	private String url;
	
	@NotBlank
	@Column
	private String username;
	
	@NotBlank
	@Column
	private String password;
	
	public Tenant() {
		
	}
	
	public Tenant(String name, String url, String username, String password) {
		this.name = name;
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Tenant [id=" + id + ", name=" + name + ", url=" + url + ", username=" + username + "]";
	}
	
}
