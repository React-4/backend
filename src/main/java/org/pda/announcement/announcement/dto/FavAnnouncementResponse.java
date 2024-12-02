package org.pda.announcement.announcement.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavAnnouncementResponse {
    private Long id;          // 공시 ID
    private String title;     // 공시 제목
    private String stockName; // 종목명
    private LocalDate date;      // 날짜
}