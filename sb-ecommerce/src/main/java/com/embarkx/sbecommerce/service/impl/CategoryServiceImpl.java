package com.embarkx.sbecommerce.service.impl;

import com.embarkx.sbecommerce.exception.ApiException;
import com.embarkx.sbecommerce.exception.ResourceNotFoundException;
import com.embarkx.sbecommerce.model.Category;
import com.embarkx.sbecommerce.repository.CategoryRepository;
import com.embarkx.sbecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    //private List<Category> categories = new ArrayList<>();

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()){
            throw new ApiException("No category created till now.");
        }
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new ApiException("Category with name " + category.getCategoryName() + " already exists");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category category = optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);

        // categories.removeIf(category -> category.getCategoryId().equals(categoryId));
        return "Category with categoryId " + categoryId + " deleted successfully!";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        category.setCategoryId(categoryId);
        return categoryRepository.save(category);
    }

}
