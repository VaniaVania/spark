package com.ivan.dropout.config;

import org.apache.spark.ml.linalg.BLAS;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Bean
    public SparkSession sparkSession() {
        SparkSession session = SparkSession.builder()
                .appName("Hello World Spark")
                .master("local[*]")
                .getOrCreate();
        session.udf().register("cosineSimilarity"
                , (UDF2<Vector, Vector, Double>) SparkConfig::cosineSimilarity
                , DataTypes.DoubleType);

        return session;
    }

    public static double cosineSimilarity(Vector vector1, Vector vector2) {
        double dotProduct = BLAS.dot(vector1, vector2);
        double norm1 = Vectors.norm(vector1, 2.0);
        double norm2 = Vectors.norm(vector2, 2.0);
        return dotProduct / (norm1 * norm2);
    }
}
