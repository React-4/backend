package org.pda.announcement.user.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoinResponse {
    String nickname;
    String email;
    String birthDate;
    String profileColor;
}
