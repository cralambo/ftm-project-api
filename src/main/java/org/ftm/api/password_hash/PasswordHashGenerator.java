package org.ftm.api.password_hash;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String password = "Ftm@Admin#2026!";

        String hash = encoder.encode(password);

        System.out.println("Password hash: " + hash);
        System.out.println("Password valid: " + encoder.matches(password, hash));
    }
}
