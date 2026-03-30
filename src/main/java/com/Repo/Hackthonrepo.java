package com.Repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.Hackathon;
import com.Entity.OrganizerProfile;

import java.util.List;


public interface Hackthonrepo extends JpaRepository<Hackathon, UUID> {

	List<Hackathon> findByOrganizer(OrganizerProfile organizer);
	
}
