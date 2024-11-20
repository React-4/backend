package org.pda.announcement.comment.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.comment.service.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
}
