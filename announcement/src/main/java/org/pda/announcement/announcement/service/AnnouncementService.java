package org.pda.announcement.announcement.service;

import org.pda.announcement.announcement.dto.AnnouncementResponse;

public interface AnnouncementService {
    AnnouncementResponse getAnnouncementById(Long announcementId);
}
