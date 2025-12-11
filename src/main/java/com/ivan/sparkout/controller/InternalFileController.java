package com.ivan.sparkout.controller;

import com.ivan.sparkout.service.spotify.SpotifyLibraryFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal")
public class InternalFileController {

    private final SpotifyLibraryFileService spotifyLibraryFileService;

    @GetMapping("/schema")
    public String cleanedData() {
        return spotifyLibraryFileService.returnSchema();
    }
}
