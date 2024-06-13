package com.visa.lib.repository;

import com.visa.lib.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByCategoryTitleContaining(String categoryTitle);
}
