package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Entity.Hackathon;
import com.Entity.Submission;
import com.Entity.User;
import com.Repo.Evaluationrepo;
import com.Repo.Hackthonrepo;
import com.Repo.Submissionrepo;
import com.Repo.UserRepository;
import com.Repo.judgerepo;

@RestController
@RequestMapping("/admin")
public class Admincontroller {

	@Autowired
	UserRepository userrepo ;
	
	@Autowired
	Hackthonrepo hrepo ;
	
	@Autowired
	Submissionrepo srepo ;
	
	@Autowired
	Evaluationrepo erepo;
	
	@Autowired
	judgerepo jrepo ;
	
	
	@GetMapping("/alluser")
	public ResponseEntity<?> Alluser()
	{
		List<User> list = userrepo.findAll();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/allparticipant")
	public ResponseEntity<?> Allparticipant()
	{
		List<User> list = userrepo.findByRole("participant");
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/allorganizer")
	public ResponseEntity<?> Allorganizer()
	{
		List<User> list = userrepo.findByRole("organizer");
		return ResponseEntity.ok(list);
	}
	@GetMapping("/alljudge")
	public ResponseEntity<?> Alljudge()
	{
		List<User> list = userrepo.findByRole("judge");
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/allsubmission")
	public  ResponseEntity<?> allsubmissons()
	{
		 List<Submission> list = srepo.findAll();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/allhackathon")
	public ResponseEntity<?> allHackathons()
	{
		List<Hackathon> list = hrepo.findAll();
		
		return ResponseEntity.ok(list);
	}
	
	
	
	

}
