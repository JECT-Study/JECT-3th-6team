package com.example.demo.domain.model;

import java.util.List;
import java.util.stream.Stream;

//TODO : 현장대기에서도 사용하도록 리팩토링
public record CursorResult<T>(
        List<T> content,
        boolean hasNext,
        Long lastId
) {
    public CursorResult(List<T> content, boolean hasNext) {
        this(content, hasNext, null);
    }

    public Stream<T> stream() {
        return content.stream();
    }
} 