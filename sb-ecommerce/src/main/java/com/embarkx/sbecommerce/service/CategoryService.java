package com.embarkx.sbecommerce.service;

import com.embarkx.sbecommerce.model.Category;
import com.embarkx.sbecommerce.payload.request.CategoryDTO;
import com.embarkx.sbecommerce.payload.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories();

    CategoryDTO createCategory(CategoryDTO category);

    String deleteCategory(Long categoryId);

    Category updateCategory(Long categoryId, Category category);
}
