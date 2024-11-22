package org.pda.announcement.favoriteannouncement.repository;

import org.pda.announcement.favoriteannouncement.domain.FavoriteAnnouncement;
import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteAnnouncementRepository extends JpaRepository<FavoriteAnnouncement, Long> {

    boolean existsByUserAndAnnouncement(User user, Announcement announcement);

    Optional<FavoriteAnnouncement> findByUserAndAnnouncement(User user, Announcement announcement);

    List<FavoriteAnnouncement> findByUser(User user);
}
