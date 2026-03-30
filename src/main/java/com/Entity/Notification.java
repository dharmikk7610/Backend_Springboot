package com.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID notificationId;


@ManyToOne
@JoinColumn(name = "user_id")
private User user;


private String message;
private Boolean isRead = false;
private LocalDateTime createdAt = LocalDateTime.now();

}
