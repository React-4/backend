package org.pda.announcement.user.service;

import org.pda.announcement.user.dto.UserLoginRequest;
import org.pda.announcement.user.dto.UserLoinResponse;
import org.pda.announcement.user.dto.UserSignupRequest;

public interface UserService {


    void signup(UserSignupRequest userSignupRequest);

    UserLoinResponse login(UserLoginRequest userLoginRequest);
}
