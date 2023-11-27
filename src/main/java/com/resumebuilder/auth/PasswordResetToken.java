package com.resumebuilder.auth;

import java.time.LocalDateTime;

import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class PasswordResetToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    
	@Column
    private LocalDateTime expiryDate;
	
	@Column(nullable = false)
    private boolean used;

	
	 public PasswordResetToken( String token, User user,LocalDateTime expiryDate) {
			super();
			this.token = token;
			this.expiryDate = expiryDate;
			this.user = user;
		}


	    
	public PasswordResetToken() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}



	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}



	public boolean isUsed() {
		return used;
	}



	public void setUsed(boolean used) {
		this.used = used;
	}
	

}
