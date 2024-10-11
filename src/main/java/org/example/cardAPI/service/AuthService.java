package org.example.cardAPI.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final String SECRET_KEY = "your_secret_key"; // Use uma chave segura em produção
    private final long EXPIRATION_TIME = 864_000_000; // 10 dias em milissegundos

    // Gera um token JWT contendo username e roles
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", "USER"); // Papel do usuário, pode ser parametrizado

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
