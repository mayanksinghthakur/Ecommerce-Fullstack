package com.ecommerce.Ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//representing individual orderItem in the class
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Long orderItemId;
    private ProductDto product;
    private Integer quantity;
    private double discount;
    private double orderProductPrice;
}
