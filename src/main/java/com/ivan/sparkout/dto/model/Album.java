package com.ivan.sparkout.dto.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Album {
    String apiPath;
    String coverArtUrl;
    String fullTitle;
    int id;
    String name;
    String primaryArtistNames;
    String releaseDateForDisplay;
    String url;
    Artist artist;
    List<Artist> primaryArtists;
}
