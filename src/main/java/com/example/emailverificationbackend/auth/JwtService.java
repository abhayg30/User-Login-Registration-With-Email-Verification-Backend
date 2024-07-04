package com.example.emailverificationbackend.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    public static final String SECRET = "4261656C64756E674261656C64756E674261656C64756E67";

    public boolean validateToken(final String token) {

        try{
            Jwts.parser().verifyWith(getSignKey()).build().parse(token);
            return true;
        } catch (Exception ex){
            return false;
        }
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
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
