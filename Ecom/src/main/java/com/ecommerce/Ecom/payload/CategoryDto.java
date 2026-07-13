package com.ecommerce.Ecom.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//this is request object
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {
    @Schema(description = "category Id ",example = "101")
    private Long categoryId;
    @Schema(description = "categoryName for a particular category",example = "iphone 16")
    private String categoryName;
}
