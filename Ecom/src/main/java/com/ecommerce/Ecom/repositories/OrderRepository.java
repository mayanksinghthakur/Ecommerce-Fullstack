package com.ecommerce.Ecom.repositories;


import com.ecommerce.Ecom.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    //coalesece will help us when our toatlAnount from database is null and we want to avoid it we will use this so when anything is not present we will return 0
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o")
    Double getTotalRevenue();
}