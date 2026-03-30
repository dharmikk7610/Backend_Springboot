package com.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Entity.Evaluation;
import com.Entity.Hackathon;
import com.Entity.Judge;
import com.Entity.OrganizerProfile;
import com.Entity.Submission;
import com.Entity.User;
import com.Repo.Evaluationrepo;
import com.Repo.Hackthonrepo;
import com.Repo.Organizationrepo;
import com.Repo.Submissionrepo;
import com.Repo.UserRepository;
import com.Repo.judgerepo;
import com.Service.Passwordencoder;
import com.Service.Usermailservice;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/hackathon")
@Transactional
public class Organization1 {

	@Autowired
	Organizationrepo oprepo ;
	
	@Autowired
	UserRepository userrepo ;
	
	@Autowired
	Submissionrepo srepo ;
	
	@Autowired
	Hackthonrepo hrepo ;
	
	@Autowired
	judgerepo jrepo ;
	
	@Autowired
	  Passwordencoder passwordencoders ;
	
	@Autowired 
	Usermailservice mailservice ;
	
	@Autowired
	UserRepository urepo ;
	
	@Autowired
	Evaluationrepo erepo ;
	
	
	
//	@PostMapping("/createorganization")
//	public ResponseEntity<String> createOrganizationProfile(
//	        @RequestBody OrganizerProfile op,
//	        @RequestHeader(value = "Authorization", required = false) String authHeader
//	) {
//		
//	    oprepo.save(op);
//	    return ResponseEntity.status(HttpStatus.CREATED)
//	            .body("Organization profile added successfully");
//	}
	


	
	@GetMapping("/showorganization")
	public ResponseEntity<?> showall()
	{
		List<OrganizerProfile> list = oprepo.findAll();
		if(list.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("notfound profile");
		}
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	//-------------SUBMISSION -----------------------//
	
	@GetMapping("/Showallsubmission")
	public ResponseEntity<?> ShowallSub(@RequestParam UUID organizationid) {
		
		List<Submission> list = srepo.findByHackathonOrganizerOrganizerId(organizationid);
		
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/addjudge")
	public ResponseEntity<?> addJudge(
	        @RequestParam String hackathonId,
	        @RequestBody Judge judgeRequest
	) {
	    UUID hackId = UUID.fromString(hackathonId);
	    Hackathon hackathon = hrepo.findById(hackId)
	            .orElseThrow(() -> new RuntimeException("Hackathon not found"));

	    // Check if judge already exists by email
	    Optional<Judge> existingJudgeOpt = jrepo.findByEmail(judgeRequest.getEmail());
	    User u = new User();
	    Judge judge;
	    if (existingJudgeOpt.isPresent()) {
	        // Judge exists: add hackathon if not already assigned
	        judge = existingJudgeOpt.get();
	        if (!judge.getHackathons().contains(hackathon)) {
	            judge.getHackathons().add(hackathon);
	            hackathon.getJudges().add(judge); // keep both sides in sync
	            jrepo.save(judge);
	        }
	        // Do not send password email again
	    } else {
	    
	    	
	        // New judge: generate password, set role, assignedAt, assign hackathon
	        int pass = (int) (Math.random() * 90000000) + 10000000;
	        String passStr = String.valueOf(pass);

	        u.setEmail(judgeRequest.getEmail());
	        u.setPasswordHash(passwordencoders.getencoder().encode(passStr));
	        u.setRole("judge");
	        urepo.save(u);
	        
	        judge = new Judge();
	        judge.setName(judgeRequest.getName());
	        judge.setEmail(judgeRequest.getEmail());
	        judge.setPassword(passwordencoders.getencoder().encode(passStr));
	        judge.setRole("judge");
	        judge.setAssignedAt(LocalDateTime.now());
	        judge.setHackathons(new ArrayList<>(List.of(hackathon)));

	        
	        // keep both sides in sync
	        hackathon.getJudges().add(judge);

	        // send email
	        mailservice.judgeinvitemail(
	                judge.getName(),
	                passStr,
	                judge.getEmail()
	        );

	        jrepo.save(judge);
	    }

	    return ResponseEntity.status(HttpStatus.CREATED).body(judge);
	}



	@GetMapping("/Showorgenizerjudge")
	public ResponseEntity<?> Showmyjudge(@RequestParam String id)
	{
		UUID id2 = UUID.fromString(id);
		Optional<User> u   = urepo.findById(id2);
		Optional<OrganizerProfile> op = oprepo.findByUser(u.get());
		 List<Judge> list  = jrepo.findByHackathonsOrganizerOrganizerId(op.get().getOrganizerId());
		 return ResponseEntity.ok(list);
	}
	
	
//	@PostMapping("/addorganizer")
//	public ResponseEntity<?> addOrganizer(
//	        @RequestParam UUID hackathonId,
//	        @RequestBody Organizer organizer
//	) {
//
//	    Optional<Hackathon> optionalHackathon = hrepo.findById(hackathonId);
//
//	    if (optionalHackathon.isEmpty()) {
//	        return ResponseEntity
//	                .status(HttpStatus.NOT_FOUND)
//	                .body("Hackathon id not found");
//	    }
//
//	    // Prevent duplicate organizer by email
//	    if (userrepo.findByEmail(organizer.getEmail()).isPresent()) {
//	        return ResponseEntity
//	                .status(HttpStatus.CONFLICT)
//	                .body("Organizer already exists");
//	    }
//
//	    // 1️⃣ Generate random password
//	    int pass = (int) (Math.random() * 90000000) + 10000000;
//	    String rawPassword = String.valueOf(pass);
//	    String encodedPassword = passwordencoders
//	            .getencoder()
//	            .encode(rawPassword);
//
//	    // 2️⃣ Save Organizer
//	    organizer.setPassword(encodedPassword);
//	    organizer.setHackathon(optionalHackathon.get());
//	    organizer.setAssignedAt(LocalDateTime.now());
//	    organizer.setRole("ORGANIZER");
//
//	    Organizer savedOrganizer = orepo.save(organizer);
//
//	    // 3️⃣ Save User for login
//	    User user = new User();
//	    user.setEmail(organizer.getEmail());
//	    user.setPasswordHash(encodedPassword);
//	    user.setRole("ORGANIZER");
//	    user.setName(organizer.getName());
//	    userrepo.save(user);
//
//	    // 4️⃣ Send email
//	    mailservice.organizerInviteMail(
//	            organizer.getName(),
//	            rawPassword,
//	            organizer.getEmail()
//	    );
//
//	    return ResponseEntity
//	            .status(HttpStatus.CREATED)
//	            .body(savedOrganizer);
//	}

	 @GetMapping("/evaluationhackathon")
	    public ResponseEntity<?> getEvaluationsByHackathon(@RequestParam UUID id) {
	        Optional<Hackathon> hackathon = hrepo.findById(id);
//	                .orElseThrow(() -> new RuntimeException("Hackathon not found"));

	          List<Evaluation> list =  erepo.findByJudge_Hackathons(hackathon.get());
	          return ResponseEntity.ok(list);
	    }


	

}
