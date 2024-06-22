package com.communityhub.community.hub_app.service;

import com.communityhub.community.hub_app.dto.AuthenticationResponse;
import com.communityhub.community.hub_app.dto.GeneralResponse;
import com.communityhub.community.hub_app.dto.LoginRequest;
import com.communityhub.community.hub_app.dto.RegisterRequest;
import com.communityhub.community.hub_app.exceptions.CommunityHubException;
import com.communityhub.community.hub_app.model.NotificationEmail;
import com.communityhub.community.hub_app.model.User;
import com.communityhub.community.hub_app.model.VerificationToken;
import com.communityhub.community.hub_app.repository.UserRepository;
import com.communityhub.community.hub_app.repository.VerificationTokenRepository;
import com.communityhub.community.hub_app.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
@EnableTransactionManagement
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    @Transactional
    public void signup(RegisterRequest registerRequest) {
        // Validate password strength
        validatePasswordStrength(registerRequest.getPassword());

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to CommunityHub, " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private void validatePasswordStrength(String password) {
        // Implement password strength validation logic
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plusSeconds(86400)); // Token valid for 24 hours

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new CommunityHubException("Invalid Token"));
        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new CommunityHubException("Token expired");
        }
        fetchUserAndEnable(verificationToken);
    }

    @Transactional
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: "+authentication);
        UserDetailImpl userDetail= (UserDetailImpl) authentication.getPrincipal();
        log.info("User detail: "+userDetail);
        return userRepository.findByUsername(userDetail.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - "));
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CommunityHubException("User not found with name " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        log.info("Login request: {}", loginRequest);
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            log.info("Authentication: {}", authenticate);
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            String token = jwtProvider.generateToken(authenticate);
            log.info("Token: {}", token);
            GeneralResponse generalResponse = new GeneralResponse("Authentication Successful", "success", 200);
            return new AuthenticationResponse(token, loginRequest.getUsername(), generalResponse);
        } catch (Exception e) {
            log.error("Exception during login: ", e);
            GeneralResponse generalResponse = new GeneralResponse(e.getMessage(), "failed", 0);
            return new AuthenticationResponse("", loginRequest.getUsername(), generalResponse);
        }
    }
}
