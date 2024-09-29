package com.tanish.Blog_springboot.service.impl;

import com.tanish.Blog_springboot.dto.ApiResponse;
import com.tanish.Blog_springboot.dto.CategoryDto;
import com.tanish.Blog_springboot.dto.CategoryRequest;
import com.tanish.Blog_springboot.entity.Category;
import com.tanish.Blog_springboot.exception.NotFoundException;
import com.tanish.Blog_springboot.mapper.EntityDtoMapper;
import com.tanish.Blog_springboot.repository.CategoryRepo;
import com.tanish.Blog_springboot.service.interf.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final  EntityDtoMapper entityDtoMapper;
    @Override
    public ApiResponse addCategory(CategoryRequest categoryRequest) {

        Category category = Category.builder()
                .title(categoryRequest.getTitle())
                .build();

        Category savedCategory = categoryRepo.save(category);
        CategoryDto categoryDto = entityDtoMapper.mapCategoryToCategoryDto(savedCategory);

        return ApiResponse.<CategoryDto>builder()
                .status(201)
                .message("Category added successfully")
                .data(categoryDto)
                .build();
    }

    @Override
    public ApiResponse updateCategory(Long id, CategoryRequest updateCategory) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        category.setTitle(updateCategory.getTitle());
        Category savedCategory = categoryRepo.save(category);
        CategoryDto categoryDto = entityDtoMapper.mapCategoryToCategoryDto(savedCategory);
        return ApiResponse.<CategoryDto>builder()
                .status(200)
                .message("Category update successfully")
                .data(categoryDto)
                .build();
    }

    @Override
    public ApiResponse deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        categoryRepo.deleteById(category.getId());

        return ApiResponse.<String>builder()
                .status(200)
                .message("category deleted successfully")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse getCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));
        CategoryDto categoryDto = entityDtoMapper.mapCategoryToCategoryDto(category);

        return ApiResponse.<CategoryDto>builder()
                .status(200)
                .message("Category fetched successfully")
                .data(categoryDto)
                .build();
    }

    @Override
    public ApiResponse getAllCategory() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map((category)->entityDtoMapper.mapCategoryToCategoryDto(category)).collect(Collectors.toList());
        return ApiResponse.<List<CategoryDto>>builder()
                .status(200)
                .message("Categories fetched successfully")
                .data(categoryDtos)
                .build();
    }
}
