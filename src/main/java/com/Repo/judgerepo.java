package com.Repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.Hackathon;
import com.Entity.Judge;
import com.Entity.Submission;

import java.util.List;
import java.util.Optional;




public interface judgerepo extends JpaRepository<Judge, UUID>  {

	Optional<Judge> findByEmail(String email);
//	List<Judge> findByHackathonOrganizerOrganizerId(UUID organizerId);
//	  List<Judge> findByHackathonOrganizerOrganizerId(UUID organizerId);
//	 Optional<Judge> findByHackathonsOrganizerOrganizerId(UUID organizerId);
	 List<Judge> findByHackathonsOrganizerOrganizerId(UUID organizerId);
}
