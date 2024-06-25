package com.example.emailverificationbackend.appuser;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequestDto {

    private String userName;
    private String password;
}
