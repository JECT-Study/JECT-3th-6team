package com.example.demo.domain.model;

import java.util.List;

public record SearchTags(
    String type,
    List<String> categoryNames
) {}