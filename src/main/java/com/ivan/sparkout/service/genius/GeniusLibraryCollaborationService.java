package com.ivan.sparkout.service.genius;

import com.ivan.sparkout.dto.request.SongComparisonRequest;
import com.ivan.sparkout.dto.response.SongCollaborationResponse;

public interface GeniusLibraryCollaborationService {
    SongCollaborationResponse analyzeCollaboration(SongComparisonRequest request);
}
