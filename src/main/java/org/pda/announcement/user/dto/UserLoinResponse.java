package org.pda.announcement.user.dto;


import lombok.Builder;
import lombok.Data;
import org.pda.announcement.favoriteannouncement.dto.FavoriteAnnouncementResponse;
import org.pda.announcement.favoritestock.dto.FavoriteStockResponse;

import java.util.List;

@Data
@Builder
public class UserLoinResponse {
    String nickname;
    String email;
    String birthDate;
    String profileColor;
    List<Long> favoriteAnnouncementIds; // 관심 공시 ID 리스트
    List<Long> favoriteStockIds; // 관심 공시 ID 리스트
}
