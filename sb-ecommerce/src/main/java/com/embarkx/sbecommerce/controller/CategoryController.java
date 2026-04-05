package com.embarkx.sbecommerce.controller;

import com.embarkx.sbecommerce.model.Category;
import com.embarkx.sbecommerce.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    private Long nextId = 1L;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        category.setCategoryId(nextId++);
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully!", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try {
//            return ResponseEntity.status(HttpStatus.OK).body(categoryService.deleteCategory(categoryId));
//            return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
            return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(categoryId, category);
            return ResponseEntity.ok("Category with id " + updatedCategory.getCategoryId() + " updated successfully!");
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

}
