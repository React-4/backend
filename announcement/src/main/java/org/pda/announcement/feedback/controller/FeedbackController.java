package org.pda.announcement.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.feedback.service.FeedbackService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
}
