package com.ecommerce.Ecom.controller;



import com.ecommerce.Ecom.config.AppConstants;
import com.ecommerce.Ecom.payload.CategoryDto;
import com.ecommerce.Ecom.payload.CategoryResponse;
import com.ecommerce.Ecom.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/echo")
    public ResponseEntity<String> echoMessage(@RequestParam(name="message",required = false,defaultValue = "default hi")  String message){
        return  new ResponseEntity<>("Echoed message: "+message,HttpStatus.OK);
    }

    @Tag(name="Category APIs",description = "APIs for managing categories")
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue=AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue=AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sortBy" ,defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder
    ){
         CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }


    @Tag(name="Category APIs",description = "APIs for managing categories")
    @Operation(summary = "Create Category",description = "Api to create a new Category")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "Category is created successfully"),
            //content is for the response that will be recived when error occurs
            @ApiResponse(responseCode = "400",description = "Invalid input",content = @Content),  @ApiResponse(responseCode = "500",description = "Internal server error",content = @Content)
    })
    @PostMapping ("/public/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto savedCategoryDto= categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(savedCategoryDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> deleteCategory(@Parameter(description = "ID of the category you wish to delete") @PathVariable Long categoryId){

            CategoryDto deleteCategory = categoryService.deleteCategory(categoryId);

            return new ResponseEntity<>(deleteCategory,HttpStatus.OK);
        }




    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long categoryId){

        CategoryDto savedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);

    }
    }




