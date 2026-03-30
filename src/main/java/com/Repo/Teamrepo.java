package com.Repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.Team;
import com.Entity.User;

public interface Teamrepo extends JpaRepository<Team, UUID> {

Optional<Team> findByInviteToken(String inviteToken);

List<Team> findByTeamLeader_UserId(UUID userId);
List<Team> findByTeamLeader(User teamLeader);

}
