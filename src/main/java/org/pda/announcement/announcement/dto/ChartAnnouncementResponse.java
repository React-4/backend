package org.pda.announcement.announcement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ChartAnnouncementResponse {
    private LocalDate announcementDate;
    private Long announcementId;
    private String title;
}
