package com.ivan.sparkout.dto.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Annotation {
    String apiPath;
    //String body; //TODO: implement this field
    int commentCount;
    boolean community;
    String customPreview;
    boolean hasVoters;
    int id;
    boolean pinned;
    String shareUrl;
    String source;
    String state;
    String url;
    boolean verified;
    int votesTotal;
    //String currentUserMetadata; //TODO: implement this field
    //String authors; //TODO: implement this field
    //String cosignedBy; //TODO: implement this field
    String rejectionComment;
    String verifiedBy;
}
