package com.example.emailverificationbackend.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    public static final String SECRET = "Random generate secret key";

    public void validateToken(final String token) {
       Jwts.parser()
               .verifyWith(getSignKey())
               .build()
               .parse(token);
    }

    public String generateToken(String userName, String id) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, id);
    }

    private String createToken(Map<String, Object> claims, String userName, String id) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .id(id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        return Jwts.SIG.HS256.key().build();
    }
}
