package com.example.emailverificationbackend.auth;

import com.example.emailverificationbackend.appuser.AppUser;
import com.example.emailverificationbackend.appuser.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    public String generateToken(String username, String id) {
        return jwtService.generateToken(username, id);
    }

    public boolean validateToken(String token) {

        return jwtService.validateToken(token);
    }
}