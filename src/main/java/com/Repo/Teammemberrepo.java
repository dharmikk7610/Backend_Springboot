package com.Repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.Team;
import com.Entity.Teammember;
import com.Entity.User;

public interface Teammemberrepo extends JpaRepository<Teammember, UUID> {

	
	boolean existsByUserAndTeam(User user, Team team);
	List<Teammember> findByUser_UserId(UUID userId);
}
