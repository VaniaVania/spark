package com.ivan.sparkout.service.genius.impl;

import com.ivan.sparkout.dto.response.SongAnalyticsResponse;
import com.ivan.sparkout.dto.request.SongComparisonRequest;
import com.ivan.sparkout.dto.request.SongInformationRequest;
import com.ivan.sparkout.service.genius.GeniusLibraryAnalysisService;
import lombok.RequiredArgsConstructor;
import org.apache.spark.ml.linalg.BLAS;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeniusLibraryAnalysisServiceImpl implements GeniusLibraryAnalysisService {

    @Override
    public SongAnalyticsResponse returnAnalysis(SongComparisonRequest request) {
        SongInformationRequest firstSong = request.getFirstSong();
        SongInformationRequest secondSong = request.getSecondSong();

        Double annotation1 = roundTo2((double) firstSong.getAnnotationCount());
        Double annotation2 = roundTo2((double) secondSong.getAnnotationCount());
        Double pyongs1 = firstSong.getPyongsCount() != null ? roundTo2(Double.valueOf(firstSong.getPyongsCount())) : 0.0;
        Double pyongs2 = secondSong.getPyongsCount() != null ? roundTo2(Double.valueOf(secondSong.getPyongsCount())) : 0.0;
        Double year1 = extractYear(firstSong.getReleaseDate());
        Double year2 = extractYear(secondSong.getReleaseDate());
        Double[] color1 = hexToRgbVector(firstSong.getSongArtPrimaryColor());
        Double[] color2 = hexToRgbVector(secondSong.getSongArtPrimaryColor());
        Double lang1 = encodeLanguage(firstSong.getLanguage());
        Double lang2 = encodeLanguage(secondSong.getLanguage());

        Double maxAnnotation = 100.0;
        Double maxPyongs = 100.0;
        Double minYear = 1900.0;
        Double maxYear = 2025.0;

        Double normAnnotation1 = roundTo2(normalize(annotation1, 0.0, maxAnnotation));
        Double normAnnotation2 = roundTo2(normalize(annotation2, 0.0, maxAnnotation));
        Double normPyongs1 = roundTo2(normalize(pyongs1, 0.0, maxPyongs));
        Double normPyongs2 = roundTo2(normalize(pyongs2, 0.0, maxPyongs));
        Double normYear1 = roundTo2(normalize(year1, minYear, maxYear));
        Double normYear2 = roundTo2(normalize(year2, minYear, maxYear));
        Double normLang1 = roundTo2(normalize(lang1, 0.0, 5.0));
        Double normLang2 = roundTo2(normalize(lang2, 0.0, 5.0));

        Vector vec1 = Vectors.dense(normAnnotation1, normPyongs1, normYear1, color1[0], color1[1], color1[2], normLang1);
        Vector vec2 = Vectors.dense(normAnnotation2, normPyongs2, normYear2, color2[0], color2[1], color2[2], normLang2);

        Double dotProduct = roundTo2(BLAS.dot(vec1, vec2));
        Double norm1 = roundTo2(Math.sqrt(BLAS.dot(vec1, vec1)));
        Double norm2 = roundTo2(Math.sqrt(BLAS.dot(vec2, vec2)));
        double cosineSimilarity = (norm1 == 0 || norm2 == 0) ? 0.0 : roundTo2(dotProduct / (norm1 * norm2));

        Double euclideanDistance = roundTo2(Math.sqrt(
                Math.pow(normAnnotation1 - normAnnotation2, 2) +
                        Math.pow(normPyongs1 - normPyongs2, 2) +
                        Math.pow(normYear1 - normYear2, 2) +
                        Math.pow(color1[0] - color2[0], 2) +
                        Math.pow(color1[1] - color2[1], 2) +
                        Math.pow(color1[2] - color2[2], 2) +
                        Math.pow(normLang1 - normLang2, 2)
        ));

        double colorDistance = roundTo2(Math.sqrt(
                Math.pow(color1[0] - color2[0], 2) +
                        Math.pow(color1[1] - color2[1], 2) +
                        Math.pow(color1[2] - color2[2], 2)
        ));

        Map<String, Double> similarityMetrics = Map.of(
                "cosine_similarity_percent", roundTo2(cosineSimilarity * 100),
                "euclidean_similarity_percent", roundTo2(Math.max(0.0, 100 - euclideanDistance * 100)),
                "color_similarity_percent", roundTo2(Math.max(0.0, 100 - colorDistance * 100))
        );
        Map<String, Double> popularityFactors = Map.of(
                "first_song_pyongs", pyongs1,
                "second_song_pyongs", pyongs2,
                "annotation_similarity_percent", roundTo2(Math.max(0.0, 100 - Math.abs(normAnnotation1 - normAnnotation2) * 100))
        );
        Map<String, Double> styleFeatures = Map.of(
                "primary_color_similarity_percent", roundTo2(Math.max(0.0, 100 - colorDistance * 100)),
                "language_similarity_percent", normLang1.equals(normLang2) ? 100.0 : 0.0
        );

        String yearDifference = (year1 == 0.0 || year2 == 0.0) ? "Undefined" : ((int)Math.abs(year1 - year2) + " years");

        Map<String, Object> temporalFeatures = Map.of(
                "release_year_similarity_percent", roundTo2(Math.max(0.0, 100 - Math.abs(normYear1 - normYear2) * 100)),
                "year_difference", yearDifference
        );

        SongAnalyticsResponse.TrackResponse firstTrack = SongAnalyticsResponse.TrackResponse.builder()
                .title(firstSong.getTitle())
                .artist(firstSong.getArtistNames())
                .releaseYear(firstSong.getReleaseDateForDisplay())
                .headerImageUrl(firstSong.getHeaderImageUrl())
                .build();

        SongAnalyticsResponse.TrackResponse secondTrack = SongAnalyticsResponse.TrackResponse.builder()
                .title(secondSong.getTitle())
                .artist(secondSong.getArtistNames())
                .releaseYear(secondSong.getReleaseDateForDisplay())
                .headerImageUrl(secondSong.getHeaderImageUrl())
                .build();

        return SongAnalyticsResponse.builder()
                .firstTrack(firstTrack)
                .secondTrack(secondTrack)
                .similarityMetrics(similarityMetrics)
                .popularityFactors(popularityFactors)
                .styleFeatures(styleFeatures)
                .temporalFeatures(temporalFeatures)
                .similarityScore(roundTo2(cosineSimilarity * 100))
                .build();
    }

    private Double normalize(Double value, Double min, Double max) {
        if (value < min) value = min;
        if (value > max) value = max;
        return (value - min) / (max - min);
    }

    private Double extractYear(String date) {
        if (date == null || date.length() < 4) return 0.0;
        try {
            return Double.valueOf(date.substring(0, 4));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Double[] hexToRgbVector(String hex) {
        try {
            if (hex == null || !hex.startsWith("#")) return new Double[]{0.0, 0.0, 0.0};
            Double r = Integer.parseInt(hex.substring(1, 3), 16) / 255.0;
            Double g = Integer.parseInt(hex.substring(3, 5), 16) / 255.0;
            Double b = Integer.parseInt(hex.substring(5, 7), 16) / 255.0;
            return new Double[]{roundTo2(r), roundTo2(g), roundTo2(b)};
        } catch (Exception e) {
            return new Double[]{0.0, 0.0, 0.0};
        }
    }

    private Double encodeLanguage(String lang) {
        if (lang == null) return 0.0;
        return switch (lang.toLowerCase()) {
            case "en" -> 1.0;
            case "es" -> 2.0;
            case "fr" -> 3.0;
            case "de" -> 4.0;
            case "uk" -> 5.0;
            default -> 0.0;
        };
    }

    private Double roundTo2(Double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
