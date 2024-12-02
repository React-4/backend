package org.pda.announcement.favoriteannouncement.service;

import org.pda.announcement.favoriteannouncement.dto.FavoriteAnnouncementResponse;

import java.util.List;

public interface FavoriteAnnouncementService {

    void addFavoriteAnnouncement(Long announcementId, String email);

    void removeFavoriteAnnouncement(Long announcementId, String email);

    List<FavoriteAnnouncementResponse> getFavoriteAnnouncementList(String email);
    boolean isFavoriteAnnouncement(Long announcementId, String email);
    List<Long> getFavoriteAnnouncementId(String userEmail);

}
