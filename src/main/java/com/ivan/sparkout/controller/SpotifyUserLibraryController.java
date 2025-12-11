package com.ivan.sparkout.controller;

import com.ivan.sparkout.dto.response.AlbumTypePopularityResponse;
import com.ivan.sparkout.dto.response.AverageArtistPopularityResponse;
import com.ivan.sparkout.dto.response.DurationPopularityResponse;
import com.ivan.sparkout.dto.response.SimilarTrackResponse;
import com.ivan.sparkout.service.spotify.SpotifyLibraryAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spotify/user")
public class SpotifyUserLibraryController {

    private final SpotifyLibraryAnalysisService spotifyLibraryAnalysisService;

    @GetMapping("/album-popularity")
    public List<AlbumTypePopularityResponse> showAlbumTypePopularity(){
        return spotifyLibraryAnalysisService.calculateAlbumTypePopularity();
    }

    @GetMapping("/artist-popularity")
    public List<AverageArtistPopularityResponse> showAverageArtistPopularity() {
        return spotifyLibraryAnalysisService.calculateAverageArtistPopularity();
    }

    @GetMapping("/duration-popularity")
    public DurationPopularityResponse showDurationPopularity(@RequestParam long trackDuration) {
        return spotifyLibraryAnalysisService.calculateDurationPopularity(trackDuration);
    }

    @GetMapping("/track-titles-analysis")
    public List<SimilarTrackResponse> showSimilarTrackTitles() {
        return spotifyLibraryAnalysisService.analyzeTrackTitles();
    }
}
