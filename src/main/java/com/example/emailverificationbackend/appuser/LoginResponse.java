package com.example.emailverificationbackend.appuser;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String userName;
    private String token;
}
