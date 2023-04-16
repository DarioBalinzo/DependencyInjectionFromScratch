package org.example.app;

import org.example.ci.Component;

@Component
public class OrderService {

    private final PaymentService paymentService;


    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
