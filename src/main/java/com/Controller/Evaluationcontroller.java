package com.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.DTO.EvaluationRequestDTO;
import com.Entity.Evaluation;
import com.Entity.Hackathon;
import com.Entity.Judge;
import com.Entity.Submission;
import com.Repo.Evaluationrepo;
import com.Repo.Submissionrepo;
import com.Repo.judgerepo;
import com.Util.Jwtutils;

@RestController
@RequestMapping("/judge")
public class Evaluationcontroller {
	
	@Autowired
	Submissionrepo submissionRepo ;
	
	@Autowired
	judgerepo judgeRepo;
	
	@Autowired
	Jwtutils jwtutil ;
	
	@Autowired
	Evaluationrepo evaluationRepo;
	
	@Autowired
	Submissionrepo srepo ;

	@PostMapping("/submit")
	public ResponseEntity<?> submitEvaluation(
	        @RequestParam UUID submissionId,
	        @RequestHeader("Authorization") String authHeader,
	        @RequestBody Evaluation data
	) {

	    try {
	        //  Extract token
	        String token = authHeader.startsWith("Bearer ")
	                ? authHeader.substring(7)
	                : authHeader;

	        String email = jwtutil.extractUsername(token);

	        //  Fetch submission
	        Submission submission = submissionRepo.findById(submissionId)
	                .orElseThrow(() -> new RuntimeException("Submission not found"));

	        //  Fetch judge
	        Judge judge = judgeRepo.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("Judge not found"));

	        //  Authorization check (IMPORTANT)
	        Hackathon submissionHackathon =
	                submission.getTeam().getHackathon();

	        if (!judge.getHackathons().contains(submissionHackathon)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                    .body("Judge not assigned to this hackathon");
	        }

	        //  Prevent duplicate evaluation (VERY IMPORTANT)
//	        if (evaluationRepo.existsByJudgeAndSubmission(judge, submission)) {
//	            return ResponseEntity.status(HttpStatus.CONFLICT)
//	                    .body("You have already evaluated this submission");
//	        }

	        
	        submission.setSubmissionStatus("evaluate");

	      
	        data.setSubmission(submission);
	        data.setJudge(judge);
	        data.setEvaluatedAt(LocalDateTime.now());

	        int avgScore = (
	                data.getInnovationScore()
	              + data.getTechnicalScore()
	              + data.getPresentationScore()
	        ) / 3;

	        data.setTotalScore(avgScore);

	        evaluationRepo.save(data);
	        submissionRepo.save(submission);

	        return ResponseEntity.ok("Evaluation submitted successfully");

	    } catch (Exception e) {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body("Invalid token or request");
	    }
	}

	 
	 
	@GetMapping("/judgeevaluation")
	public ResponseEntity<?> getAllEvaluationsByJudge(@RequestHeader("Authorization") String authHeader) {

	    String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
	    String email = jwtutil.extractUsername(token);

	    Judge judge = judgeRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Judge not found"));
	    System.out.println("judge-->"+judge);

	    List<Evaluation> evaluations = evaluationRepo.findByJudgeJudgeId(judge.getJudgeId());
	    System.out.println("evaluations-->"+evaluations);

	    return ResponseEntity.ok(evaluations);
	}


	 
	 @GetMapping("/submission/{submissionId}")
	    public ResponseEntity<?> getAllEvaluationsForSubmission(
	            @PathVariable UUID submissionId) {

	        Submission submission = submissionRepo.findById(submissionId)
	                .orElseThrow(() -> new RuntimeException("Submission not found"));

	        List<Evaluation> evaluations = evaluationRepo
	                .findBySubmission(submission)
	                .stream()
	                .collect(Collectors.toList());

	        return ResponseEntity.ok(evaluations);
	    }
	
}
