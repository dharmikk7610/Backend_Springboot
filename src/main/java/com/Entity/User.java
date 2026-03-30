package com.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID userId;


String name;


@Column(unique = true)
private String username;


@Column(unique = true)
private String email;

private String phoneNumber;
private String passwordHash;
private String role;
String location ;
private String profileImage;
private String collegeName;
private String department;
private String studyYear;
private String bio;
private String linkedin;
String skills ;
private String github;
private String status;

String otp ;



private LocalDateTime createdAt = LocalDateTime.now();
private LocalDateTime updatedAt = LocalDateTime.now();
private LocalDateTime lastLogin;
private LocalDateTime expireotp;


//@OneToMany(mappedBy = "teamLeader")
//@JsonBackReference
//private List<Team> teams;



@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
@JsonIgnore
private List<Teammember> members = new ArrayList<>();



}
