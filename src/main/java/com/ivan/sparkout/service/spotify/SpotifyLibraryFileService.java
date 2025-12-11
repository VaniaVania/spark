package com.ivan.sparkout.service.spotify;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface SpotifyLibraryFileService {
    Dataset<Row> returnAnalysisFile();
    String returnSchema();
}
