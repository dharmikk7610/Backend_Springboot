package com.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.AnyDiscriminatorImplicitValues.Strategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "hackathons")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hackathon {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
//@Column(columnDefinition = "BINARY(16)")
UUID hackathonId;


//@OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL)
//@JsonIgnoreProperties("hackathon")
//@ToString.Exclude
//@EqualsAndHashCode.Exclude
//private List<Judge> judges;

@ManyToMany(mappedBy = "hackathons",cascade = CascadeType.ALL)
@ToString.Exclude
@EqualsAndHashCode.Exclude
private List<Judge> judges;


@ManyToOne
@JoinColumn(name = "organizer_id")
@ToString.Exclude
@EqualsAndHashCode.Exclude
private OrganizerProfile organizer;


private String title;
private String shortDescription;
private String description;
private String format;
private String location;
private LocalDateTime startDate;
private LocalDateTime endDate;
private LocalDateTime registrationDeadline;
private Integer minTeamSize;
private Integer maxTeamSize;
private String problemStatement;
private String rules;
private String judgingCriteria;
private String prizePool;
private String eligibility;
private String coverImage;
private String status;
String[] tech ;


private LocalDateTime createdAt = LocalDateTime.now();
private LocalDateTime updatedAt = LocalDateTime.now();
}
