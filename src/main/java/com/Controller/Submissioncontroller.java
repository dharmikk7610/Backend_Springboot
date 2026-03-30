package com.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Entity.Hackathon;
import com.Entity.Submission;
import com.Entity.Team;
import com.Entity.User;
import com.Repo.Hackthonrepo;
import com.Repo.Submissionrepo;
import com.Repo.Teamrepo;
import com.Repo.UserRepository;

@RestController
@RequestMapping("/team")
public class Submissioncontroller {

	@Autowired
	Submissionrepo srepo;
	
	@Autowired
	UserRepository userrepo ;

	@Autowired
	Teamrepo teamrepo;

	@Autowired
	Hackthonrepo hackrepo;

	@PostMapping("/addsubmission")
	public ResponseEntity<?> AddSubmission(@RequestBody Submission s  , @RequestParam UUID teamid ,@RequestParam UUID hackid )
	{
		// USING THE TOKEN CHECK  THE ONLY LEADER ADD THIS DETAILS ONLY ONE TEAM 
		Optional<Team> op  = teamrepo.findById(teamid);
		Optional<Hackathon> op2 = hackrepo.findById(hackid);
		
		if(op.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("team id not found");
		}
		if(op2.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("hack id not found");
		}
		
		s.setHackathon(op2.get());
		s.setTeam(op.get());
		
		Submission s2 = srepo.save(s);
		return ResponseEntity.ok(s2) ;
	}

//	@GetMapping("/Showallsubmission")
//	public ResponseEntity<?> ShowallSub() {
//		List<Submission> list = srepo.findAll();
//		return ResponseEntity.ok(list);
//	}

	@GetMapping("/findsubmission")
	public ResponseEntity<?> findsubmission(@RequestParam UUID id) {
		Optional<Submission> s = srepo.findById(id);
		if (s.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID not found");
		}

		return ResponseEntity.ok(s.get());

	}
	
	
	
	

}
