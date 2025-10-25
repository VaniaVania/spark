package com.ivan.dropout.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AverageArtistPopularityResponse {
    private String artistName;
    private double averagePopularity;
}
