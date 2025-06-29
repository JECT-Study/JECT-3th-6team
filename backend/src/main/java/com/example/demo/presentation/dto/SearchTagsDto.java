package com.example.demo.presentation.dto;

import com.example.demo.domain.model.SearchTags;
import java.util.List;

public record SearchTagsDto(
    String type,
    List<String> categoryNames
) {
    public static SearchTagsDto from(SearchTags tags) {
        return new SearchTagsDto(
            tags.type(),
            tags.categoryNames()
        );
    }
}
