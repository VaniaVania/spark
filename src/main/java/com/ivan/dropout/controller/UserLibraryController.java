package com.ivan.dropout.controller;

import com.ivan.dropout.dto.AlbumTypePopularityResponse;
import com.ivan.dropout.dto.AverageArtistPopularityResponse;
import com.ivan.dropout.dto.DurationPopularityResponse;
import com.ivan.dropout.dto.SimilarTrackResponse;
import com.ivan.dropout.service.UserLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserLibraryController {

    private final UserLibraryService userLibraryService;

    @GetMapping("/album-popularity")
    public List<AlbumTypePopularityResponse> showAlbumTypePopularity(){
        return userLibraryService.calculateAlbumTypePopularity();
    }

    @GetMapping("/duration-popularity")
    public DurationPopularityResponse showDurationPopularity(@RequestParam long trackDuration) {
        return userLibraryService.calculateDurationPopularity(trackDuration);
    }

    @GetMapping("/artist-popularity")
    public List<AverageArtistPopularityResponse> showAverageArtistPopularity() {
        return userLibraryService.calculateAverageArtistPopularity();
    }

    @GetMapping("/track-titles-analysis")
    public List<SimilarTrackResponse> showSimilarTrackTitles() {
        return userLibraryService.analyzeTrackTitles();
    }
}
