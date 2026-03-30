package com.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "judges")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE )
public class Judge {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID judgeId;


//@ManyToOne(fetch = FetchType.EAGER)
//@JoinColumn(name = "hackathon_id")
//@JsonIgnoreProperties("judges") 
//@ToString.Exclude
//@EqualsAndHashCode.Exclude
//private Hackathon hackathon;
//
@ManyToMany
@JoinTable(
    name = "judge_hackathon",
    joinColumns = @JoinColumn(name = "judge_id"), 
    inverseJoinColumns = @JoinColumn(name = "hackathon_id")
		)

@JsonIgnoreProperties("judges")
@ToString.Exclude
private List<Hackathon> hackathons;

private String name;
private String email;
String role ;
String status ;
String password ;
private String expertise;
private LocalDateTime assignedAt = LocalDateTime.now();
}
