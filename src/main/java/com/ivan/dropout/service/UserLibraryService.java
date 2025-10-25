package com.ivan.dropout.service;

import com.ivan.dropout.dto.AlbumTypePopularityResponse;
import com.ivan.dropout.dto.AverageArtistPopularityResponse;
import com.ivan.dropout.dto.DurationPopularityResponse;
import com.ivan.dropout.dto.SimilarTrackResponse;

import java.util.List;

public interface UserLibraryService {

    List<AlbumTypePopularityResponse> calculateAlbumTypePopularity();

    DurationPopularityResponse calculateDurationPopularity(long trackDuration);

    List<AverageArtistPopularityResponse> calculateAverageArtistPopularity();

    List<SimilarTrackResponse> analyzeTrackTitles();
}
