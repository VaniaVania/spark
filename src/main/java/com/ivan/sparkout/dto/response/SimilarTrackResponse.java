package com.ivan.sparkout.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SimilarTrackResponse {
    private String trackName;
    private String similarTrackName;
    private String artistName;
    private String similarArtistName;
    private double similarityScore;
}