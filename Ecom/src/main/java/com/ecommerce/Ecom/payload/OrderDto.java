package com.ecommerce.Ecom.payload;

import com.ecommerce.Ecom.model.Address;
import com.ecommerce.Ecom.model.OrderItem;
import com.ecommerce.Ecom.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {

    private Long orderId;
    private String email;
    List<OrderItemDto> orderItems;
    private LocalDate orderDate;
    private PaymentDto payment;
    private Double totalAmount;
    private String orderStatus;
    //because we want to which address
    private Long addressId;
}
