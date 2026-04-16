package com.Firmanann.CoreBankingSystem.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //Method untuk Hash password
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
