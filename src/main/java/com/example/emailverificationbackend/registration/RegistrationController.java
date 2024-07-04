package com.example.emailverificationbackend.registration;

import com.example.emailverificationbackend.appuser.AppUser;
import com.example.emailverificationbackend.appuser.AppUserRepository;
import com.example.emailverificationbackend.appuser.LoginRequestDto;
import com.example.emailverificationbackend.appuser.LoginResponse;
import com.example.emailverificationbackend.auth.AuthService;
import com.example.emailverificationbackend.auth.JwtService;
import lombok.AllArgsConstructor;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class RegistrationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private final RegistrationService registrationService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AppUserRepository appUserRepository;

    @PostMapping("/auth/api/v1/registration")
    @ResponseBody
    public String register(@RequestBody RegistrationRequest registrationRequest) {
        return registrationService.register(registrationRequest);
    }

    @GetMapping(path = "/auth/api/v1/registration/confirm")
    @ResponseBody
    public HttpStatus confirm(@RequestParam("token") String token) {

        if(Objects.equals(registrationService.confirmToken(token), "confirmed")){
            return HttpStatus.OK;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @PostMapping("/auth/api/v1/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestDto loginRequestDto){
        Long id;
        try {
            Optional<AppUser> appUser = appUserRepository.findByEmail(loginRequestDto.getUserName()).stream().findFirst()
                    .filter((u) -> Objects.equals(u.getEnabled(), true));
            id = appUser.orElseThrow().getId();
        }catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new LoginResponse("user not found", "user not found"), HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUserName(), loginRequestDto.getPassword()
        ));
        String token;
        if(authentication.isAuthenticated()){
            token = authService.generateToken(loginRequestDto.getUserName(), id.toString());
        }
        else {
            throw new RuntimeException("invalid access");
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUserName(loginRequestDto.getUserName());
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/auth/api/v1/validate")
    public ResponseEntity<Object> validateToken(@RequestParam("token") String token) throws AuthenticationException {
        try {
            if(authService.validateToken(token)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new AuthenticationException("Invalid Token");
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
