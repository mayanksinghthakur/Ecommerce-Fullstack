package com.ecommerce.Ecom.repositories;

import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

//we have added the  JpaSpecificationExecutor<Product> using this our product will get filtered
//so this JpaSpecificationExecutor<Product> will allow us to execut ethe specification based on the criteria that we used to create dynamic query in the productservice impl class
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product> {


    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);

    Page<Product> findByUser(User user, Pageable pageDetails);
}
