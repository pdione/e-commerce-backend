package com.embarkx.sbecommerce.service;

import com.embarkx.sbecommerce.model.Category;
import com.embarkx.sbecommerce.payload.request.CategoryDTO;
import com.embarkx.sbecommerce.payload.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);

    CategoryDTO createCategory(CategoryDTO categoryDto);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDto);
}
