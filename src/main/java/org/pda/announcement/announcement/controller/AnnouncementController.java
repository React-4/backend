package org.pda.announcement.announcement.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.announcement.dto.AllAnnouncementsResponse;
import org.pda.announcement.announcement.service.AnnouncementService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/{announcement_id}")
    public ResponseEntity<ApiCustomResponse> getAnnouncementById(@PathVariable("announcement_id") Long announcementId) {
        return ResponseEntity.ok(new ApiCustomResponse("특정 공시 정보 조회 성공", announcementService.getAnnouncementById(announcementId)));
    }

    @GetMapping
    public ResponseEntity<ApiCustomResponse> getAllAnnouncements(
            @RequestParam(name = "sortBy", defaultValue = "latest") String sortBy,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        AllAnnouncementsResponse announcements = announcementService.getAllAnnouncements(sortBy, pageable);
        return ResponseEntity.ok(new ApiCustomResponse("전체 공시 목록 조회 성공", announcements));
    }

    @GetMapping("/stock/{stock_id}")
    public ResponseEntity<ApiCustomResponse> getAnnouncementsByStockId(
            @PathVariable("stock_id") Long stockId,
            @RequestParam(name = "sortBy", defaultValue = "latest") String sortBy,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        AllAnnouncementsResponse announcements = announcementService.getAnnouncementsByStockId(stockId, sortBy, pageable);
        return ResponseEntity.ok(new ApiCustomResponse("특정 주식 공시 목록 조회 성공", announcements));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiCustomResponse> searchAnnouncements(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "latest") String sortBy,
            @RequestParam(name = "period", defaultValue = "") String period,
            @RequestParam(name = "marketType", defaultValue = "") String marketType,
            @RequestParam(name = "type", defaultValue = "") String type,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        AllAnnouncementsResponse announcements = announcementService.searchAnnouncements(keyword, sortBy, period, marketType, type, pageable);
        return ResponseEntity.ok(new ApiCustomResponse("검색어 주식 공시 목록 조회 성공", announcements));
    }
}
