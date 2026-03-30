package com.Repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Entity.Hackathon;
import com.Entity.Submission;
import com.Entity.User;

import java.util.List;


@Repository
public interface Submissionrepo extends JpaRepository<Submission, UUID> {

//	List<Submission> findByHackathonOrganizationOrganizationId(UUID organizationId);
	List<Submission> findByHackathonOrganizerOrganizerId(UUID organizerId);
	List<Submission> findByHackathon(Hackathon hackathon);
	List<Submission> findByTeam_TeamLeader(User user);
	List<Submission> findByHackathonIn(List<Hackathon> hackathons);

	boolean existsByTeam_TeamLeaderAndHackathon_HackathonId(User user, UUID hackathonId);

}
