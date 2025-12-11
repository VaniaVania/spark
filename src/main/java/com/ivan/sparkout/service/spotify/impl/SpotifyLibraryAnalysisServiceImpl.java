package com.ivan.sparkout.service.spotify.impl;

import com.ivan.sparkout.dto.response.AlbumTypePopularityResponse;
import com.ivan.sparkout.dto.response.AverageArtistPopularityResponse;
import com.ivan.sparkout.dto.response.DurationPopularityResponse;
import com.ivan.sparkout.dto.response.SimilarTrackResponse;
import com.ivan.sparkout.service.spotify.SpotifyLibraryFileService;
import com.ivan.sparkout.service.spotify.SpotifyLibraryAnalysisService;
import lombok.RequiredArgsConstructor;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SpotifyLibraryAnalysisServiceImpl implements SpotifyLibraryAnalysisService {

    private final SpotifyLibraryFileService spotifyLibraryFileService;

    @Override
    public List<AlbumTypePopularityResponse> calculateAlbumTypePopularity() {
        Dataset<Row> result = spotifyLibraryFileService.returnAnalysisFile()
                .groupBy("album_type")
                .agg(functions.avg("track_popularity").alias("average_popularity"))
                .orderBy(functions.desc("average_popularity"));

        return result.collectAsList().stream()
                .map(row -> AlbumTypePopularityResponse.builder()
                        .albumType(row.getAs("album_type").toString())
                        .popularity(String.format("%.2f", row.getDouble(1)))
                        .build())
                .toList();
    }

    @Override
    public DurationPopularityResponse calculateDurationPopularity(long trackDuration) {
        Dataset<Row> analysisFile = spotifyLibraryFileService.returnAnalysisFile();
        Dataset<Row> filteredTracks = analysisFile
                .filter(functions.col("track_duration_ms").gt(TimeUnit.SECONDS.toMillis(trackDuration)))
                .select("track_duration_ms", "track_popularity");

        long count = filteredTracks.count();
        double correlation = filteredTracks.stat().corr("track_duration_ms", "track_popularity");

        return DurationPopularityResponse.builder()
                .trackDurationCount(count)
                .correlationDurationPopularity(correlation)
                .build();
    }

    @Override
    public List<AverageArtistPopularityResponse> calculateAverageArtistPopularity() {
        Dataset<Row> result = spotifyLibraryFileService.returnAnalysisFile()
                .select("artist_name", "track_popularity")
                .groupBy("artist_name")
                .agg(functions.avg("track_popularity").alias("average_popularity"))
                .orderBy(functions.desc("average_popularity"));

        return result.collectAsList().stream()
                .map(row -> AverageArtistPopularityResponse.builder()
                        .artistName(String.join(", ", row.getList(0)))
                        .averagePopularity(row.getDouble(1))
                        .build())
                .toList();
    }

    @Override
    public List<SimilarTrackResponse> analyzeTrackTitles() {
        // Завантаження даних
        Dataset<Row> tracks = spotifyLibraryFileService.returnAnalysisFile()
                .select("track_name", "artist_name")
                .distinct();

        // Токенізація назв
        Tokenizer tokenizer = new Tokenizer().setInputCol("track_name").setOutputCol("words");
        Dataset<Row> wordsData = tokenizer.transform(tracks);

        // Обчислення TF-IDF
        HashingTF hashingTF = new HashingTF()
                .setInputCol("words")
                .setOutputCol("rawFeatures")
                .setNumFeatures(1000);
        Dataset<Row> featurizedData = hashingTF.transform(wordsData);

        IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");
        IDFModel idfModel = idf.fit(featurizedData);
        Dataset<Row> rescaledData = idfModel.transform(featurizedData);

        // Векторизація назв пісень
        Dataset<Row> trackFeatures = rescaledData.select(
                tracks.col("track_name").alias("track_name"),
                tracks.col("artist_name").alias("artist_name"),
                rescaledData.col("features").alias("features")
        );

        // Крос-джойн для обчислення косинусної подібності
        Dataset<Row> crossJoin = trackFeatures.alias("t1")
                .crossJoin(trackFeatures.alias("t2"))
                .filter("t1.track_name != t2.track_name");

        // Додавання стовпця подібності
        Dataset<Row> similarityData = crossJoin.withColumn("similarity",
                functions.callUDF("cosineSimilarity",
                        crossJoin.col("t1.features"),
                        crossJoin.col("t2.features")));

        // Фільтрую та сортую найбільш схожі пари
        Dataset<Row> topSimilarities = similarityData
                .filter("similarity > 0.5")
                .orderBy(functions.desc("similarity")) // Сортування за спаданням cosineSimilarity
                .select(
                        crossJoin.col("t1.track_name").alias("track_name"),
                        crossJoin.col("t2.track_name").alias("similar_track_name"),
                        crossJoin.col("t1.artist_name").alias("artist_name"),
                        crossJoin.col("t2.artist_name").alias("similar_artist_name"),
                        similarityData.col("similarity")
                );

        return topSimilarities.collectAsList().stream()
                .map(row -> SimilarTrackResponse.builder()
                        .trackName(row.getString(0))
                        .similarTrackName(row.getString(1))
                        .artistName(String.join(", ", row.getList(2)))
                        .similarArtistName(String.join(", ", row.getList(3)))
                        .similarityScore(row.getDouble(4))
                        .build())
                .toList();
    }

}
