package com.example.demo.domain.port;

import com.example.demo.domain.model.BrandStory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BrandStoryPortImpl implements BrandStoryPort {
    @Override
    public Optional<BrandStory> findByPopupId(Long popupId) {
        return Optional.empty();
    }
}
