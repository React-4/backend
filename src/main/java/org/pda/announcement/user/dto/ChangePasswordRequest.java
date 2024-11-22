package org.pda.announcement.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank
    private String currentPassword; // 현재 비밀번호
    @NotBlank
    private String newPassword; // 새 비밀번호
}


