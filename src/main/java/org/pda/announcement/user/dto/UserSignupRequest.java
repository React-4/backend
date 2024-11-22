package org.pda.announcement.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignupRequest {
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    public String nickname;
    @NotBlank(message = "패스워드는 필수 입력 값입니다.")
    public String password;
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    public String email;
    @Size(min = 7, max = 7)
    @NotBlank(message = "생일은 필수 입력 값입니다.")
    public String birthDate;
}
