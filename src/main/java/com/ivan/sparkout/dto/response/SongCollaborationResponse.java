package com.ivan.sparkout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongCollaborationResponse {
    private List<ArtistResponse> sharedArtists;
    private List<CollabEdge> sharedCollaborations;

    @Data
    @AllArgsConstructor
    public static class ArtistResponse {
        private int id;
        private String name;
        private String role;
        private String imageUrl;
    }

    @Data
    @AllArgsConstructor
    public static class CollabEdge {
        private int src;
        private int dst;
    }
}
