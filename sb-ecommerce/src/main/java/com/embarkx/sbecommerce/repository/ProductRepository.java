package com.embarkx.sbecommerce.repository;

import com.embarkx.sbecommerce.model.Category;
import com.embarkx.sbecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageable);
}
