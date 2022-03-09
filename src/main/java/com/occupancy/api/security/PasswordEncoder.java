package com.occupancy.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoder {

    //Bcrypt uses a hash algorithm to store a password which is a one-way hash of the password.
    // BCrypt internally generates a random salt while encoding passwords
    // and stores that salt along with the encrypted password encoder
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
