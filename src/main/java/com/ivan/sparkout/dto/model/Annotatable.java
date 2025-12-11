package com.ivan.sparkout.dto.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Annotatable {
    String apiPath;
    ClientTimestamp clientTimestamps;
    String context;
    int id;
    String imageUrl;
    String linkTitle;
    String title;
    String type;
    String url;
}
