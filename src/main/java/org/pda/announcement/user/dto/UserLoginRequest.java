package org.pda.announcement.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank(message = "패스워드는 필수 입력 값입니다.")
    public String password;
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    public String email;
}
