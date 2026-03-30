package com.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Entity.Hackathon;
import com.Entity.Judge;
import com.Entity.Submission;
import com.Repo.Submissionrepo;
import com.Repo.UserRepository;
import com.Repo.judgerepo;
import com.Util.Jwtutils;

@RequestMapping("/judge")
@RestController
public class Judgecontroller {

	@Autowired
	judgerepo jrepo;
	
	@Autowired
	UserRepository urepo ;

	@Autowired
	Submissionrepo submissionrepo;

	@Autowired
	Jwtutils jwtutil;

	@GetMapping("/judge/hackathon")
	public ResponseEntity<?> getHackathonByJudgeId(@RequestHeader("Authorization") String auth) {

		String token = auth.startsWith("Bearer ") ? auth.substring(7) : auth;
		String mail = jwtutil.extractUsername(token);

		Judge judge = jrepo.findByEmail(mail).orElseThrow(() -> new RuntimeException("Judge not found"));

		List<Hackathon> list = judge.getHackathons();

		return ResponseEntity.ok(list);
	}

	@GetMapping("/judgesubmissions")
	public ResponseEntity<?> getAllSubmissionsForJudge(
	        @RequestHeader("Authorization") String authHeader
	) {
	    try {
	        //  Extract token
	        String token = authHeader.startsWith("Bearer ")
	                ? authHeader.substring(7)
	                : authHeader;

	        //  Extract email from JWT
	        String email = jwtutil.extractUsername(token);

	        //  Find judge
	        Judge judge = jrepo.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("Judge not found"));

	        //  Get assigned hackathons
	        List<Hackathon> hackathons = judge.getHackathons();

	        if (hackathons == null || hackathons.isEmpty()) {
	            return ResponseEntity.ok(List.of());
	        }

	        //  Get submissions for all hackathons
	        List<Submission> submissions =
	                submissionrepo.findByHackathonIn(hackathons);

	        return ResponseEntity.ok(submissions);

	    } catch (Exception e) {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body("Invalid token or request");
	    }
	}

	
//	@GetMapping("/judgeevaluation")
//	public ResponseEntity<?> getAllevaluationForJudge(@RequestHeader("Authorization") String authHeader) {
//	    try {
//	       
//	        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
//
//	        
//	        String email = jwtutil.extractUsername(token);
//
//	        // Find Judge by email
//	        Judge judge = jrepo.findByEmail(email)
//	                .orElseThrow(() -> new RuntimeException("Judge not found"));
//
//	        // Get assigned Hackathon
//	        Hackathon hack = judge.getHackathon();
//
//	        // Get all submissions of teams in that hackathon
//	        List<Submission> submissions = submissionrepo.findByHackathon(hack);
//
//	        return ResponseEntity.ok(submissions);
//
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or request");
//	    }
//	}


}
