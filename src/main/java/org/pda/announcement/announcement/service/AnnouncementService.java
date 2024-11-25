package org.pda.announcement.announcement.service;

import org.pda.announcement.announcement.dto.AllAnnouncementsResponse;
import org.pda.announcement.announcement.dto.AnnouncementResponse;
import org.pda.announcement.announcement.dto.ChartAnnouncementResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnouncementService {
    AnnouncementResponse getAnnouncementById(Long announcementId);

    AllAnnouncementsResponse getAnnouncementsByStockId(Long stockId, String sortBy, Pageable pageable);

    AllAnnouncementsResponse getAllAnnouncements(String sortBy, Pageable pageable);

    AllAnnouncementsResponse searchAnnouncements(String keyword, String sortBy, String period, String marketType, String type, Pageable pageable);

    List<ChartAnnouncementResponse> getAnnouncementsGroupedBy(Long stockId, String groupBy);
}
