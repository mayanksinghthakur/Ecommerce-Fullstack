package com.ecommerce.Ecom.controller;

import com.ecommerce.Ecom.config.AppConstants;
import com.ecommerce.Ecom.payload.*;
import com.ecommerce.Ecom.service.OrderService;
import com.ecommerce.Ecom.service.StripeService;
import com.ecommerce.Ecom.util.AuthUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private StripeService stripeService;


    @PostMapping("order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDto>orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDto orderRequestDto){


        //getting the logged in user
        String emailId=authUtil.loggedInEmail();
        //we dont know about which order to place we only know about the email id
        //so we use the cart to get the orders info

       OrderDto order= orderService.placeOrder(
                emailId,
               orderRequestDto.getAddressId(),
               paymentMethod,
               orderRequestDto.getPgName(),
               orderRequestDto.getPgPaymentId(),
               orderRequestDto.getPgStatus(),
               orderRequestDto.getPgResponseMessage()

        );

       return  new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PostMapping("order/stripe-client-secret")
    public ResponseEntity<String>CreateStripeClientsSecret(@RequestBody StripePaymentDto stripePaymentDto) throws StripeException {
        System.out.println("orderRequestDTO DATA: " + stripePaymentDto);
        PaymentIntent paymentIntent=stripeService.paymentIntent(stripePaymentDto);
        return new ResponseEntity<>(paymentIntent.getClientSecret(),HttpStatus.CREATED);
    };


    //all the ordeers will be reflected to the admin
    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/seller/orders")
    public ResponseEntity<OrderResponse> getAllSellerOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        OrderResponse orderResponse = orderService.getAllSellerOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/orders/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long orderId,
                                                      @RequestBody OrderStatusUpdateDto orderStatusUpdateDto) {
        OrderDto order = orderService.updateOrder(orderId, orderStatusUpdateDto.getStatus());
        return new ResponseEntity<OrderDto>(order, HttpStatus.OK);
    }

    @PutMapping("/seller/orders/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatusSeller(@PathVariable Long orderId,
                                                            @RequestBody OrderStatusUpdateDto orderStatusUpdateDto) {
        OrderDto order = orderService.updateOrder(orderId, orderStatusUpdateDto.getStatus());
        return new ResponseEntity<OrderDto>(order, HttpStatus.OK);
    }

}
