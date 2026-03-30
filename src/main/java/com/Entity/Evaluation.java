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
@Table(name = "evaluation")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Evaluation {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID evaluationId;


@ManyToOne
@JoinColumn(name = "submission_id")
private Submission submission;



@ManyToOne
@JoinColumn(name = "judge_id")
private Judge judge;


private Integer innovationScore;
private Integer technicalScore;
private Integer presentationScore;
private Integer totalScore;
private String overallComments;
private LocalDateTime evaluatedAt = LocalDateTime.now();
}