package com.example.spring_dblab.auth;

import com.example.spring_dblab.dto.LoginDto;
import com.example.spring_dblab.dto.LoginReturnDto;
import com.example.spring_dblab.dto.RefreshTokenDto;
import com.example.spring_dblab.dto.SignupDto;
import com.example.spring_dblab.entitiy.User;
import com.example.spring_dblab.enums.RoleEnum;
import com.example.spring_dblab.jwt.JwtTokenProvider;
import com.example.spring_dblab.jwt.TokenInfo;
import com.example.spring_dblab.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.spring_dblab.utils.SecurityUtil.getCurrentMemberEmail;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param loginDto 로그인 정보를 담은 데이터 전송 객체
     * @return 로그인 후 생성된 토큰 정보
     */
    public LoginReturnDto login(LoginDto loginDto) throws Exception {
        try {
            String email = loginDto.getEmail();
            String password = loginDto.getPassword();

            Optional<User> user = userRepository.findByEmail(email);

            if (user.isEmpty()) {
                log.error("User not Found");
                throw new Exception("User not found");
            }

            Collection<? extends GrantedAuthority> authorities = user.get().getAuthorities();
            Collection<GrantedAuthority> newAuthorities = new ArrayList<>();

            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(RoleEnum.USER.name())) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }
                if (authority.getAuthority().equals(RoleEnum.ADMIN.name())) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }
                if (authority.getAuthority().equals(RoleEnum.ORGANIZER.name())) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_ORGANIZER"));
                }
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password, newAuthorities);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            user.ifPresent(value -> {
                value.setRefreshToken(tokenInfo.getRefreshToken());
                userRepository.save(value);
            });

            LoginReturnDto loginReturnDto = new LoginReturnDto();
            loginReturnDto.setNickname(user.get().getNickName());
            loginReturnDto.setTokenInfo(tokenInfo);

            log.info("Success Login : " + email);
            return loginReturnDto;
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 새로운 사용자를 시스템에 등록합니다.
     *
     * @param signupDto 회원가입 정보를 담은 데이터 전송 객체
     * @return 회원가입 성공 여부를 나타내는 문자열
     */
    public String signUp(SignupDto signupDto) {
        try {
            String email = signupDto.getEmail();

            Optional<User> emailCheck = userRepository.findByEmail(email);

            if(emailCheck.isPresent()) {
                log.info("This email already signup");
                return "This email already signup";
            }

            String password_hash = encoder.encode(signupDto.getPassword());
            List<RoleEnum> role = new ArrayList<>(List.of(RoleEnum.ORGANIZER));
            String name = signupDto.getName();
            String nickName = signupDto.getNickName();

            User user = new User(email, password_hash, role, name, nickName);

            userRepository.save(user);

            log.info("Success Signup : " + email);

            return "success";
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 현재 사용자를 로그아웃 처리합니다.
     *
     * @return 로그아웃 성공 여부를 나타내는 문자열
     */
    public String logout() throws Exception {
        try {
            String userEmail = getCurrentMemberEmail();
            Optional<User> user = userRepository.findByEmail(userEmail);

            if(user.isPresent()) {
                user.get().setRefreshToken("");
                userRepository.save(user.get());

                log.info("Success Logout : "+ userEmail);
                return "Success Logout";
            }

            log.error("Failed Logout");

            throw new Exception("Failed Logout");
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 제공된 새로 고침 토큰을 사용하여 토큰을 새로 생성합니다.
     *
     * @param refreshTokenDto 토큰 새로 고침에 필요한 정보를 담은 데이터 전송 객체
     * @return 새로 생성된 토큰 정보
     * @throws Exception 토큰 생성 과정 중 발생한 예외
     */
    public TokenInfo refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
        try {
            if(!jwtTokenProvider.validateToken(refreshTokenDto.getRefreshToken())) {
                throw new Exception("RefreshToken is Invalid");
            }

            Claims claims = jwtTokenProvider.parseClaims(refreshTokenDto.getRefreshToken());

            Optional<User> user = userRepository.findByEmail(claims.getSubject());
            if (user.isEmpty()) {
                throw new Exception("User not found");
            }

            if(!Objects.equals(user.get().getRefreshToken(), refreshTokenDto.getRefreshToken())) {
                throw new Exception("RefreshToken is not allow");
            }

            Collection<? extends GrantedAuthority> authorities = user.get().getAuthorities();
            Collection<GrantedAuthority> newAuthorities = new ArrayList<>();

            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("USER")) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }
                if (authority.getAuthority().equals("ADMIN")) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }
                if (authority.getAuthority().equals("ORGANIZER")) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_ORGANIZER"));
                }
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.get().getUsername(),user.get().getPassword(),newAuthorities);

            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            user.ifPresent(value -> {
                value.setRefreshToken(tokenInfo.getRefreshToken());
                userRepository.save(value);
            });

            return tokenInfo;
        } catch(Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 현재 사용자의 자격 증명을 재검증하여 로그인을 재진행합니다.
     *
     * @return 재로그인에 성공했을 경우 생성된 토큰 정보를 담은 Optional 객체
     * @throws Exception 재로그인 과정 중 발생한 예외
     */
    public String reLogin() throws Exception {
        try{
            Optional<User> user = userRepository.findByEmail(getCurrentMemberEmail());

            if(user.isEmpty()) {
                log.error("Please Refresh Token");
                throw new Exception("Please Refresh Token");
            }

            log.info("relogin : Success");
            return getCurrentMemberEmail();
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }
}
