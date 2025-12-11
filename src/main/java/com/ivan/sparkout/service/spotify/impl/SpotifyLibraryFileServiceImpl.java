package com.ivan.sparkout.service.spotify.impl;

import com.ivan.sparkout.service.spotify.SpotifyLibraryFileService;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.apache.spark.sql.functions.col;

@Service
@RequiredArgsConstructor
public class SpotifyLibraryFileServiceImpl implements SpotifyLibraryFileService {

    @Value(value = "${dropout.analysis.file.path}")
    private String analysisFilePath;

    private final SparkSession sparkSession;

    @Override
    public Dataset<Row> returnAnalysisFile() {
        return sparkSession.read()
                .option("multiline", true)
                .json(analysisFilePath)
                .withColumn("album_type", col("track.album.album_type"))
                .withColumn("artists_external_urls_spotify", col("track.album.artists.external_urls.spotify"))
                .withColumn("artists_href", col("track.album.artists.href"))
                .withColumn("artists_id", col("track.album.artists.id"))
                .withColumn("name", col("track.album.artists.name"))
                .withColumn("type", col("track.album.artists.type"))
                .withColumn("uri", col("track.album.artists.uri"))
                .withColumn("available_markets", col("track.album.available_markets"))
                .withColumn("album_external_urls", col("track.album.external_urls.spotify"))
                .withColumn("album_href", col("track.album.href"))
                .withColumn("album_id", col("track.album.id"))
                .withColumn("album_images", col("track.album.images"))
                .withColumn("album_is_playable", col("track.album.is_playable"))
                .withColumn("album_name", col("track.album.name"))
                .withColumn("album_release_date", col("track.album.release_date"))
                .withColumn("album_release_date_precision", col("track.album.release_date_precision"))
                .withColumn("album_total_tracks", col("track.album.total_tracks"))
                .withColumn("album_internal_type", col("track.album.type"))
                .withColumn("album_uri", col("track.album.uri"))
                .withColumn("artist_external_urls", col("track.artists.external_urls.spotify"))
                .withColumn("artist_href", col("track.artists.href"))
                .withColumn("artist_id", col("track.artists.id"))
                .withColumn("artist_name", col("track.artists.name"))
                .withColumn("artist_type", col("track.artists.type"))
                .withColumn("artist_uri", col("track.artists.uri"))
                .withColumn("track_available_markets", col("track.available_markets"))
                .withColumn("track_disc_number", col("track.disc_number"))
                .withColumn("track_duration_ms", col("track.duration_ms"))
                .withColumn("track_external_ids_isrc", col("track.external_ids.isrc"))
                .withColumn("track_external_urls", col("track.external_urls.spotify"))
                .withColumn("track_href", col("track.href"))
                .withColumn("track_id", col("track.id"))
                .withColumn("track_is_local", col("track.is_local"))
                .withColumn("track_is_playable", col("track.is_playable"))
                .withColumn("track_name", col("track.name"))
                .withColumn("track_popularity", col("track.popularity"))
                .withColumn("track_preview_url", col("track.preview_url"))
                .withColumn("track_track_number", col("track.track_number"))
                .withColumn("track_type", col("track.type"))
                .withColumn("track_uri", col("track.uri"));
    }

    @Override
    public String returnSchema() {
        return returnAnalysisFile()
                .schema()
                .prettyJson();
    }
}
