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
public class Song {
    int annotationCount;
    String apiPath;
    String appleMusicId;
    String appleMusicPlayerUrl;
    String artistNames;
    String fullTitle;
    String headerImageThumbnailUrl;
    String headerImageUrl;
    int id;
    String language;
    int lyricsOwnerId;
    String lyricsState;
    String path;
    String primaryArtistNames;
    int pyongsCount;
    String recordingLocation;
    String relationshipsIndexUrl;
    String releaseDate;
    String releaseDateForDisplay;
    String releaseDateWithAbbreviatedMonthForDisplay;
    String songArtImageThumbnailUrl;
    String songArtImageUrl;
    Stats stats;
    String title;
    String titleWithFeatured;
    String url;
    String songArtPrimaryColor;
    String songArtSecondaryColor;
    String songArtTextColor;
    Album album;
    List<CustomPerformance> customPerformances;
    DescriptionAnnotation descriptionAnnotation;
    List<Artist> featuredArtists;
    String lyricsMarkedCompleteBy;
    String lyricsMarkedStaffApprovedBy;
    List<Media> media;
    Artist primaryArtist;
    List<Artist> primaryArtists;
    List<Artist> producerArtists;
    List<SongRelationship> songRelationships;
    /*List<String> translationSongs;
    List<String> verifiedAnnotationsBy;
    List<String> verifiedContributors;
    List<String> verifiedLyricsBy;*/
    List<Artist> writerArtists;
}
