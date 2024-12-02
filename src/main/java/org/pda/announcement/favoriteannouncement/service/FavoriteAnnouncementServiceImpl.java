package org.pda.announcement.favoriteannouncement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.favoriteannouncement.domain.FavoriteAnnouncement;
import org.pda.announcement.favoriteannouncement.dto.FavoriteAnnouncementResponse;
import org.pda.announcement.favoriteannouncement.repository.FavoriteAnnouncementRepository;
import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.announcement.repository.AnnouncementRepository;
import org.pda.announcement.user.domain.User;
import org.pda.announcement.user.repository.UserRepository;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteAnnouncementServiceImpl implements FavoriteAnnouncementService {

    private final FavoriteAnnouncementRepository favoriteAnnouncementRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void addFavoriteAnnouncement(Long announcementId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 announcement_id"));

        if (favoriteAnnouncementRepository.existsByUserAndAnnouncement(user, announcement)) {
            throw new IllegalArgumentException("이미 관심 공시에 추가됨");
        }

        FavoriteAnnouncement favoriteAnnouncement = FavoriteAnnouncement.builder()
                .user(user)
                .announcement(announcement)
                .build();

        favoriteAnnouncementRepository.save(favoriteAnnouncement);
    }

    @Override
    @Transactional
    public void removeFavoriteAnnouncement(Long announcementId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 announcement_id"));

        FavoriteAnnouncement favoriteAnnouncement = favoriteAnnouncementRepository.findByUserAndAnnouncement(user, announcement)
                .orElseThrow(() -> new IllegalArgumentException("관심 공시가 눌리지 않았습니다"));

        favoriteAnnouncementRepository.delete(favoriteAnnouncement);
    }

    @Override
    public List<FavoriteAnnouncementResponse> getFavoriteAnnouncementList(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        List<FavoriteAnnouncement> favoriteAnnouncements = favoriteAnnouncementRepository.findByUser(user);

        return favoriteAnnouncements.stream()
                .map(favoriteAnnouncement -> new FavoriteAnnouncementResponse(
                        favoriteAnnouncement.getAnnouncement().getAnnouncementId(),
                        favoriteAnnouncement.getAnnouncement().getTitle(),
                        favoriteAnnouncement.getAnnouncement().getAnnouncementDate(),
                        favoriteAnnouncement.getAnnouncement().getSubmitter()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavoriteAnnouncement(Long announcementId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 announcement_id"));

        return favoriteAnnouncementRepository.existsByUserAndAnnouncement(user, announcement);
    }
    @Override
    public List<Long> getFavoriteAnnouncementId(String userEmail) {
        // 관심 종목 ID 리스트에 해당하는 공시 ID 리스트 반환
        return favoriteAnnouncementRepository.findAnnouncementIdsByUserEmail(userEmail);
    }

}
