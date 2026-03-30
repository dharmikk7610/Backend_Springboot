package com.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Entity.Hackathon;
import com.Entity.User;
import com.Repo.Hackthonrepo;
import com.Repo.UserRepository;



@Service
public class Paginationservice {
	
	@Autowired
	UserRepository urepo ;
	
	@Autowired
	Hackthonrepo hrepo ;
	
	 public List<User> getUsers(int pageNumber, int pageSize) {
	        Pageable pageable =  PageRequest.of(pageNumber, pageSize);
	        Page<User> page =  urepo.findAll(pageable);
	        
	        List<User> listuser = page.getContent();
	        return listuser ;
	        
//	 public List<Hackathon> gethackathon(int pageNumber , int pageSize)
//	 {
//		 Pageable pageable2 = PageRequest.of(pageNumber,pageSize);
//		 Page<Hackathon> page2 = hrepo.findAll(pageable2);
//		 List<Hackathon> listhack = page2.getContent();
//		 return listhack ;
//	 }
	      

}
	
}


