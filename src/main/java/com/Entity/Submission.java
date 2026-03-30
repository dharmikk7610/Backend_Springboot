package com.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
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
@Table(name = "submissions")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Submission {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID submissionId;


@ManyToOne
@JoinColumn(name = "hackathon_id")
private Hackathon hackathon;


@ManyToOne
@JoinColumn(name = "team_id")
private Team team;


private String projectTitle;
private String description;
private String githubRepo;
private String demoVideoUrl;
private String presentationUrl;
String techStack ;
String liveUrl ;
private String submissionStatus = "PENDING";
private LocalDateTime submissionTime = LocalDateTime.now();
}
