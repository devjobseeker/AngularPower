package org.asu.chilll.power.entity.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Scope("session")
@Table(name = "app_user")
public class AppUser implements UserDetails{
	public AppUser() {
		
	}
	
	public AppUser(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Id
	private String uid;
	
	//@GeneratedValue(strategy= GenerationType.IDENTITY)
	//private Long id;
	@Column(unique = true)
	private String username;
	private String password;
	private String  role;
	private Boolean enabled;
	//private String approved;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
	
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

//	public String getApproved() {
//		return approved;
//	}
//
//	public void setApproved(String approved) {
//		this.approved = approved;
//	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public static enum Role{ USER, ADMIN }

	@Override
	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//@JsonIgnore
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
