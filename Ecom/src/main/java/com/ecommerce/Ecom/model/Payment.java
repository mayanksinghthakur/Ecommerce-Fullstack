package com.ecommerce.Ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Order order;

    @NotBlank
    @Size(min=4,message = "payment method must contain  at least  4 characters  ")
    private  String paymentMethod;

    //for the paymentgateway
    private String pgPaymentId;
    //payment gateway status
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    //helps to creat the payment object  that we would like to use when we need the gatewayinfo to be saved
    public Payment(String paymentMethod,String pgPaymentId,String pgStatus,String pgResponseMessage,String pgName){this.paymentMethod=paymentMethod;
    this.pgPaymentId=pgPaymentId;
    this.pgStatus=pgStatus;
    this.pgResponseMessage=pgResponseMessage;
    this.pgName=pgName;
    }
}
