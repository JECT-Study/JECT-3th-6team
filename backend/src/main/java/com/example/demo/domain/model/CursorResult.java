package com.example.demo.domain.model;

import java.util.List;

//TODO : 현장대기에서도 사용하도록 리팩토링
public record CursorResult<T>(
    List<T> content,
    boolean hasNext
) {
} 