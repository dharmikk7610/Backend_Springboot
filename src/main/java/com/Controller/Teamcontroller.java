package com.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DTO.Createrequestdto;
import com.DTO.InviteMemberDTO;
import com.DTO.addmemberdto;
import com.Entity.Evaluation;
import com.Entity.Hackathon;
import com.Entity.Registration;
import com.Entity.Submission;
import com.Entity.Team;
import com.Entity.TeamInvite;
import com.Entity.Teammember;
import com.Entity.User;
import com.Repo.Evaluationrepo;
import com.Repo.Hackthonrepo;
import com.Repo.Inviterepo;
import com.Repo.Submissionrepo;
import com.Repo.Teammemberrepo;
import com.Repo.Teamrepo;
import com.Repo.UserRepository;
import com.Repo.registractionrepo;
import com.Service.Usermailservice;
import com.Util.Jwtutils;

@RestController
@RequestMapping("/team")
public class Teamcontroller {
	
	@Autowired
	Teamrepo tr ;
	
	@Autowired
	Jwtutils jwtutil ;
	
	@Autowired
	UserRepository userrepo ; 
	
	@Autowired
	Inviterepo inviterepo ;
	
	@Autowired
	Teammemberrepo tmrepo ;
	
	@Autowired
	Hackthonrepo hackthonrepo ; 
	
	@Autowired
	Evaluationrepo erepo ;
	
	@Autowired
	Submissionrepo srepo ;
	
	@Autowired
	registractionrepo rrepo ;
	
	@Autowired
	Usermailservice emailService ;
	
	

	@PostMapping("/createteam")
	public ResponseEntity<?> createTemp(@RequestBody Createrequestdto teamdto)
	{
		System.out.println("temaaid-->"+teamdto.getTeam_leaderid());
		System.out.println("hackathonid-->"+teamdto.getHackathon_id());
		UUID teamleaderid = UUID.fromString(teamdto.getTeam_leaderid());
		UUID hackthonid = UUID.fromString(teamdto.getHackathon_id());
		System.out.println("temaaid-->"+teamleaderid);
		System.out.println("hackathonid-->"+hackthonid);
		 User leader = userrepo.findById(teamleaderid)
		            .orElseThrow(() -> new RuntimeException("Team leader not found"));

		    Hackathon hackathon = hackthonrepo.findById(hackthonid)
		            .orElseThrow(() -> new RuntimeException("Hackathon not found"));
		
		
		Team t = new Team() ;
		
		t.setTeamName(teamdto.getTeamName());
		t.setTeamSize(teamdto.getTeamSize());
		t.setTeamStatus(teamdto.getTeamStatus());
		t.setHackathon(hackathon);
		t.setTeamLeader(leader);
		
		t.setCreatedAt(LocalDateTime.now());

		 //  Generate  -->>> invite token
//	    String inviteToken = UUID.randomUUID().toString();
//	    t.setInviteToken(inviteToken);
//	    t.setInviteExpiry(LocalDateTime.now().plusDays(2));

	    Team savedTeam = tr.save(t);
	    
	    Registration r = new Registration();
	    r.setAppliedBy(leader);
	    r.setHackathon(hackathon);
	    r.setTeam(savedTeam);
	    rrepo.save(r);

	    // create the --> Invite link
//	    String inviteLink = "http://localhost:8081/join-team?token=" + inviteToken;


	    return ResponseEntity.ok(savedTeam);
	}
	
	@GetMapping("/Showteam")
	public ResponseEntity<?> showTeam()
	{
		List<Team> team  = tr.findAll();
		return ResponseEntity.ok(team) ;
	}
	
	
	@PostMapping("/deleteteam")
	public ResponseEntity<?> deleteteam(@RequestParam UUID id)
	{
		 Optional<Team> op = tr.findById(id);
		 
		 if(op.isEmpty())
		 {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id not found");
		 }
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(op.get()) ;
	}
	
//	@PostMapping("/addmember")
//	public ResponseEntity<?> addmember(addmemberdto mdto)
//	{
//		UUID teamid = UUID.fromString(mdto.getTeamid());
//		UUID userid = UUID.fromString(mdto.getUserid());
//		
//		Team team = tr.findById(teamid)
//	            .orElseThrow(() -> new RuntimeException("Team not found"));
//
//	    User user = userrepo.findById(userid)
//	            .orElseThrow(() -> new RuntimeException("User not found"));
//		
//	    if (user.getTeam() != null) {
//	        return ResponseEntity.badRequest().body("User already in a team");
//	    }
//	    
//	    user.setTeam(team);
//	    userrepo.save(user);
//
//	    return ResponseEntity.ok("Member added successfully");
//	}
	
	
	@PostMapping("/addmember")
	public ResponseEntity<?> joinTeam(
	        @RequestParam String token,
	        @RequestHeader("Authorization") String auth) {

	    if (auth == null || !auth.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing auth");
	    }

	    String jwt = auth.substring(7);
	    String email = jwtutil.extractUsername(jwt);

	    User user = userrepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    //  Find invite by TOKEN 
	    TeamInvite invite = inviterepo.findByInviteToken(token);
//	            .orElseThrow(() -> new RuntimeException("Invalid invite link"));

	    //  Check invite email
	    if (!invite.getEmail().equalsIgnoreCase(email)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body("This invite is not for your email");
	    }

	    //  Expiry check
	    if (invite.getExpiry().isBefore(LocalDateTime.now())) {
	        return ResponseEntity.status(HttpStatus.GONE).body("Invite link expired");
	    }

	    //  Already used
	    if (invite.isUsed()) {
	        return ResponseEntity.badRequest().body("Invite already used");
	    }

	    Team team = invite.getTeam();

	    //  Already in team
	    if (tmrepo.existsByUserAndTeam(user, team)) {
	        return ResponseEntity.badRequest().body("User already in this team");
	    }

	    //  Team capacity check 
//	    if (tmrepo.countByTeam(team) >= team.getMaxTeamSize()) {
//	        return ResponseEntity.badRequest().body("Team is full");
//	    }

	    //  Add member
	    Teammember member = new Teammember();
	    member.setJoinedAt(LocalDateTime.now());
	    member.setRoleInTeam("MEMBER");
	    member.setTeam(team);
	    member.setUser(user);
	    tmrepo.save(member);

	    //  Mark invite as used
	    invite.setUsed(true);
	    inviterepo.save(invite);

	    return ResponseEntity.ok("Joined team successfully");
	}



	@PostMapping("/invite-member")
	public ResponseEntity<?> inviteMember(@RequestBody InviteMemberDTO dto , @RequestHeader("Authorization") String authHeader)  {

	    UUID teamId = UUID.fromString(dto.getTeamid());

	    Team team = tr.findById(teamId)
	            .orElseThrow(() -> new RuntimeException("Team not found"));

	    //  Optional: only leader can invite
	    // if (!team.getTeamLeader().getUserId().equals(loggedInUserId)) {
	    //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only leader can invite");
	    // }

	    //  new invite token
	    String inviteToken = UUID.randomUUID().toString();
	    tr.save(team);
	    TeamInvite ti = new TeamInvite();
	    ti.setTeam(team);
	    ti.setEmail(dto.getMemberemail());
	    ti.setInviteToken(inviteToken);
	    ti.setExpiry(LocalDateTime.now().plusDays(2));
	    inviterepo.save(ti);
//	    team.setInviteToken(inviteToken);
//	    team.setInviteExpiry(LocalDateTime.now().plusDays(2));
	  

	    String inviteLink =
	            "http://localhost:8081/join-team?token=" + inviteToken;

	    emailService.sendInviteEmail(dto.getMemberemail(), inviteLink);

	    return ResponseEntity.ok("Invite sent successfully");
	}
	
	
	@GetMapping("/findmyresult")
	public ResponseEntity<?> findmyresult(@RequestParam UUID id)
	{
		Optional<User> user  = userrepo.findById(id);
		
		 List<Evaluation> list = erepo.findBySubmissionTeamTeamLeader(user.get());
		
		return ResponseEntity.ok(list) ;
	}
	
	@GetMapping("/checkprojectSubmission")
	public ResponseEntity<?> checkSubmission(
	        @RequestParam UUID id,
	        @RequestHeader("Authorization") String authHeader
	) {
	    String jwt = authHeader.substring(7);
	    String email = jwtutil.extractUsername(jwt);

	    User user = userrepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Check if submission exists for this team leader & hackathon
	    boolean submitted = srepo.existsByTeam_TeamLeaderAndHackathon_HackathonId(user, id);

	    return ResponseEntity.ok(Map.of(
	            "submitted", submitted
	    ));
	}


	
	
	

}
