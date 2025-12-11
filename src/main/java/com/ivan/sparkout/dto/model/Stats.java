package com.ivan.sparkout.dto.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Stats {
    int acceptedAnnotations;
    int contributors;
    int iqEarners;
    int transcribers;
    int unreviewedAnnotations;
    int verifiedAnnotations;
    int concurrents;
    boolean hot;
    int pageviews;
}
