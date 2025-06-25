package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatrgoryRepository extends JpaRepository<CategoryEntity, Long> {

}
