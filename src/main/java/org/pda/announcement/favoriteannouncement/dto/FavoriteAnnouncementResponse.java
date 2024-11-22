package org.pda.announcement.favoriteannouncement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FavoriteAnnouncementResponse {
    private Long announcementId;
    private String title;
    private LocalDate announcementDate;
    private String submitter;
}
