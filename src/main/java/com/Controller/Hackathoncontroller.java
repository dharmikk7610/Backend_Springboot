package com.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Entity.Hackathon;
import com.Entity.Hackthontech;
import com.Entity.OrganizerProfile;
import com.Entity.Submission;
import com.Entity.User;
import com.Repo.Hackathontech;
import com.Repo.Hackthonrepo;
import com.Repo.Organizationrepo;
import com.Repo.Submissionrepo;
import com.Repo.UserRepository;
import com.Util.Jwtutils;

@RestController
@RequestMapping("/hackathon")
public class Hackathoncontroller {

	@Autowired
	Hackthonrepo hrepo;

	@Autowired
	Organizationrepo op;
	
	@Autowired
	UserRepository urepo ;
	
	@Autowired
	Submissionrepo srepo ;

	@Autowired
	Hackathontech htechrepo;

	@Autowired
	Jwtutils jwtutil;

	@PostMapping("/addhackathon")
	public ResponseEntity<?> addHackathon(@RequestBody Hackathon h, @RequestHeader("Authorization") String authHeader) {

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
	    }

	    String token = authHeader.substring(7);
	    String email = jwtutil.extractUsername(token); // extract sub from JWT

	    Optional<User> up = urepo.findByEmail(email);
	    if (up.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: User not found");
	    }

	    Optional<OrganizerProfile> or = op.findByUser_UserId(up.get().getUserId());
	    if (or.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: Organizer profile not found");
	    }

	    h.setOrganizer(or.get());
	    hrepo.save(h);

	    return ResponseEntity.ok("Hackathon added successfully");
	}



	@PostMapping("/addhacktech")
	public ResponseEntity<?> addtech(@RequestBody Hackthontech htech, @RequestParam UUID id) {
		Optional<Hackathon> op = hrepo.findById(id);

		if (op.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("hackathon id not found");
		}
		htech.setHackathon(op.get());
		htechrepo.save(htech);

		return ResponseEntity.ok(htech);
	}

	
	
	@GetMapping("/showmyhackathon")
	public ResponseEntity<?> showMyHackathon(
	        @RequestHeader("Authorization") String authHeader) {

	   
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body("Missing or invalid Authorization header");
	    }

	    String token = authHeader.substring(7);
	    String email = jwtutil.extractUsername(token);

	   
	    Optional<User> userOpt = urepo.findByEmail(email);
	    if (userOpt.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body("User not found");
	    }

	    
	    Optional<OrganizerProfile> organizerOpt =
	            op.findByUser_UserId(userOpt.get().getUserId());

	    if (organizerOpt.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body("Organizer profile not found");
	    }

	    // 4. Get all hackathons
	    List<Hackathon> hackathons =
	            hrepo.findByOrganizer(organizerOpt.get());

	    return ResponseEntity.ok(hackathons);
	}



	@GetMapping("/checkhackathonlist")
	public ResponseEntity<?> showmyhackthonlist(@RequestParam UUID id) {
		Optional<Hackathon> op = hrepo.findById(id);
		if (op.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("hackathon is not found");
		}

		return ResponseEntity.ok(op.get());
	}
	
	@GetMapping("findbyhacks")
	public ResponseEntity<?> findhacksubmission(@RequestParam UUID id)
	{
		Optional<Hackathon> op = hrepo.findById(id);
		
		List<Submission> list  = srepo.findByHackathon(op.get());
		
		return ResponseEntity.ok(list) ;
	}

}
