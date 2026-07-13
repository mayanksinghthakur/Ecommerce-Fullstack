package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.payload.StripePaymentDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeSearchResult;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StripeServiceImpl implements StripeService{
    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    //this annotation will help in creating a bean  StripeServiceImpl first than implementing this method
    @PostConstruct
    public void init(){
        Stripe.apiKey=stripeApiKey;
    }
    @Override
    public PaymentIntent paymentIntent(StripePaymentDto stripePaymentDto) throws StripeException{


        Customer customer;

        CustomerSearchParams searchParams =
                CustomerSearchParams.builder()
                        //searching customer using email
                        .setQuery("email:' "+stripePaymentDto.getEmail()+"'")
                        .build();
        CustomerSearchResult customers =
                Customer.search(searchParams);

    //if customer exisits inthe stripe than
        if(customers.getData().isEmpty()){
    //create a new cusomter

            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    //seting email and name for this
                    .setEmail(stripePaymentDto.getEmail())
                    .setName(stripePaymentDto.getName())
                    .setAddress(
                            //had to create nested CustomerCreateParams
                            CustomerCreateParams.Address.builder()
                                    .setLine1(stripePaymentDto.getAddress().getStreet())
                                    .setCity(stripePaymentDto.getAddress().getCity())
                                    .setState(stripePaymentDto.getAddress().getState())
                                    .setPostalCode(stripePaymentDto.getAddress().getPincode())
                                    .setCountry(stripePaymentDto.getAddress().getCountry())
                                    .build()
                    )
                    .build();

            customer = Customer.create(customerParams);
        }else {
//fetch that existing customer at the 0 
            customer = customers.getData().get(0);
        }
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(stripePaymentDto.getAmount())
                        .setCurrency(stripePaymentDto.getCurrency())
                        //getting customer id who has done payment
                        .setCustomer(customer.getId())
                        .setDescription(stripePaymentDto.getDescription())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        return PaymentIntent.create(params);

    }
}
