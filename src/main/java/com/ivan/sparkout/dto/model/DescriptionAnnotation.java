package com.ivan.sparkout.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DescriptionAnnotation {
    @JsonProperty("_type")
    String _type;
    int annotatorId;
    String annotatorLogin;
    String apiPath;
    String classification;
    String fragment;
    int id;
    boolean isDescription;
    String path;
    Range range;
    int songId;
    String url;
    List<Integer> verifiedAnnotatorIds;
    Annotatable annotatable;
    List<Annotation> annotations;
}
