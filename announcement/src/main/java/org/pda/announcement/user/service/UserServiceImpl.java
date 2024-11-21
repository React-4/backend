package org.pda.announcement.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.user.domain.User;
import org.pda.announcement.user.dto.UserLoginRequest;
import org.pda.announcement.user.dto.UserLoinResponse;
import org.pda.announcement.user.dto.UserSignupRequest;
import org.pda.announcement.user.exception.CustomExceptions.DuplicateFieldException;
import org.pda.announcement.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

import static org.pda.announcement.user.exception.CustomExceptions.InvalidCredentialsException;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder encodePwd;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void signup(UserSignupRequest userSignupRequest) {
        if (userRepository.findByNickname(userSignupRequest.getNickname()).isPresent()
                || userRepository.findByEmail(userSignupRequest.getEmail()).isPresent()) {
            throw new DuplicateFieldException("닉네임 또는 이메일 중복");
        }

        // 사용자 정보 저장
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .nickname(userSignupRequest.getNickname())
                .passwordHash(encodePwd.encode(userSignupRequest.getPassword()))
                .email(userSignupRequest.getEmail())
                .birthDate(userSignupRequest.getBirthDate())
                .profileColor(String.valueOf(new Random().nextInt(10)))
                .build();
        userRepository.save(user);
        userRepository.flush();
    }

    @Override
    public UserLoinResponse login(UserLoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("잘못된 이메일"));

        if (!encodePwd.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("잘못된 비밀번호");
        }

        return UserLoinResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .profileColor(user.getProfileColor())
                .build();
    }
}
