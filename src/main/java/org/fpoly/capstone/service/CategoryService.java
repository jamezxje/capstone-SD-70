package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.service.payload.category.CategoryRequest;
import org.fpoly.capstone.service.payload.category.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();

    Page<CategoryResponse> getAllCategory(Pageable pageable);

    Page<CategoryResponse> searchCategory(String name, Boolean status, Pageable pageable);

    void createCategory(CategoryRequest request);

    void updateCategory(Integer categoryId, CategoryRequest request);

    void deleteCategory(Integer categoryId);

    CategoryResponse getCategoryById(Integer categoryId);

}
