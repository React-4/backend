package org.pda.announcement.announcement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pda.announcement.announcement.dto.AllAnnouncementsResponse;
import org.pda.announcement.announcement.dto.FavAnnouncementResponse;
import org.pda.announcement.announcement.service.AnnouncementService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
@Tag(name = "Announcement API", description = "공시 관련 API")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/{announcement_id}")
    @Operation(summary = "특정 공시 정보 조회", description = "특정 공시 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 공시 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 announcement_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> getAnnouncementById(
            @Parameter(description = "공시 ID", required = true) @PathVariable("announcement_id") Long announcementId) {
        return ResponseEntity.ok(new ApiCustomResponse("특정 공시 정보 조회 성공", announcementService.getAnnouncementById(announcementId)));
    }

    @GetMapping
    @Operation(summary = "전체 공시 목록 조회", description = "전체 공시 목록을 정렬 및 페이징하여 조회합니다. 최신순, 댓글 많은 순, 투표 수 순으로 정렬합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 공시 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 정렬 기준입니다",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> getAllAnnouncements(
            @Parameter(description = "정렬 기준", schema = @Schema(allowableValues = {"latest", "comment", "vote"}, defaultValue = "latest")) @RequestParam(name = "sortBy", defaultValue = "latest") String sortBy,
            @Parameter(description = "페이지 번호", schema = @Schema(defaultValue = "0")) @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", schema = @Schema(defaultValue = "10")) @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        AllAnnouncementsResponse announcements = announcementService.getAllAnnouncements(sortBy, pageable);
        return ResponseEntity.ok(new ApiCustomResponse("전체 공시 목록 조회 성공", announcements));
    }

    @GetMapping("/stock/{stock_id}")
    @Operation(summary = "특정 주식 공시 목록 조회", description = "특정 주식에 대한 공시 목록을 페이징하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 주식 공시 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 stock_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> getAnnouncementsByStockId(
            @Parameter(description = "주식 ID", required = true) @PathVariable("stock_id") Long stockId,
            @Parameter(description = "정렬 기준", schema = @Schema(allowableValues = {"latest", "comment", "vote"}, defaultValue = "latest")) @RequestParam(name = "sortBy", defaultValue = "latest") String sortBy,
            @Parameter(description = "페이지 번호", schema = @Schema(defaultValue = "0")) @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", schema = @Schema(defaultValue = "10")) @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        AllAnnouncementsResponse announcements = announcementService.getAnnouncementsByStockId(stockId, sortBy, pageable);
        return ResponseEntity.ok(new ApiCustomResponse("특정 주식 공시 목록 조회 성공", announcements));
    }

    @GetMapping("/search")
    @Operation(summary = "검색어 공시 목록 조회", description = "검색어에 따라 공시 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색어 주식 공시 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> searchAnnouncements(
            @Parameter(description = "검색어") @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @Parameter(description = "정렬 기준", schema = @Schema(allowableValues = {"latest"}, defaultValue = "latest")) @RequestParam(name = "sortBy", defaultValue = "latest") String sortBy,
            @Parameter(description = "기간", schema = @Schema(allowableValues = {"1m", "6m", "1y", "3y", "2024-11-20~2024-11-20"}, defaultValue = "")) @RequestParam(name = "period", defaultValue = "") String period,
            @Parameter(description = "시장 타입", schema = @Schema(allowableValues = {"KOSPI", "KOSDAQ"}, defaultValue = "")) @RequestParam(name = "marketType", defaultValue = "") String marketType,
            @Parameter(description = "공시 타입", schema = @Schema(allowableValues = {"정기공시", "주요사항보고", "외부감사관련", "발행공시", "지분공시", "자산유동화", "거래소공시", "기타공시", "공정위공시"}, defaultValue = "")) @RequestParam(name = "type", defaultValue = "") String type,
            @Parameter(description = "페이지 번호", schema = @Schema(defaultValue = "0")) @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", schema = @Schema(defaultValue = "10")) @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        AllAnnouncementsResponse announcements = announcementService.searchAnnouncements(keyword, sortBy, period, marketType, type, pageable);
        return ResponseEntity.ok(new ApiCustomResponse("검색어 주식 공시 목록 조회 성공", announcements));
    }

    @GetMapping("/stock/{stock_id}/{groupBy}")
    @Operation(summary = "{stock_id}로 공시 목록 조회 후 groupBy하여 반환", description = "특정 주식 ID로 공시 목록을 조회하고 지정된 기준(day, week, month)으로 그룹화하여 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공시 목록 조회 후 그룹화 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 그룹화 기준",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 stock_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> getAnnouncementsGroupedByDay(
            @Parameter(description = "주식 ID", required = true) @PathVariable("stock_id") Long stockId,
            @Parameter(description = "그룹화 기준 (day, week, month)", required = true) @PathVariable("groupBy") String groupBy) {
        return ResponseEntity.ok(new ApiCustomResponse("공시 목록 조회 후 공시의 date를 " + groupBy + "로 GroupBy하여 반환 성공", announcementService.getAnnouncementsGroupedBy(stockId, groupBy)));
    }

    @PostMapping("/list")
    @Operation(summary = "공시 ID 리스트로 공시 정보 조회", description = "공시 ID 리스트를 이용하여 공시 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공시 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<?> getAnnouncementsByIds(
            @Parameter(description = "공시 ID 리스트", required = true) @RequestBody List<Long> announcementIds) {
        try {
            List<FavAnnouncementResponse> announcements = announcementService.getAnnouncementsByIds(announcementIds);
            return ResponseEntity.ok(new ApiCustomResponse("공시 정보 조회 성공", announcements));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorCustomResponse("유효하지 않은 요청 데이터"));
        }
    }

}