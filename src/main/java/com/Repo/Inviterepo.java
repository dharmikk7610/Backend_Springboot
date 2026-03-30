package com.Repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.Team;
import com.Entity.TeamInvite;
import java.util.List;
import java.util.Optional;


public interface Inviterepo extends JpaRepository<TeamInvite, UUID> {

	List<TeamInvite> findByTeam(Team team);
	Optional<TeamInvite> findByEmail(String email);
	TeamInvite  findByInviteToken(String inviteToken);

}
