package org.pda.announcement.announcement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pda.announcement.announcement.domain.AnnouncementType;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AnnouncementResponse {
    private Long stockId;
    private String title;
    private String content;
    private LocalDate announcementDate;
    private String submitter;
    private AnnouncementType announcementType;
    private String originalAnnouncementUrl;
    private int totalVoteCount;
    private int positiveVoteCount;
    private int negativeVoteCount;
    private int commentCount;
}
