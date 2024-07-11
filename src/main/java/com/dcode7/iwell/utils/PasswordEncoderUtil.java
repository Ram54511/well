package com.dcode7.iwell.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderUtil {
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordEncoderUtil() {
        // Higher strength means more computation time, making the hashing process slower and safer
        int strength = 10; // or another appropriate value
        this.passwordEncoder = new BCryptPasswordEncoder(strength);
    }

    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
