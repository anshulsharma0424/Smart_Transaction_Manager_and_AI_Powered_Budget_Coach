package com.spenzr.category_service.service;

import com.spenzr.category_service.dto.CategoryDto;
import com.spenzr.category_service.entity.Category;
import com.spenzr.category_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto createCategory(CategoryDto categoryDto, String userId) {
        Category category = Category.builder()
                .name(categoryDto.getName())
                .type(categoryDto.getType())
                .userId(userId) // Associate with the current user
                .build();
        Category savedCategory = categoryRepository.save(category);
        return mapToDto(savedCategory);
    }

    public List<CategoryDto> getCategoriesForUser(String userId) {
        return categoryRepository.findByUserIdOrUserIdIsNull(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto updateCategory(String id, CategoryDto categoryDto, String userId) {
        // Find the category ensuring it belongs to the user
        Category existingCategory = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Category not found or you don't have permission to update it."));

        existingCategory.setName(categoryDto.getName());
        existingCategory.setType(categoryDto.getType());

        Category updatedCategory = categoryRepository.save(existingCategory);
        return mapToDto(updatedCategory);
    }

    public void deleteCategory(String id, String userId) {
        // Find the category ensuring it belongs to the user
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Category not found or you don't have permission to delete it."));
        categoryRepository.delete(category);
    }

    private CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build();
    }
}
