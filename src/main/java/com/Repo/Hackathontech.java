package com.Repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Entity.Hackthontech;

public interface Hackathontech extends JpaRepository<Hackthontech, UUID> {

	

}
