package com.ecommerce.Ecom.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long productId;
    private Integer quantity;
    //we have commented this to decrease the payload that we need during the request
//    private ProductDto productDto;
//    private Integer quantity;
//    private Double discount;
//    private Double productPrice;
}
