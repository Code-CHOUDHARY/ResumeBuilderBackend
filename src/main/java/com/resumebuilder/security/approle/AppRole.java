//package com.resumebuilder.security.approle;
//
//
//
//import java.util.HashSet;
//import java.util.Set;
//
//import com.resumebuilder.user.User;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.ManyToMany;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name = "app_roles")
//public class AppRole {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
//	
//	@Enumerated(EnumType.STRING)
//	@Column(length = 20)
//	private ERole name;
//	
//	@ManyToMany(mappedBy = "appRoles")
//	private Set<User> users = new HashSet<>();
//	
//  public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public ERole getName() {
//		return name;
//	}
//
//	public void setName(ERole name) {
//		this.name = name;
//	}
//
//
//
//
//}
