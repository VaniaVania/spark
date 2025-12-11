package com.ivan.sparkout.service.genius.impl;

import com.ivan.sparkout.dto.request.SongComparisonRequest;
import com.ivan.sparkout.dto.request.SongInformationRequest;
import com.ivan.sparkout.dto.response.SongCollaborationResponse;
import com.ivan.sparkout.service.genius.GeniusLibraryCollaborationService;
import com.ivan.sparkout.util.JsonUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.graphframes.GraphFrame;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.spark.sql.functions.col;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GeniusLibraryCollaborationServiceImpl implements GeniusLibraryCollaborationService {
    SparkSession spark;

    @Override
    public SongCollaborationResponse analyzeCollaboration(SongComparisonRequest request) {

        SongInformationRequest s1 = request.getFirstSong();
        SongInformationRequest s2 = request.getSecondSong();

        StructType artistType = new StructType()
                .add("id", DataTypes.IntegerType)
                .add("name", DataTypes.StringType)
                .add("header_image_url", DataTypes.StringType);

        StructType schema = new StructType()
                .add("producerArtists", DataTypes.createArrayType(artistType))
                .add("writerArtists", DataTypes.createArrayType(artistType))
                .add("featuredArtists", DataTypes.createArrayType(artistType));

        Dataset<Row> ds1 = spark.createDataset(
                        List.of(JsonUtils.toJsonMinimalArtists(s1)), Encoders.STRING())
                .toDF("json")
                .select(functions.from_json(col("json"), schema).as("data"))
                .select("data.*");

        Dataset<Row> ds2 = spark.createDataset(
                        List.of(JsonUtils.toJsonMinimalArtists(s2)), Encoders.STRING())
                .toDF("json")
                .select(functions.from_json(col("json"), schema).as("data"))
                .select("data.*");

        Dataset<Row> v1 = extractVertices(ds1);
        Dataset<Row> v2 = extractVertices(ds2);

        Dataset<Row> e1 = buildEdges(ds1);
        Dataset<Row> e2 = buildEdges(ds2);

        GraphFrame g1 = new GraphFrame(v1, e1);
        GraphFrame g2 = new GraphFrame(v2, e2);


        Dataset<Row> v1a = v1.alias("v1");
        Dataset<Row> v2a = v2.alias("v2");

        Dataset<Row> commonArtists = v1a
                .join(v2a, v1a.col("id").equalTo(v2a.col("id")))
                .select(
                        v1a.col("id").as("id"),
                        v1a.col("name").as("name"),
                        v1a.col("role").as("role"),
                        v1a.col("imageUrl").as("imageUrl")
                );

        Dataset<Row> commonEdges = e1.intersect(e2);

        List<Row> finalArtists = commonArtists.collectAsList();
        List<Row> finalEdges = commonEdges.collectAsList();

        return SongCollaborationResponse.builder()
                .sharedArtists(JsonUtils.toArtistList(finalArtists))
                .sharedCollaborations(JsonUtils.toEdgeList(finalEdges))
                .build();
    }

    private Dataset<Row> extractVertices(Dataset<Row> ds) {

        Dataset<Row> p = ds.selectExpr("explode(producerArtists) as a")
                .selectExpr("a.id as id", "a.name as name", "'Producer' as role", "a.header_image_url as imageUrl");

        Dataset<Row> w = ds.selectExpr("explode(writerArtists) as a")
                .selectExpr("a.id as id", "a.name as name", "'Writer' as role", "a.header_image_url as imageUrl");

        Dataset<Row> f = ds.selectExpr("explode(featuredArtists) as a")
                .selectExpr("a.id as id", "a.name as name", "'Featured' as role", "a.header_image_url as imageUrl");


        return p.union(w).union(f).dropDuplicates("id");
    }

    private Dataset<Row> buildEdges(Dataset<Row> ds) {

        StructType edgeSchema = new StructType()
                .add("src", DataTypes.IntegerType)
                .add("dst", DataTypes.IntegerType);

        return ds.flatMap(
                new EdgeBuilder(),
                RowEncoder.apply(edgeSchema)
        );
    }

    static class EdgeBuilder implements FlatMapFunction<Row, Row>, Serializable {

        @Override
        public Iterator<Row> call(Row row) {
            List<Row> artists = new ArrayList<>();

            addAllStatic(artists, row, "producerArtists");
            addAllStatic(artists, row, "writerArtists");
            addAllStatic(artists, row, "featuredArtists");

            List<Row> edges = new ArrayList<>();
            for (int i = 0; i < artists.size(); i++) {
                for (int j = i + 1; j < artists.size(); j++) {
                    int a = artists.get(i).getInt(artists.get(i).fieldIndex("id"));
                    int b = artists.get(j).getInt(artists.get(j).fieldIndex("id"));
                    edges.add(RowFactory.create(a, b));
                }
            }
            return edges.iterator();
        }

        static void addAllStatic(List<Row> target, Row row, String field) {
            if (!row.isNullAt(row.fieldIndex(field))) {
                target.addAll(row.getList(row.fieldIndex(field)));
            }
        }
    }
}

