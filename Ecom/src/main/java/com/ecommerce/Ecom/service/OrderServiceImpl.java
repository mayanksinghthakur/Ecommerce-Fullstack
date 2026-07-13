package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.exceptions.APIException;
import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.*;
import com.ecommerce.Ecom.payload.OrderDto;
import com.ecommerce.Ecom.payload.OrderItemDto;
import com.ecommerce.Ecom.payload.OrderResponse;
import com.ecommerce.Ecom.repositories.*;
import com.ecommerce.Ecom.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ModelMapper modelMapper;


    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Override
    @Transactional
    public OrderDto placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {

        //getting the user cart because we want the user cart that user created
        Cart cart=cartRepository.findCartByEmail(emailId);

        if(cart==null){
            throw  new ResourceNotFoundException("cart","email",emailId);
        }


        //create a new order with the payment info
        Address address=addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        //get item from the cart into the order item
        Order order=new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Accepted!");
        order.setAddress(address);

        //creating payment object
        Payment payment=new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);

        payment.setOrder(order);

        payment=paymentRepository.save(payment);
        order.setPayment(payment);

        //saving order in the order database and getting the save order in the variable later using it as the
        Order savedOrder=orderRepository.save(order);

        //we want the items that user want to order so we will use this and order model has the  List<CartItem>
        List<CartItem>cartItems=cart.getCartItems();

        if(cartItems.isEmpty()){
            throw new APIException("Cart is empty");
        }


        //we need to get items from the cart and transform it into the OrderItem

        List<OrderItem>orderItems=new ArrayList<>();

        for(CartItem cartItem:cartItems){
            OrderItem orderItem=new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            //product price from cartItem
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());

            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        //we will get all the saved order things  so order is created
        orderItems = orderItemRepository.saveAll(orderItems);


        //now clear the cart
            cart.getCartItems().forEach((item)->{
            int quantity=item.getQuantity();
            Product product=item.getProduct();
            product.setQuantity(product.getQuantity()-quantity);
            productRepository.save(product);


            //clear the cart whose items where ordered
            cartService.deleteProductFromCart(cart.getCartId(),item.getProduct().getProductId());


        });

        //send back the order summary
        OrderDto orderDto=modelMapper.map(savedOrder,OrderDto.class);

    //we have the list<OrderItemDto> in the orderDto class so we need to add that

    //for every orderItems we are getting the OrderItems and mapping to the orderDto.class and addinf to the



        orderItems.forEach(item->orderDto.getOrderItems().add
                //converting orderItem to the orderItemDto class
                (modelMapper.map(item, OrderItemDto.class)));

        //setting addreess id also
        orderDto.setAddressId(addressId);
        return orderDto;
    }

    @Override
    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Order> pageOrders = orderRepository.findAll(pageDetails);
        List<Order> orders = pageOrders.getContent();

        //converting this to OrderDto becuae in OrderResponse class we have another type returned if you will look
        List<OrderDto> orderDTOs = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .toList();
    //creating order Response
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());
        return orderResponse;
    }

    @Override
    public OrderDto updateOrder(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order","orderId",orderId));
        order.setOrderStatus(status);
        orderRepository.save(order);
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderResponse getAllSellerOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        User seller = authUtil.loggedInUser();

        Page<Order> pageOrders = orderRepository.findAll(pageDetails);

        List<Order> sellerOrders = pageOrders.getContent().stream()
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(orderItem -> {
                            var product = orderItem.getProduct();
                            if (product == null || product.getUser() == null) {
                                return false;
                            }
                            return product.getUser().getUserId().equals(
                                    seller.getUserId());
                        }))
                .toList();

        List<OrderDto> orderDTOs = sellerOrders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .toList();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());
        return orderResponse;
    }



}
