package org.pda.announcement.announcement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.announcement.domain.AnnouncementType;
import org.pda.announcement.announcement.dto.*;
import org.pda.announcement.announcement.repository.AnnouncementRepository;
import org.pda.announcement.comment.repository.CommentRepository;
import org.pda.announcement.exception.GlobalCustomException.AnnouncementNotFoundException;
import org.pda.announcement.feedback.domain.FeedbackType;
import org.pda.announcement.feedback.repository.FeedbackRepository;
import org.pda.announcement.stock.domain.MarketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CommentRepository commentRepository;
    private final FeedbackRepository feedbackRepository;

    private static List<ChartAnnouncementDto> getChartAnnouncementDtos(List<Object[]> announcements) {
        Map<String, List<ChartAnnouncement>> groupedData = new HashMap<>();

        // 공시 데이터를 날짜별로 그룹화합니다.
        for (Object[] announcement : announcements) {
            String date = announcement[0].toString(); // 주의 시작 날짜
            Long announcementId = (Long) announcement[1]; // 공시 ID
            String title = (String) announcement[2]; // 공시 제목

            // AnnouncementDTO로 변환
            ChartAnnouncement announcementDTO = new ChartAnnouncement(announcementId, title.trim());

            // 날짜별로 데이터를 그룹화합니다.
            groupedData.computeIfAbsent(date, k -> new ArrayList<>()).add(announcementDTO);

        }

        // 최종 결과 리스트 생성
        List<ChartAnnouncementDto> response = new ArrayList<>();
        for (Map.Entry<String, List<ChartAnnouncement>> entry : groupedData.entrySet()) {
            ChartAnnouncementDto weekData = new ChartAnnouncementDto(entry.getKey(), entry.getValue());
            response.add(weekData);
        }

        return response;
    }

    @Override
    public AnnouncementResponse getAnnouncementById(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementNotFoundException("유효하지 않은 announcement_id"));

        int commentCount = commentRepository.countByAnnouncement(announcement);
        int positiveVoteCount = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.POSITIVE);
        int negativeVoteCount = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.NEGATIVE);

        return new AnnouncementResponse(
                announcement.getStock().getId(),
                announcement.getTitle(),
                announcement.getContent(),
                announcement.getAnnouncementDate(),
                announcement.getSubmitter(),
                announcement.getAnnouncementType(),
                announcement.getOriginalAnnouncementUrl(),
                positiveVoteCount + negativeVoteCount,
                positiveVoteCount,
                negativeVoteCount,
                commentCount
        );
    }

    @Override
    public AllAnnouncementsResponse getAnnouncementsByStockId(Long stockId, String sortBy, Pageable pageable) {
        Page<Announcement> announcements = switch (sortBy) {
            case "latest" ->
                    announcementRepository.findByStockId(stockId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "announcementDate")));
            case "comment" ->
                    announcementRepository.findByStockIdOrderByCommentCount(stockId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
            case "vote" ->
                    announcementRepository.findByStockIdOrderByFeedbackCount(stockId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
            default -> throw new IllegalArgumentException("Invalid sort criteria: " + sortBy);
        };

        return getAllAnnouncementsResponse(announcements);
    }

    @Override
    public AllAnnouncementsResponse getAllAnnouncements(String sortBy, Pageable pageable) {
        Page<Announcement> announcements = switch (sortBy) {
            case "latest" ->
                    announcementRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "announcementDate")));
            case "comment" ->
                    announcementRepository.findAllOrderByCommentCount(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
            case "vote" ->
                    announcementRepository.findAllOrderByFeedbackCount(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
            default -> throw new IllegalArgumentException("Invalid sort criteria: " + sortBy);
        };

        return getAllAnnouncementsResponse(announcements);
    }

    @Override
    public AllAnnouncementsResponse searchAnnouncements(String keyword, String sortBy, String period, String marketType, String type, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementDate");

        LocalDate startDate = LocalDate.MIN;
        LocalDate endDate = LocalDate.now();
        if (!period.isEmpty()) {
            switch (period) {
                case "1m" -> startDate = endDate.minusMonths(1);
                case "6m" -> startDate = endDate.minusMonths(6);
                case "1y" -> startDate = endDate.minusYears(1);
                case "3y" -> startDate = endDate.minusYears(3);
                default -> {
                    if (period.contains("~")) {
                        String[] dates = period.split("~");
                        startDate = LocalDate.parse(dates[0], DateTimeFormatter.ISO_DATE);
                        endDate = LocalDate.parse(dates[1], DateTimeFormatter.ISO_DATE);
                    }
                }
            }
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Announcement> announcements = announcementRepository.searchAnnouncements(
                keyword.isEmpty() ? null : keyword,
                startDate,
                endDate,
                marketType.isEmpty() ? null : MarketType.valueOf(marketType),
                type.isEmpty() ? null : AnnouncementType.valueOf(type),
                sortedPageable
        );

        return getAllAnnouncementsResponse(announcements);
    }

    @Override
    public List<ChartAnnouncementDto> getAnnouncementsGroupedBy(Long stockId, String groupBy) {
        switch (groupBy) {
            case "day" -> {
                List<Object[]> announcementsByDay = announcementRepository.findAnnouncementsGroupedByDay(stockId);
                return getChartAnnouncementDtos(announcementsByDay);
            }
            case "week" -> {
                List<Object[]> announcementsByWeek = announcementRepository.findAnnouncementsByWeek(stockId);
                return getChartAnnouncementDtos(announcementsByWeek);
            }
            case "month" -> {
                List<Object[]> announcementsByWeek = announcementRepository.findAnnouncementsByMonth(stockId);
                return getChartAnnouncementDtos(announcementsByWeek);
            }
            default -> throw new IllegalArgumentException("Invalid group by criteria: " + groupBy);
        }
    }

    private AllAnnouncementsResponse getAllAnnouncementsResponse(Page<Announcement> announcements) {
        List<AllAnnouncementResponse> announcementList = announcements.stream().map(announcement -> {
            int commentCount = commentRepository.countByAnnouncement(announcement);
            int positiveVoteCount = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.POSITIVE);
            int negativeVoteCount = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.NEGATIVE);

            return new AllAnnouncementResponse(
                    announcement.getAnnouncementId(),
                    announcement.getTitle(),
                    announcement.getStock().getCompanyName(),
                    announcement.getStock().getTicker(),
                    announcement.getAnnouncementDate(),
                    announcement.getSubmitter(),
                    positiveVoteCount + negativeVoteCount,
                    positiveVoteCount,
                    negativeVoteCount,
                    commentCount
            );
        }).collect(Collectors.toList());

        return new AllAnnouncementsResponse(announcements.getTotalPages(), announcementList);
    }


    @Override
    public List<FavAnnouncementResponse> getAnnouncementsByIds(List<Long> announcementIds) {
        List<Announcement> announcements = announcementRepository.findAllById(announcementIds);

        return announcements.stream().map(announcement -> new FavAnnouncementResponse(
                announcement.getAnnouncementId(),
                announcement.getTitle(),
                announcement.getStock().getCompanyName(),
                announcement.getAnnouncementDate()
        )).collect(Collectors.toList());
    }

}