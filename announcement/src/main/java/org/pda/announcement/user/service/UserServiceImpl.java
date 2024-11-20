package org.pda.announcement.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.user.domain.User;
import org.pda.announcement.user.dto.UserSignupRequest;
import org.pda.announcement.user.exception.CustomExceptions.DuplicateFieldException;
import org.pda.announcement.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encodePwd;

    @Override
    public ResponseEntity<?> signup(UserSignupRequest userSignupRequest) {
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
                .build();

        userRepository.save(user);

        return null;
    }
}
