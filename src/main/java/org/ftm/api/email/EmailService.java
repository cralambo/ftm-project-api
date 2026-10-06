package org.ftm.api.email;


public interface EmailService {

    void sendWelcomeEmail(
            String email,
            String firstname,
            String lastname,
            String temporaryPassword
    );
}
