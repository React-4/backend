package org.pda.announcement.announcement.service;

import org.pda.announcement.announcement.dto.AllAnnouncementsResponse;
import org.pda.announcement.announcement.dto.AnnouncementResponse;
import org.springframework.data.domain.Pageable;

public interface AnnouncementService {
    AnnouncementResponse getAnnouncementById(Long announcementId);

    AllAnnouncementsResponse getAllAnnouncements(String sortBy, Pageable pageable);
}
