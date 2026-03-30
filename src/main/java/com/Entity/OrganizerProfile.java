package com.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "organizer_profile")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrganizerProfile {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID organizerId;


@OneToOne
@JoinColumn(name = "user_id")
private User user;


private String organizationName;
private String organizationType;
private String officialEmail;
String password ; 
private String website;
private String description;
private String verificationStatus;
private LocalDateTime createdAt = LocalDateTime.now();
}