package org.pda.announcement.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNicknameRequest {
    @NotBlank
    private String nickname; // 수정할 닉네임
}

