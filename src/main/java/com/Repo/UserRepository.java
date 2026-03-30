package com.Repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, UUID> {

//	List<User> findByFullName(String fullName);
	List<User> findByRole(String role);
	
	Optional<User> findByEmail(String email);
	
}
