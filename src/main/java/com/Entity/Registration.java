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
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "registrations")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Registration {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID registrationId;


@ManyToOne
@JoinColumn(name = "hackathon_id")
private Hackathon hackathon;


@ManyToOne
@JoinColumn(name = "team_id")
private Team team;


@ManyToOne
@JoinColumn(name = "applied_by")
private User appliedBy;


private String registrationStatus;
private String remarks;
private LocalDateTime appliedAt = LocalDateTime.now();
private LocalDateTime reviewedAt;
}