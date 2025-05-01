package no.ntnu.idatt2106.krisefikser.service;

public interface PasswordResetService {

    /**
     * Initiates a password reset by generating a token, saving it,
     * and sending a reset email to the given address.
     *
     * @param email the user's email address
     */
    void initiateReset(String email);

    /**
     * Completes a password reset by validating the token and
     * updating the user's password.
     *
     * @param token the reset token
     * @param newPassword the new password to set
     */
    void completeReset(String token, String newPassword);
}