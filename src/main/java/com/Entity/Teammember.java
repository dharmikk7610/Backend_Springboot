package com.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "team_members")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teammember {


@Id
@GeneratedValue(strategy =GenerationType.UUID)
//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID teamMemberId;


  @ManyToOne(fetch = FetchType.EAGER)
  @JsonIgnore
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;


@ManyToOne
//@JsonIgnore
@JoinColumn(name = "user_id")
private User user;


private String roleInTeam;
private LocalDateTime joinedAt = LocalDateTime.now();
}
