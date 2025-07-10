package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByIdIn(List<Long> ids);
} 