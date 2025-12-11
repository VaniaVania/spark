package com.ivan.sparkout.util;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ivan.sparkout.dto.request.SongInformationRequest;
import com.ivan.sparkout.dto.response.SongCollaborationResponse;
import org.apache.spark.sql.Row;

import java.util.List;

public class JsonUtils {
    public static String toJsonMinimalArtists(SongInformationRequest r) {
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        root.putPOJO("producerArtists", r.getProducerArtists());
        root.putPOJO("writerArtists", r.getWriterArtists());
        root.putPOJO("featuredArtists", r.getFeaturedArtists());
        return root.toString();
    }

    public static List<SongCollaborationResponse.ArtistResponse> toArtistList(List<Row> rows) {
        return rows.stream()
                .map(r -> new SongCollaborationResponse.ArtistResponse(r.getInt(0), r.getString(1), r.getString(2), r.getString(3)))
                .toList();
    }

    public static List<SongCollaborationResponse.CollabEdge> toEdgeList(List<Row> rows) {
        return rows.stream()
                .map(r -> new SongCollaborationResponse.CollabEdge(r.getInt(0), r.getInt(1)))
                .toList();
    }
}

