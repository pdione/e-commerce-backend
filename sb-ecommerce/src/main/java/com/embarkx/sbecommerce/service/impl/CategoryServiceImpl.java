package com.embarkx.sbecommerce.service.impl;

import com.embarkx.sbecommerce.exception.ApiException;
import com.embarkx.sbecommerce.exception.ResourceNotFoundException;
import com.embarkx.sbecommerce.model.Category;
import com.embarkx.sbecommerce.payload.request.CategoryDTO;
import com.embarkx.sbecommerce.payload.response.CategoryResponse;
import com.embarkx.sbecommerce.repository.CategoryRepository;
import com.embarkx.sbecommerce.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    //private List<Category> categories = new ArrayList<>();

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> categoriesPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoriesPage.getContent();
        if (categories.isEmpty()) {
            throw new ApiException("No category created till now.");
        }
        List<CategoryDTO> categoryDTOS = categories
                .stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(pageNumber);
        categoryResponse.setPageSize(pageSize);
        categoryResponse.setTotalElements(categoriesPage.getTotalElements());
        categoryResponse.setTotalPages(categoriesPage.getTotalPages());
        categoryResponse.setLast(categoriesPage.isLast());
        return categoryResponse;
        //return new CategoryResponse(categoryDTOS);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new ApiException("Category with name " + category.getCategoryName() + " already exists");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category category = optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        CategoryDTO deletedCategory = modelMapper.map(category, CategoryDTO.class);
        categoryRepository.delete(category);
        // categories.removeIf(category -> category.getCategoryId().equals(categoryId));
        return deletedCategory;
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Category categoryEntity = modelMapper.map(categoryDto, Category.class);
        categoryEntity.setCategoryId(categoryId);

        Category savedEntity = categoryRepository.save(categoryEntity);
        return modelMapper.map(savedEntity, CategoryDTO.class);
    }

}
