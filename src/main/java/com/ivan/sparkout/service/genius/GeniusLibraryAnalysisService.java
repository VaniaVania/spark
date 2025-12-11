package com.ivan.sparkout.service.genius;

import com.ivan.sparkout.dto.response.SongAnalyticsResponse;
import com.ivan.sparkout.dto.request.SongComparisonRequest;

public interface GeniusLibraryAnalysisService {
    SongAnalyticsResponse returnAnalysis(SongComparisonRequest request);
}
