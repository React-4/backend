package org.pda.announcement.favoriteannouncement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.favoriteannouncement.repository.FavoriteAnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteAnnouncementServiceImpl implements FavoriteAnnouncementService {

    private final FavoriteAnnouncementRepository favoriteAnnouncementRepository;

}
