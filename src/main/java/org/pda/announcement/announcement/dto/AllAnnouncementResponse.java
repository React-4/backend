package org.pda.announcement.announcement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AllAnnouncementResponse {
    private Long announcementId;
    private String title;
    private String stockName;
    private LocalDate announcementDate;
    private String submitter;
    private int totalVoteCount;
    private int positiveVoteCount;
    private int negativeVoteCount;
    private int commentCount;
}
