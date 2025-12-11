package com.ivan.sparkout.service.spotify;

import com.ivan.sparkout.dto.response.AlbumTypePopularityResponse;
import com.ivan.sparkout.dto.response.AverageArtistPopularityResponse;
import com.ivan.sparkout.dto.response.DurationPopularityResponse;
import com.ivan.sparkout.dto.response.SimilarTrackResponse;

import java.util.List;

public interface SpotifyLibraryAnalysisService {

    List<AlbumTypePopularityResponse> calculateAlbumTypePopularity();

    DurationPopularityResponse calculateDurationPopularity(long trackDuration);

    List<AverageArtistPopularityResponse> calculateAverageArtistPopularity();

    List<SimilarTrackResponse> analyzeTrackTitles();
}
