package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.exceptions.ResourceNotFoundException;
import org.fpoly.capstone.repository.CategoryRepository;
import org.fpoly.capstone.service.CategoryService;
import org.fpoly.capstone.service.payload.category.CategoryRequest;
import org.fpoly.capstone.service.payload.category.CategoryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final ModelMapper modelMapper;
  private static final String CATEGORY_NOT_FOUND_EXCEPTIONS = "Category not found with id: ";

  @Override
  public List<Category> getAllCategory() {
    return this.categoryRepository.findAll();
  }

  @Override
  public Page<CategoryResponse> getAllCategory(Pageable pageable) {
    return this.categoryRepository.findAll(pageable)
        .map(category -> this.modelMapper.map(category, CategoryResponse.class));
  }

  @Override
  public Page<CategoryResponse> searchCategory(String name, Boolean status, Pageable pageable) {
    // Delegate the search logic to the repository's custom query method
    return this.categoryRepository.findByFilter(name, status, pageable);
  }

  @Override
  public void createCategory(CategoryRequest request) {
    Category category = Category.builder()
        .name(request.getName())
        .status(request.getStatus())
        .build();

    Category savedCategory = this.categoryRepository.save(category);

    this.modelMapper.map(savedCategory, CategoryResponse.class);
  }

  @Override
  public void updateCategory(Integer categoryId, CategoryRequest request) {
    Category existingCategory = this.categoryRepository.findById(Long.valueOf(categoryId))
        .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_EXCEPTIONS + categoryId));

    existingCategory.setName(request.getName());
    existingCategory.setStatus(request.getStatus());

    Category updatedCategory = this.categoryRepository.save(existingCategory);

    this.modelMapper.map(updatedCategory, CategoryResponse.class);
  }

  @Override
  public void deleteCategory(Integer categoryId) {
    Category category = this.categoryRepository.findById(Long.valueOf(categoryId))
        .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_EXCEPTIONS + categoryId));

    this.categoryRepository.delete(category);
  }

  @Override
  public CategoryResponse getCategoryById(Integer categoryId) {
    Category category = this.categoryRepository.findById(Long.valueOf(categoryId))
        .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_EXCEPTIONS + categoryId));

    return this.modelMapper.map(category, CategoryResponse.class);
  }

}
