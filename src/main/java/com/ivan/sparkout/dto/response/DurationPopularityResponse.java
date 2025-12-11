package com.ivan.sparkout.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DurationPopularityResponse {

    private long trackDurationCount;
    private double correlationDurationPopularity;
}
