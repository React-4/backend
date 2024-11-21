package org.pda.announcement.user.service;

import org.pda.announcement.user.dto.*;

public interface UserService {

    void signup(UserSignupRequest userSignupRequest);

    UserLoinResponse login(UserLoginRequest userLoginRequest);

    UpdateNicknameResponse updateNickname(UpdateNicknameRequest request, String email);

    void updatePassword(ChangePasswordRequest request, String email);

    void deleteUser(DeleteUserRequest request, String email);
}
