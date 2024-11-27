package org.pda.announcement.announcement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartAnnouncementDto {
    private String date; // 주의 시작 날짜
    private List<ChartAnnouncement> announcementList; // 해당 주의 공시 목록
}
