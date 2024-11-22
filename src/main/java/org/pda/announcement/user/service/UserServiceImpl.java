package org.pda.announcement.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.exception.GlobalCustomException.DuplicateFieldException;
import org.pda.announcement.exception.GlobalCustomException.InvalidCredentialsException;
import org.pda.announcement.exception.GlobalCustomException.UserNotFoundException;
import org.pda.announcement.user.domain.User;
import org.pda.announcement.user.dto.*;
import org.pda.announcement.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

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

    @Override
    @Transactional
    public UpdateNicknameResponse updateNickname(UpdateNicknameRequest request, String email) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        // 새로운 닉네임이 이미 사용 중인지 확인
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new DuplicateFieldException("닉네임이 이미 사용 중입니다.");
        }

        // 닉네임 업데이트
        user.updateNickname(request.getNickname());
        userRepository.save(user); // 변경 사항 저장

        // 응답 생성
        return new UpdateNicknameResponse(user.getNickname());
    }

    @Override
    @Transactional
    public void updatePassword(ChangePasswordRequest request, String email) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("유효하지 않은 사용자입니다."));

        // 현재 비밀번호 확인
        if (!encodePwd.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호로 변경
        user.updatePassword(encodePwd.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(DeleteUserRequest request, String email) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("유효하지 않은 사용자입니다."));

        // 비밀번호 확인
        if (!encodePwd.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 사용자 삭제
        userRepository.delete(user);
    }
}
