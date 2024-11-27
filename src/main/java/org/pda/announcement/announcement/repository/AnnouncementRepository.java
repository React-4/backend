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

    @Query(value = "SELECT " +
            "DATE_FORMAT(a.announcement_date, '%Y-%m-%d') AS day, " + // 연도-월-일 형식으로 변환
            "a.announcement_id AS announcementId, " + // 공시 ID
            "a.title AS title " + // 공시 제목
            "FROM announcement a " +
            "WHERE a.stock_id = :stockId " + // stockId 필터 추가
            "ORDER BY a.announcement_date DESC", // 날짜 기준으로 정렬
            nativeQuery = true)
    List<Object[]> findAnnouncementsGroupedByDay(@Param("stockId") Long stockId);


    @Query(value = "SELECT " +
            "DATE_FORMAT(a.announcement_date, '%Y-%m') AS month, " + // 연도-월 형식으로 변환
            "a.announcement_id AS announcementId, " + // 공시 ID
            "a.title AS title " + // 공시 제목
            "FROM announcement a " +
            "WHERE a.stock_id = :stockId " + // stockId 필터 추가
            "ORDER BY month DESC, a.announcement_date DESC", // 월 기준으로 정렬
            nativeQuery = true)
    List<Object[]> findAnnouncementsByMonth(@Param("stockId") Long stockId);


    @Query(value = "SELECT " +
            "DATE_SUB(a.announcement_date, INTERVAL (WEEKDAY(a.announcement_date)) DAY) AS week, " +
            "a.announcement_id AS announcementId, " +
            "a.title AS title " +
            "FROM announcement a " +
            "WHERE a.stock_id = :stockId " +
            "ORDER BY week DESC, a.announcement_date DESC",
            nativeQuery = true)
    List<Object[]> findAnnouncementsByWeek(@Param("stockId") Long stockId);
}
