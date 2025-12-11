package com.ivan.sparkout.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AlbumTypePopularityResponse {

    private String albumType;
    private String popularity;
}
