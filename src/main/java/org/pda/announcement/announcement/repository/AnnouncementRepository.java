package org.pda.announcement.announcement.repository;

import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.announcement.domain.AnnouncementType;
import org.pda.announcement.stock.domain.MarketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, AnnouncementRepositoryCustom {

    List<Announcement> findByStockId(Long stockId);

    Page<Announcement> findByStockId(Long stockId, Pageable pageable);

    @Query("SELECT a FROM Announcement a LEFT JOIN a.comments c GROUP BY a ORDER BY COUNT(c) DESC")
    Page<Announcement> findAllOrderByCommentCount(Pageable pageable);

    @Query("SELECT a FROM Announcement a LEFT JOIN a.feedbacks f GROUP BY a ORDER BY COUNT(f) DESC")
    Page<Announcement> findAllOrderByFeedbackCount(Pageable pageable);

    @Query("SELECT a FROM Announcement a LEFT JOIN a.comments c WHERE a.stock.id = :stockId GROUP BY a ORDER BY COUNT(c) DESC")
    Page<Announcement> findByStockIdOrderByCommentCount(@Param("stockId") Long stockId, Pageable pageable);

    @Query("SELECT a FROM Announcement a LEFT JOIN a.feedbacks f WHERE a.stock.id = :stockId GROUP BY a ORDER BY COUNT(f) DESC")
    Page<Announcement> findByStockIdOrderByFeedbackCount(@Param("stockId") Long stockId, Pageable pageable);


    @Query("SELECT a FROM Announcement a WHERE " +
            "(:keyword IS NULL OR a.stock.companyName LIKE %:keyword%) AND " +
            "(:marketType IS NULL OR a.stock.marketType = :marketType) AND " +
            "(:type IS NULL OR a.announcementType = :type) AND " +
            "a.announcementDate BETWEEN :startDate AND :endDate")
    Page<Announcement> searchAnnouncements(@Param("keyword") String keyword,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate,
                                           @Param("marketType") MarketType marketType,
                                           @Param("type") AnnouncementType type,
                                           Pageable pageable);

    @Query("SELECT a FROM Announcement a WHERE a.stock.id = :stockId GROUP BY a.announcementDate")
    List<Announcement> findAnnouncementsGroupedByDay(@Param("stockId") Long stockId);

    @Query("SELECT FUNCTION('DATE_FORMAT', a.announcementDate, '%Y-%m') AS month " +
            "FROM Announcement a " +
            "GROUP BY month " +
            "ORDER BY FUNCTION('DATE_FORMAT', a.announcementDate, '%Y-%m') DESC")
    List<Announcement> findAnnouncementsGroupedByMonth(@Param("stockId") Long stockId);

    @Query("SELECT FUNCTION('YEARWEEK', a.announcementDate, 1) AS week " +
            "FROM Announcement a " +
            "WHERE a.stock.id = :stockId " +
            "GROUP BY week " +
            "ORDER BY FUNCTION('YEARWEEK', a.announcementDate, 1) DESC")
    List<Announcement> findAnnouncementsGroupedByWeek(@Param("stockId") Long stockId);

}
