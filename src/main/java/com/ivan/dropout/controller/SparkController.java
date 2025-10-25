package com.ivan.dropout.controller;

import com.ivan.dropout.service.SparkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spark")
public class SparkController {

    private final SparkService sparkService;

    @GetMapping("/schema")
    public String cleanedData() {
        return sparkService.returnSchema();
    }
}
