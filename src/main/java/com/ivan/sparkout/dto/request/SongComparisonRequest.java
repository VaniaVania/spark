package com.ivan.sparkout.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SongComparisonRequest {
    private SongInformationRequest firstSong;
    private SongInformationRequest secondSong;
}
