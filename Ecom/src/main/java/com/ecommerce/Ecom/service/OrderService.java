package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.payload.OrderDto;
import com.ecommerce.Ecom.payload.OrderResponse;
import jakarta.transaction.Transactional;



public interface OrderService {
    @Transactional
    OrderDto placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);



    OrderDto updateOrder(Long orderId, String status);

    OrderResponse getAllSellerOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}