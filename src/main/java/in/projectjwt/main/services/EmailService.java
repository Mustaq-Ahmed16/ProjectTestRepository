package in.projectjwt.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Value("${spring.mail.username")
	private String fromEmailId;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String email, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        try {
        	System.out.println(email);
        	System.out.println(subject);
        	System.out.println(body);
            mailMessage.setFrom(fromEmailId);
            mailMessage.setTo(email);
            mailMessage.setText(body);
            mailMessage.setSubject(subject);
            

            // Sending the email
            mailSender.send(mailMessage);
            System.out.println("Email sent successfully to " + email);
            
        } catch (Exception e) {
            // Improved error handling
            System.out.println("Error in sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
