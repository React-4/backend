package org.pda.announcement.user.service;

import org.pda.announcement.user.dto.UserSignupRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    void signup(UserSignupRequest userSignupRequest);
}
