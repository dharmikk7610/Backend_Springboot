package com.Repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Entity.Evaluation;
import com.Entity.Hackathon;
import com.Entity.Judge;
import com.Entity.Submission;
import com.Entity.User;

@Repository
public interface Evaluationrepo extends JpaRepository<Evaluation, UUID> {

	 Optional<Evaluation> findBySubmissionAndJudge(Submission submission, Judge judge);

	    List<Evaluation> findBySubmission(Submission submission);

	    List<Evaluation> findByJudge(Judge judge);
	    
	    List<Evaluation> findBySubmissionTeamTeamLeader(User user);
	    List<Evaluation> findByJudge_Hackathons(Hackathon hackathon);
	    List<Evaluation> findByJudgeJudgeId(UUID judgeId);


}
