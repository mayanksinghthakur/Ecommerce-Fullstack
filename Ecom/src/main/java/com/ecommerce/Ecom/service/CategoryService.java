package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.payload.CategoryDto;
import com.ecommerce.Ecom.payload.CategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface CategoryService {
     CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryDto createCategory(CategoryDto categoryDto
    );

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
}
