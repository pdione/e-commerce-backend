package com.embarkx.sbecommerce.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productId;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, message = "Product name must be at least 3 characters long")
    private String productName;

    private String image;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 6, message = "Product name must be at least 6 characters long")
    private String description;

    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

}
