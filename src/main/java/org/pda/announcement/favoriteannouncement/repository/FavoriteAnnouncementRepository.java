package org.pda.announcement.favoriteannouncement.repository;

import org.pda.announcement.favoriteannouncement.domain.FavoriteAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteAnnouncementRepository extends JpaRepository<FavoriteAnnouncement, Long>, FavoriteAnnouncementRepositoryCustom {

}
