package com.example.spring_dblab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignupDto {
    private String email;
    private String password;
    private String name;
    private String nickName;
}
