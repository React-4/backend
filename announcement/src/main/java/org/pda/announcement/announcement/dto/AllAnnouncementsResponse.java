package org.pda.announcement.announcement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AllAnnouncementsResponse {
    private int announcementCount;
    private List<AllAnnouncementResponse> announcementList;
}
