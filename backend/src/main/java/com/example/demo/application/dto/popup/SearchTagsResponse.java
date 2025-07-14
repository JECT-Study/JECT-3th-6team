package com.example.demo.application.dto.popup;

import java.util.List;

public record SearchTagsResponse(
    String type,
    List<String> category
) {
}
