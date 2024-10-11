package org.example.cardAPI.model;

import java.time.LocalDateTime;

public class LoginAttempt {
    private int attempts;
    private LocalDateTime lastAttempt;

    public LoginAttempt(int attempts, LocalDateTime lastAttempt) {
        this.attempts = attempts;
        this.lastAttempt = lastAttempt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
        this.lastAttempt = LocalDateTime.now();
    }

    public LocalDateTime getLastAttempt() {
        return lastAttempt;
    }

    public void resetAttempts() {
        this.attempts = 0;
    }
}
