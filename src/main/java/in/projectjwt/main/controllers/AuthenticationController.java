package in.projectjwt.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.projectjwt.main.dtos.LoginUserDto;
import in.projectjwt.main.dtos.PasswordResetRequestDto;
import in.projectjwt.main.entities.User;
import in.projectjwt.main.services.AuthenticationService;
import in.projectjwt.main.services.JwtService;
import in.projectjwt.main.services.PasswordResetService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
	private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;
    @Autowired
    private PasswordResetService passwordResetService;
  

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody User user){
    	String response = authenticationService.signup(user);
        if (response.contains("already created")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // HTTP 409 Conflict
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    	User registeredUser = authenticationService.signup(registerUserDto);
//    
//        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String response = passwordResetService.sendResetLink(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestDto request) {
        String response = passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(response);
    }

}
