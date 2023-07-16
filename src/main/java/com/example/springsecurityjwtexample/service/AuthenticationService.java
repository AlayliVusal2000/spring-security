package com.example.springsecurityjwtexample.service;
import com.example.springsecurityjwtexample.dao.entity.User;
import com.example.springsecurityjwtexample.dao.repository.UserRepo;
import com.example.springsecurityjwtexample.model.AuthenticationRequest;
import com.example.springsecurityjwtexample.model.AuthenticationResponse;
import com.example.springsecurityjwtexample.model.RegisterRequest;
import com.example.springsecurityjwtexample.model.RegisterResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.springsecurityjwtexample.model.RegisterResponse.buildRegisterDto;

@Service
public record AuthenticationService(UserRepo repository,
                                    PasswordEncoder passwordEncoder,
                                    JwtService jwtService,
                                    AuthenticationManager authenticationManager) {

    public RegisterResponse register(RegisterRequest request) {

        var isExist = repository.findByEmail(request.getEmail()).isPresent();
        if (isExist) {
            throw new RuntimeException("Email already exist!");
        }
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var userEntity = repository.save(user);

        return buildRegisterDto(userEntity);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found!"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

