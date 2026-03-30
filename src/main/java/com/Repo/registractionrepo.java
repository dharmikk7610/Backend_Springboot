package com.Repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.Registration;

public interface registractionrepo extends JpaRepository<Registration, UUID> {

	List<Registration> findByAppliedBy_UserId(UUID userId);
}
