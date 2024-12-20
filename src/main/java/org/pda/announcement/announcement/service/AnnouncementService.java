package org.pda.announcement.announcement.service;

import org.pda.announcement.announcement.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AnnouncementService {
    AnnouncementResponse getAnnouncementById(Long announcementId);

    AllAnnouncementsResponse getAnnouncementsByStockId(Long stockId, String sortBy, Pageable pageable);

    AllAnnouncementsResponse getAllAnnouncements(String sortBy, Pageable pageable);

    AllAnnouncementsResponse searchAnnouncements(String keyword, String sortBy, String period, String marketType, String type, Pageable pageable);

    List<ChartAnnouncementDto> getAnnouncementsGroupedBy(Long stockId, String groupBy);


    List<FavAnnouncementResponse> getAnnouncementsByIds(List<Long> announcementIds);
}
