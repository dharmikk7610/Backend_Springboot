package com.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Entity.Judge;
import com.Entity.OrganizerProfile;
import com.Entity.User;
import com.Repo.Organizationrepo;
import com.Repo.UserRepository;
import com.Repo.judgerepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private Organizationrepo oprepo ;
    
    @Autowired
    judgerepo jrepo ;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("welcome userdetails servicess==>>"+username);
		
		String staticAdminEmail = "admin123@gmail.com";
	    String staticAdminPassword = "{noop}admin@123"; 

	    if (username.equalsIgnoreCase(staticAdminEmail)) {
	        System.out.println("Logging in static admin");

	        return org.springframework.security.core.userdetails.User
	                .withUsername(staticAdminEmail)
	                .password(staticAdminPassword)
	                .roles("ADMIN")
	                .build();
	    }
		
		
		if(oprepo.findByOfficialEmail(username).isPresent())
		{
			OrganizerProfile  op = oprepo.findByOfficialEmail(username)
					.orElseThrow(()->new UsernameNotFoundException(username));
			System.out.println("op data-->>"+op);
			
			  return org.springframework.security.core.userdetails.User
		                .withUsername(op.getOfficialEmail())
		                .password(op.getPassword())
		                .roles("ORGANIZER")
		                .build();
		}
		
		if(jrepo.findByEmail(username).isPresent())
		{
			Judge  op = jrepo.findByEmail(username)
					.orElseThrow(()->new UsernameNotFoundException(username));
			
			  return org.springframework.security.core.userdetails.User
		                .withUsername(op.getEmail())
		                .password(op.getPassword())
		                .roles("JUDGE")
		                .build();
		}
		
		
		
		
		  User user = userRepository.findByEmail(username)
	        		.orElseThrow(() -> new UsernameNotFoundException("not found: " ));
		  String loginName = user.getUsername() != null ? user.getUsername() : user.getEmail();

		 


	        return org.springframework.security.core.userdetails.User
	                .withUsername(loginName)
	                .password(user.getPasswordHash())
	                .roles(user.getRole().toUpperCase())
//	                .roles("USER")
	                .build();
	}
}
