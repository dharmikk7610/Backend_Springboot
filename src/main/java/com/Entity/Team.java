package com.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "teams")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID team_id;



@ManyToOne
@JoinColumn(name = "hackthon_id")

private Hackathon hackathon;


@ManyToOne
@JoinColumn(name = "team_leaderid")
//@JsonManagedReference
private User teamLeader;



@OneToMany(mappedBy = "team",cascade = CascadeType.ALL)
@JsonIgnore
private List<Teammember> members = new ArrayList<>();



private String teamName;
private Integer teamSize;
private Integer totalteamsize ;
private String teamStatus;
String inviteToken ;
LocalDateTime inviteExpiry = LocalDateTime.now();
private LocalDateTime createdAt = LocalDateTime.now();
}
