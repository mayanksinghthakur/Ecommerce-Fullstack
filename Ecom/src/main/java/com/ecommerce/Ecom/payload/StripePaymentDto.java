package com.ecommerce.Ecom.payload;

import com.ecommerce.Ecom.model.Address;
import lombok.Data;

import java.util.Map;

//we could accept two things that we want so
@Data
public class StripePaymentDto {
    private Long amount;
    private String currency;
    private String email;
    private String name;
    private Address address;
    private String description;
    private Map<String, String> metadata;

}
