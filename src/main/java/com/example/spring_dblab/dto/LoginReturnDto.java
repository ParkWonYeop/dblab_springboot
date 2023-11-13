package com.example.spring_dblab.dto;

import com.example.spring_dblab.jwt.TokenInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginReturnDto {
    TokenInfo tokenInfo;
    String nickname;
}
