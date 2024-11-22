package org.pda.announcement.announcement.repository;

import org.pda.announcement.announcement.domain.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, AnnouncementRepositoryCustom {

    List<Announcement> findByStockId(Long stockId);
}
