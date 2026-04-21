package com.embarkx.sbecommerce.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @Schema(description = "Category ID for a particular category", example = "101")
    private Long categoryId;

    @Schema(description = "Category name for a particular category", example = "Electronics")
    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 5, message = "Category name must be at least 5 characters long")
    private String categoryName;
}
