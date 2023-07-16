package com.example.springsecurityjwtexample.model;


import com.example.springsecurityjwtexample.dao.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterResponse {
    String fullName;
    String email;
    String password;
    Role role;

    public static RegisterResponse buildRegisterDto(User user) {
        return RegisterResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole()).build();
    }

}
