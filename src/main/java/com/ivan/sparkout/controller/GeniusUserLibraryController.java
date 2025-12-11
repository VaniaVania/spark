package com.ivan.sparkout.controller;

import com.ivan.sparkout.dto.response.SongAnalyticsResponse;
import com.ivan.sparkout.dto.request.SongComparisonRequest;
import com.ivan.sparkout.dto.response.SongCollaborationResponse;
import com.ivan.sparkout.service.genius.GeniusLibraryCollaborationService;
import com.ivan.sparkout.service.genius.GeniusLibraryAnalysisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/genius")
public class GeniusUserLibraryController {

    GeniusLibraryAnalysisService geniusLibraryAnalysisService;
    GeniusLibraryCollaborationService geniusLibraryCollaborationService;

    @PostMapping("/song/analytics")
    public SongAnalyticsResponse getSongAnalytics(@RequestBody SongComparisonRequest request) {
        return geniusLibraryAnalysisService
                .returnAnalysis(request);
    }

    @PostMapping("/song/collaboration")
    public SongCollaborationResponse getSongCollaborationAnalytics(@RequestBody SongComparisonRequest request) {
        return geniusLibraryCollaborationService.analyzeCollaboration(request);
    }
}
