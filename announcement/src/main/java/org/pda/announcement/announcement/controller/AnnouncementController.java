package org.pda.announcement.announcement.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.announcement.service.AnnouncementService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/{announcement_id}")
    public ResponseEntity<ApiCustomResponse> getAnnouncementById(@PathVariable("announcement_id") Long announcementId) {
        return ResponseEntity.ok(new ApiCustomResponse("특정 공시 정보 조회 성공", announcementService.getAnnouncementById(announcementId)));
    }
}
