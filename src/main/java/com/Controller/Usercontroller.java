package com.Controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.DTO.MemberDTO;
import com.DTO.TeamDTO;
import com.Entity.Hackathon;
import com.Entity.OrganizerProfile;
import com.Entity.Registration;
import com.Entity.Submission;
import com.Entity.Team;
import com.Entity.Teammember;
import com.Entity.User;
import com.Repo.Hackthonrepo;
import com.Repo.Organizationrepo;
import com.Repo.Submissionrepo;
import com.Repo.Teammemberrepo;
import com.Repo.Teamrepo;
import com.Repo.UserRepository;
import com.Repo.registractionrepo;
import com.Service.Cloudinaryservice;
import com.Service.Paginationservice;
import com.Service.Passwordencoder;
import com.Service.Usermailservice;
import com.Util.Jwtutils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/public")
public class Usercontroller  {

  @Autowired
  UserRepository userrepo ;
  
  @Autowired
  registractionrepo rrepo ;
  
  @Autowired
  Passwordencoder passwordencoders ;
  
  @Autowired
  Cloudinaryservice cloudinartservice ;

  @Autowired
  Usermailservice mailservice ;
  
  @Autowired
  Organizationrepo oprepo ;
  
  @Autowired
  Teammemberrepo teammemberRepo ;
  
  @Autowired
  Teamrepo trepo ;
  
  @Autowired
  Paginationservice pservice ;
  
  @Autowired
  Hackthonrepo hrepo ;
  
  @Autowired
  Submissionrepo srepo ;
  
  @Autowired
  Jwtutils jwtutil ; 
  
  
  
  @GetMapping("/findsubmissionofuser")
  public ResponseEntity<?> findsubmissionByuser(@RequestParam UUID id) {

      User user = userrepo.findById(id)
              .orElseThrow(() -> new RuntimeException("User not found"));

      List<Submission> list = srepo.findByTeam_TeamLeader(user);
      return ResponseEntity.ok(list);
  }

  
  @GetMapping("/gethackathonbyuser2")
  public ResponseEntity<?> getHackathonByUserAndHackathon(
          @RequestParam UUID id,
          @RequestParam UUID hackid) {

      // 1️⃣ Validate user
      User user = userrepo.findById(id)
              .orElseThrow(() ->
                      new RuntimeException("User not found"));

      // 2️⃣ Fetch teams by leader
      List<Team> teams = trepo.findByTeamLeader(user);

      // 3️⃣ Filter by hackathon id
      List<Team> result = teams.stream()
              .filter(team ->
                      team.getHackathon() != null &&
                      team.getHackathon().getHackathonId().equals(hackid))
              .toList();

      // 4️⃣ Return response
      System.out.println("data->"+result);
      return ResponseEntity.ok(result);
  }

  
    // ===============================
    // REGISTER USER
    // ===============================
    @PostMapping("/register")
    public ResponseEntity<?> registeruser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPasswordHash(passwordencoders.getencoder().encode(user.getPasswordHash()));
        
        return ResponseEntity.ok(userrepo.save(user));
    }
    
    // logout |||||
    
    @GetMapping("/logout")
    public ResponseEntity<?> logoutuser(@RequestParam UUID id)
    {
    	Optional<User> op   = userrepo.findById(id);
    	op.get().setStatus("inactive");
    	return ResponseEntity.ok("logout sucess");
    }

    // ===============================
    // LOGIN USER 
    // ===============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
    	
    	if(user.getEmail().equals("admin123@gmail.com")&& user.getPasswordHash().equals("Admin@123") )
        {
    		User u = new User() ;
    		u.setRole("admin");
    		u.setEmail(user.getEmail());
    		
//    		u.setPasswordHash("Admin@123")
    		 String token =  jwtutil.generateToken("admin123@gmail.com","ADMIN");
    		 Map<Object, Object> m = new HashMap<>();
    	        m.put("data", u);
    	        m.put("token",token);
     	   return ResponseEntity.ok(m);
        }
    	
        Optional<User> myuser = userrepo.findByEmail(user.getEmail());

        if (myuser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
        
        

        // NOTE: Password comparison should be encrypted in production
//        if (!myuser.get().getPasswordHash().equals(user.getPasswordHash())) {
//           
//        }
        if(!passwordencoders.getencoder().matches( user.getPasswordHash(),myuser.get().getPasswordHash()))
        {
        	 return ResponseEntity.status(401).build();
        }
       

        myuser.get().setLastLogin(LocalDateTime.now());
        myuser.get().setStatus("Active");
        userrepo.save(myuser.get());
        String token =  jwtutil.generateToken(myuser.get().getEmail(), myuser.get().getRole().toUpperCase());
        
        Map<Object, Object> m = new HashMap<>();
        m.put("data", myuser.get());
        m.put("token", token);
        return ResponseEntity.ok(m);
    }

    // ===============================
    // GET USER BY ID
    // ===============================
    
    @GetMapping("/getuser")
    public ResponseEntity<?>getuser(@RequestParam String id) {
//        User user = getUserByIdCached(id);
    	UUID id2 = UUID.fromString(id);
    User user = 	userrepo.findById(id2).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
//    @Cacheable(value = "userCache", key = "#id")
//    public User getUserByIdCached(UUID id) {
//        System.out.println("👉 HITTING DATABASE for id: " + id);
//        return userrepo.findById(id).orElse(null);
//    }


    // ===============================
    // GET ALL USERS
    // ===============================
    @GetMapping("/getalldata")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userrepo.findAll());
    }
//
//    @Cacheable("users")
//    public List<User> getAllCached() {
//    	System.out.println("Only one ...");
//        return userrepo.findAll();
//    }
    
    @GetMapping("/getdata")
    public List<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return pservice.getUsers(page, size);
    }
    


    // ===============================
    // UPDATE USER
    // ===============================
//    @CachePut(value="users")
    @PutMapping("/changeuserdata/{id}")
  
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody User updatedUser) {

        return userrepo.findById(id)
                .map(user -> {

                    if (updatedUser.getPhoneNumber() != null)
                        user.setPhoneNumber(updatedUser.getPhoneNumber());

                    if (updatedUser.getCollegeName() != null)
                        user.setCollegeName(updatedUser.getCollegeName());

                    if (updatedUser.getDepartment() != null)
                        user.setDepartment(updatedUser.getDepartment());

                    if (updatedUser.getStudyYear() != null)
                        user.setStudyYear(updatedUser.getStudyYear());

                    if (updatedUser.getGithub() != null)
                        user.setGithub(updatedUser.getGithub());

                    if (updatedUser.getLinkedin() != null)
                        user.setLinkedin(updatedUser.getLinkedin());

                    if (updatedUser.getBio() != null)
                        user.setBio(updatedUser.getBio());

                    if (updatedUser.getSkills() != null)
                        user.setSkills(updatedUser.getSkills());
                    if(updatedUser.getLocation()!=null)
                    	user.setLocation(updatedUser.getLocation());

                    user.setUpdatedAt(LocalDateTime.now());

                    // save optional but safe
                    userrepo.save(user);

                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // ===============================
    // DELETE USER
    // ===============================
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID id) {
    
    	
    	
        userrepo.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ===============================
    // UPDATE USER STATUS
    // ===============================
    @PatchMapping("/updatedetails")
    public ResponseEntity<User> updateStatus(@RequestParam UUID id,
                                             @RequestParam String status) {
        return userrepo.findById(id)
                .map(user -> {
                    user.setStatus(status);
                    user.setUpdatedAt(LocalDateTime.now());
                    return ResponseEntity.ok(userrepo.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ===============================
    // UPDATE USER ROLE
    // ===============================
//    @PatchMapping("/{id}/role")
//    public ResponseEntity<User> updateRole(@PathVariable UUID id,
//                                           @RequestParam String role) {
//        return userrepo.findById(id)
//                .map(user -> {
//                    user.setRole(role);
//                    user.setUpdatedAt(LocalDateTime.now());
//                    return ResponseEntity.ok(userrepo.save(user));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
    // ===============================
    // Change password  USER ROLE
    // ===============================
    @PostMapping("/forgotpass")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
    	
    	Optional<User> userOpt =	userrepo.findByEmail(email);
        

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();

        // Generate 4-digit OTP
        int otp = (int) (Math.random() * 9000) + 1000;
        String otpString = String.valueOf(otp);

        // Save OTP & expiry (add these fields in User entity)
        user.setOtp(otpString);
        user.setExpireotp(LocalDateTime.now().plusMinutes(10));
//        userrepo.save(user);

        // Send email
        mailservice.sendmail(
                otpString,
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok("OTP sent to registered email");
    }
    
    @PostMapping("/checkotp")
    public ResponseEntity<?> checkotp(@RequestParam String email , @RequestParam String otp){
    	 Optional<User> user = userrepo.findByEmail(email);
    	 if(user.isEmpty())
    	 {
    		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id not found");
    	 }
    	if(otp.equalsIgnoreCase(user.get().getOtp()))
    	{
    		
    		return ResponseEntity.status(HttpStatus.OK).body(otp);
    	}
    	return null ;
    }
    
    @PostMapping("/repassword")
    public ResponseEntity<?> repassword(
            @RequestParam String  email,
            @RequestParam String newPassword) {

        Optional<User> userOpt = userrepo.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();

        // Set new password directly
        user.setPasswordHash(passwordencoders.getencoder().encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userrepo.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }
    
    //=========================================
    //    Update Details  
    //=========================================
    
    @PatchMapping("setprofil/{id}")
    public ResponseEntity<?> setprofil(@PathVariable UUID id, @RequestBody User u ,MultipartFile file) {
    
    	Optional<User> myuser = userrepo.findById(id);
    	
    	if(myuser.isEmpty())
    	{
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    	}
    	myuser.get().setBio(u.getBio());
    	myuser.get().setCollegeName(u.getCollegeName());
    	myuser.get().setDepartment(u.getDepartment());
    	myuser.get().setEmail(u.getEmail());
    	
    	myuser.get().setName(u.getName());
    	myuser.get().setGithub(u.getGithub());
    	myuser.get().setStudyYear(u.getStudyYear());
    	myuser.get().setLinkedin(u.getLinkedin());
    	myuser.get().setPhoneNumber(u.getPhoneNumber());
    	
    	  if (file != null && !file.isEmpty()) {
    	        try {
    	            Map<?, ?> uploadResult = cloudinartservice
    	                    .cloudinary()
    	                    .uploader()
    	                    .upload(file.getBytes(), Map.of("folder", "profile_images"));

    	            String imageUrl = uploadResult.get("secure_url").toString();
    	           myuser.get().setProfileImage(imageUrl);

    	        } catch (Exception e) {
    	            return ResponseEntity
    	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
    	                    .body("Image upload failed");
    	        }
    	    }
    	 myuser.get().setUsername(u.getUsername());
    	
    	 
    	
        return ResponseEntity.ok(myuser)  ;
    }

//----------------Organization ------------------//
    @PostMapping("/createorganization")
    public ResponseEntity<?> createOrganizationProfile(
            @RequestParam UUID userId,
            @RequestBody OrganizerProfile op) {

        User user = userrepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Check if organizer profile already exists
        Optional<OrganizerProfile> existing =
                oprepo.findByUser_UserId(user.getUserId());

        if (existing.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Organizer profile already exists. Please login.");
        }

        // ✅ Link user ↔ organizer
        op.setUser(user);

        // ⚠️ Only encode password if it exists
        if (op.getPassword() != null && !op.getPassword().isBlank()) {
            op.setPassword(passwordencoders.getencoder().encode(op.getPassword()));
        }

        OrganizerProfile saved = oprepo.save(op);

        Map<String, Object> response = new HashMap<>();
        response.put("data", saved);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

	
	@PostMapping("/organizationlogin")
	public ResponseEntity<?> Organizationlogin(@RequestBody OrganizerProfile op ) {
		//TODO: process POST request
		Optional<OrganizerProfile> op2 = oprepo.findByOfficialEmail(op.getOfficialEmail());
		
		if(op2.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("email not found");
		}
		
		if(!passwordencoders.getencoder().matches( op.getPassword(),op2.get().getPassword()))
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("password is not match ...");
		}
		
		 String token = jwtutil.generateToken(op.getOfficialEmail(), "ORGANIZER");
		 Map<Object, Object> map  = new HashMap<>();
		    map.put("data", op);
		    map.put("token", token);
		    return ResponseEntity.status(HttpStatus.CREATED)
		            .body(map);
	}
	
	@GetMapping("/showhackathon")
	public ResponseEntity<?> Showallhackathon() {
		List<Hackathon> list = hrepo.findAll();
		return ResponseEntity.ok(list);
	}
	
    @GetMapping("/showhackathonbyid")
    public ResponseEntity<?> showhackahon(@RequestParam String id)
    {
    	UUID id2 = UUID.fromString(id);
    	 Optional<Hackathon> op = hrepo.findById(id2);
    	 if(op.isEmpty())
    	 {
    		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("hack id not found");
    	 }
    	 return ResponseEntity.ok(op.get());
    }
    
//    @GetMapping("/gethackathonbyuser")
//    public ResponseEntity<?> showmyhackathon(@RequestParam String id)
//    {
//  
//    	UUID id2  =  UUID.fromString(id);
//    Optional<User> user  = 	userrepo.findById(id2);
//    	List<Team> list  = trepo.findByTeamLeader_UserId(id2);
//   
//    	List<Registration> list  = rrepo.findByAppliedBy_UserId(id2);
//    
//    	
//     return ResponseEntity.ok(list) ;
//    }
    
    
    @GetMapping("/gethackathonbyuser")
    public ResponseEntity<?> showmyhackathon(@RequestParam String id) {

        UUID userId = UUID.fromString(id);

        // 1. Teams where user is leader
        List<Team> leaderTeams = trepo.findByTeamLeader_UserId(userId);

        // 2. Teams where user is member
        List<Teammember> memberships = teammemberRepo.findByUser_UserId(userId);

        Set<Team> allTeams = new HashSet<>();
        allTeams.addAll(leaderTeams);

        for (Teammember tm : memberships) {
            allTeams.add(tm.getTeam());
        }

        // 3. Convert to DTO
        List<TeamDTO> dtoList = allTeams.stream()
            .map(team -> mapToDTO(team))
            .toList();

        return ResponseEntity.ok(dtoList);
    }

    
    
    //method
    private TeamDTO mapToDTO(Team team) {

        TeamDTO dto = new TeamDTO();
        dto.setTeamId(team.getTeam_id());
        dto.setTeamName(team.getTeamName());
        dto.setHackathon(team.getHackathon());
        dto.setCreatedAt(team.getCreatedAt());
        dto.setInviteToken(team.getInviteToken());
        dto.setLeaderId(team.getTeamLeader().getUserId());
       

        List<MemberDTO> members = team.getMembers().stream().map(tm -> {
            MemberDTO m = new MemberDTO();
            m.setUserId(tm.getUser().getUserId());
            m.setName(tm.getUser().getName());
            m.setEmail(tm.getUser().getEmail());
            m.setRoleInTeam(tm.getRoleInTeam());
            return m;
        }).toList();

        dto.setMembers(members);
        return dto;
    }

    
    
    
    
    
    
    
}

