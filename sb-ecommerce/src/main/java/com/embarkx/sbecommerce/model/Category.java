package com.embarkx.sbecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity(name = "categories")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

}
