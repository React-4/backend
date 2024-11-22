package org.pda.announcement.favoriteannouncement.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.favoriteannouncement.service.FavoriteAnnouncementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite/announcement")
public class FavoriteAnnouncementController {

    private final FavoriteAnnouncementService favoriteAnnouncementService;
}
