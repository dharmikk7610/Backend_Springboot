package com.Repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.OrganizerProfile;
import com.Entity.User;

import java.util.List;
import java.util.Optional;


public interface Organizationrepo extends JpaRepository<OrganizerProfile, UUID> {

Optional<OrganizerProfile> findByOfficialEmail(String officialEmail);
Optional<OrganizerProfile> findByUser_UserId(UUID userId);
Optional<OrganizerProfile> findByUser(User user);

//	List<OrganizerProfile> findByOfficialEmail(String officialEmail);

}