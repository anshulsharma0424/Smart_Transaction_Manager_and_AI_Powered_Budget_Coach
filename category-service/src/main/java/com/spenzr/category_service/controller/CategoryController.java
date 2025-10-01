package com.spenzr.category_service.controller;

import com.spenzr.category_service.dto.CategoryDto;
import com.spenzr.category_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        CategoryDto createdCategory = categoryService.createCategory(categoryDto, userId);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getUserCategories(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(categoryService.getCategoriesForUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        categoryService.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }
}
