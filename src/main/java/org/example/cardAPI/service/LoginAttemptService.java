package org.example.cardAPI.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {
    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final int MAX_ATTEMPTS = 5; // Defina o número máximo de tentativas
    private final long BLOCK_TIME = 300_000; // Tempo de bloqueio em milissegundos (5 minutos)

    public void loginSucceeded(String username) {
        attemptsCache.remove(username); // Remove o usuário se o login for bem-sucedido
    }

    public void loginFailed(String username) {
        attemptsCache.put(username, attemptsCache.getOrDefault(username, 0) + 1);
    }

    public boolean isBlocked(String username) {
        return attemptsCache.getOrDefault(username, 0) >= MAX_ATTEMPTS;
    }
}