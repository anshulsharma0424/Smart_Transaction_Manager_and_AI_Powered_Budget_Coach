package com.spenzr.category_service.dto;

import com.spenzr.category_service.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String id;
    private String name;
    private CategoryType type;
}