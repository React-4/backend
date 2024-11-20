package org.pda.announcement.announcement.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.announcement.service.AnnouncementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;
}
