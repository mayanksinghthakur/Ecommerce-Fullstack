package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.exceptions.APIException;
import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.payload.CategoryDto;
import com.ecommerce.Ecom.payload.CategoryResponse;
import com.ecommerce.Ecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{



    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryResponse  getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        //using Sort from data class to sort values
        Sort sortByOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageableDetails= PageRequest.of(pageNumber,pageSize,sortByOrder);

        Page <Category>categoryPage=categoryRepository.findAll(pageableDetails);


        List<Category>categories=categoryPage.getContent();

        if(categories.isEmpty()){
            throw new APIException("No categories in database");
        }
        //mapping every object in the list
        List<CategoryDto>categoryDtos=categories.stream().map(category -> modelMapper.map(category,CategoryDto.class)).toList();

        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDtos);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPagSeize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
         return categoryResponse;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category=modelMapper.map(categoryDto,Category.class);

        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null)
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!!");

        Category savedCategory=categoryRepository.save(category);

        return modelMapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));



        categoryRepository.delete(category);


        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto , Long categoryId) {

        //getting category with id
        Category savedCategory=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));

        //Using category to map to category dto
        Category category=modelMapper.map(categoryDto,Category.class);

        //chaning id
        category.setCategoryId(categoryId);
         savedCategory =categoryRepository.save(category);

//         returning the categorydto class

        return modelMapper.map(savedCategory,CategoryDto.class);
    }
}
