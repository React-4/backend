package org.pda.announcement.user.dto;

import lombok.Data;

@Data
public class UserSignupRequest {
    public String nickname;
    public String password;
    public String email;
    public String birthDate;
}
