package com.Entity;

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
@Table(name = "hackathon_tech_stack")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hackthontech {


@Id
@GeneratedValue(strategy = GenerationType.UUID)

//@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//@Column(columnDefinition = "CHAR(36)")
private UUID techId;


@ManyToOne
@JoinColumn(name = "hackathon_id")
private Hackathon hackathon;


private String technologyName;
}
