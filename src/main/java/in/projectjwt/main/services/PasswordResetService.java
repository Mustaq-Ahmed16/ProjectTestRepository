package in.projectjwt.main.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import in.projectjwt.main.entities.User;
import in.projectjwt.main.repositories.UserRepository;

@Service
public class PasswordResetService {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // Implement email sending logic

    private Map<String, String> resetTokens = new HashMap<>(); // Temporary store, use DB in production

    public String sendResetLink(String email) {
    	System.out.println("Received email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, email);

        // Send email with reset link
        String resetLink = "http://localhost:8005/auth/reset-password?token=" + token;
        emailService.sendEmail(email, "Password Reset Request", "Click the link to reset: " + resetLink);

        return "Password reset link sent to your email.";
    }

    public String resetPassword(String token, String newPassword) {
        if (!resetTokens.containsKey(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        String email = resetTokens.get(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt())); // Hash new password
        userRepository.save(user);
        resetTokens.remove(token); // Invalidate token
        return "Password updated successfully.";
    }

}
