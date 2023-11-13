package com.example.spring_dblab.auth;

import com.example.spring_dblab.dto.LoginDto;
import com.example.spring_dblab.dto.LoginReturnDto;
import com.example.spring_dblab.dto.RefreshTokenDto;
import com.example.spring_dblab.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 로그인, 회원가입, 로그아웃 및 토큰 관련 작업을 처리하는 인증 컨트롤러.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 제공된 로그인 자격 증명으로 사용자를 로그인합니다.
     *
     * @param loginDto 로그인 정보를 포함하는 데이터 전송 객체
     * @return 로그인한 사용자의 토큰 정보
     */
    @PostMapping("/login")
    public LoginReturnDto login(@RequestBody LoginDto loginDto) throws Exception {
        return authService.login(loginDto);
    }

    /**
     * 제공된 회원가입 정보로 새로운 사용자를 등록합니다.
     *
     * @param signupDto 사용자 회원가입 정보를 포함하는 데이터 전송 객체
     * @return 회원가입 작업의 결과를 나타내는 문자열
     */
    @PostMapping("/signup")
    public String signup(@RequestBody SignupDto signupDto) {
        return authService.signUp(signupDto);
    }

    /**
     * 현재 사용자를 로그아웃합니다.
     *
     * @return 로그아웃 작업의 결과를 나타내는 문자열
     */
    @GetMapping("/logout")
    public String logout() throws Exception {
        return authService.logout();
    }

    /**
     * 제공된 새로 고침 토큰을 사용하여 새 액세스 토큰을 검색하거나 재로그인을 트리거합니다.
     *
     * @param dto 데이터 전송 객체, 새로 고침 토큰 또는 재로그인을 위한 null
     * @return 새 토큰 정보가 적용된 경우 해당 정보를 포함하는 옵셔널
     * @throws Exception 토큰 생성 과정에 실패한 경우
     */
    @GetMapping("/token")
    public String Token(@ModelAttribute("dto") Object dto) throws Exception {
        if(dto instanceof RefreshTokenDto refreshTokenDto) {
            return String.valueOf(Optional.ofNullable(authService.refreshToken(refreshTokenDto)));
        }
        return authService.reLogin();
    }
}

