package com.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Usermailservice {

	@Autowired
	JavaMailSender mailsender ; 
	
	
	public void sendmail(String otp , String name , String email )
	{
		SimpleMailMessage m1 = new SimpleMailMessage();
		m1.setTo(email);
		  m1.setSubject("Your OTP Code:");
	        m1.setText(name + ", your OTP is: " + otp + " for password change.");
	        mailsender.send(m1);
	}
	
	public void judgeinvitemail(String name , String password , String mail)
	{
		 SimpleMailMessage m1 = new SimpleMailMessage();
		    m1.setTo(mail);
		    m1.setSubject("Invitation to Join as Hackathon Judge");

		    m1.setText(
		        "Hello " + name + ",\n\n" +
		        "You have been invited to join our Hackathon as a Judge.\n\n" +
		        "Your login credentials are below:\n" +
		        "Email: " + mail + "\n" +
		        "Temporary Password: " + password + "\n\n" +
		        "Please log in and change your password after your first login.\n\n" +
		        "Best Regards,\n" +
		        "Hackathon Team"
		    );

		    mailsender.send(m1);
	}
	
	
	 public void sendInviteEmail(String toEmail, String inviteLink) {

	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(toEmail);
	        message.setSubject("You're invited to join a team!");
	        message.setText(
	                "Hello,\n\n" +
	                "You have been invited to join a team.\n\n" +
	                "Click the link below to join:\n" +
	                inviteLink + "\n\n" +
	                "Note: This link will expire.\n\n" +
	                "Best regards,\nHackathon Team"
	        );

	        mailsender.send(message);
	    }
	
	
	
}
