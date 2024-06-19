package com.communityhub.community.hub_app.service;

import com.communityhub.community.hub_app.dto.AuthenticationResponse;
import com.communityhub.community.hub_app.dto.LoginRequest;
import com.communityhub.community.hub_app.dto.RegisterRequest;
import com.communityhub.community.hub_app.exceptions.CommunityHubException;
import com.communityhub.community.hub_app.model.NotificationEmail;
import com.communityhub.community.hub_app.model.User;
import com.communityhub.community.hub_app.model.VerificationToken;
import com.communityhub.community.hub_app.repository.UserRepository;
import com.communityhub.community.hub_app.repository.VerificationTokenRepository;
import com.communityhub.community.hub_app.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
//public class AuthService {
//
//    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
//    private final VerificationTokenRepository verificationTokenRepository;
//    private final MailService mailService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtProvider jwtProvider;
//
//    @Transactional
//    public void signup(RegisterRequest registerRequest){
//        User user = new User();
//        user.setUsername(registerRequest.getUsername());
//        user.setEmail(registerRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//        user.setCreated(Instant.now());
//        user.setEnabled(false);
//        userRepository.save(user);
//       String token = generateVerificationToken(user);
//       mailService.sendMail(new NotificationEmail("Please Activate your Account",
//               user.getEmail(), "Thank you for signing up to CommunityHub, "+
//               "http://localhost:8080/api/auth/accountVerification/" + token));
//    }
//
//    private String generateVerificationToken(User user) {
//        String token = UUID.randomUUID().toString();
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setToken(token);
//        verificationToken.setUser(user);
//
//        verificationTokenRepository.save(verificationToken);
//        return token;
//    }
//
//
//    public void verifyAccount(String token) {
//        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
//        verificationToken.orElseThrow(() -> new CommunityHubException("Invalid Token"));
//        fetchUserAndEnable(verificationToken.get());
//    }
//
//    @Transactional
//    private void fetchUserAndEnable(VerificationToken verificationToken) {
//       String username =  verificationToken.getUser().getUsername();
//     User user =  userRepository.findByUsername(username).orElseThrow(() -> new CommunityHubException("User not found with name"+username));
//     user.setEnabled(true);
//     userRepository.save(user);
//    }
//
//    public AuthenticationResponse login(LoginRequest loginRequest) {
//        log.info("Login request: "+loginRequest);
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
//                loginRequest.getPassword()));
//        log.info("Authentication: "+authenticate);
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String token = jwtProvider.generateToken(authenticate);
//        log.info("Token: "+token);
//        AuthenticationResponse response=  new AuthenticationResponse(token,loginRequest.getUsername());
//        log.info("Authentication response: "+response);
//        return response;
//
//    }
//
//}

public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
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

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new CommunityHubException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CommunityHubException("User not found with name" + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        log.info("Login request: " + loginRequest);
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            log.info("Authentication: " + authenticate);
            SecurityContextHolder.getContext().setAuthentication(authenticate);

//            UserDetailImpl userDetails = (UserDetailImpl) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(authenticate);
            log.info("Token: " + token);
            AuthenticationResponse response = new AuthenticationResponse(token, loginRequest.getUsername());
            log.info("Authentication response: " + response);
            return response;
        } catch (Exception e) {
            log.info("Exception: " + e);
            throw e;
        }
    }
}
