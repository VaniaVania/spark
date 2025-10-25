package com.ivan.dropout.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface SparkService {
    Dataset<Row> returnAnalysisFile();
    String returnSchema();
}
