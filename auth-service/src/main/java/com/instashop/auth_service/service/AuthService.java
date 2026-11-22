package com.instashop.auth_service.service;

import com.instashop.auth_service.dto.*;
import com.instashop.auth_service.entity.User;
import com.instashop.auth_service.exception.ApiException;
import com.instashop.auth_service.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GoogleTokenService googleTokenService;

    @Transactional
    public AuthResponse register(RegisterRequest request){
        String email = request.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmail(email)){
            throw new ApiException("Email already Registered", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());

        userRepository.save(user);
        return toAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request){
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ApiException("Invalid Email or Password", HttpStatus.UNAUTHORIZED));

        if(user.getPasswordHash() == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
            throw new ApiException("Invalid Email or Password", HttpStatus.UNAUTHORIZED);
        }

        return toAuthResponse(user);
    }

    @Transactional
    public AuthResponse loginWithGoogle(GoogleLoginRequest request){
        GoogleTokenService.GoogleProfile profile = googleTokenService.verify(request.getIdToken());

        User user = userRepository.findByGoogleSub(profile.subject())
                .or(()-> userRepository.findByEmail(profile.email().toLowerCase()))
                .orElseGet(()->{
                    User created = new User();
                    created.setEmail(profile.email().toLowerCase());
                    created.setGoogleSub(profile.subject());
                    created.setName(profile.name());

                    return userRepository.save(created);
                });

        if (user.getGoogleSub() == null){
            user.setGoogleSub(profile.subject());
            userRepository.save(user);
        }

        return toAuthResponse(user);
    }

    @Transactional
    public UserResponse me(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ApiException("User not found", HttpStatus.NOT_FOUND));
        return toUserResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .tokenType("Bearer")
                .expiresInSeconds(jwtService.getExpiresInSeconds())
                .user(toUserResponse(user))
                .build();
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
