package com.tanish.Blog_springboot.service.interf;

import com.tanish.Blog_springboot.dto.ApiResponse;
import com.tanish.Blog_springboot.dto.CategoryRequest;

public interface CategoryService {
    ApiResponse addCategory(CategoryRequest categoryRequest);
    ApiResponse updateCategory(Long id,CategoryRequest updateCategory);
    ApiResponse deleteCategory(Long id);
    ApiResponse getCategory(Long id);
    ApiResponse getAllCategory();
}
