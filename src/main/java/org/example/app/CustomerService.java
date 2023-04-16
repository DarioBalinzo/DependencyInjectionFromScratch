package org.example.app;

import org.example.ci.Component;

@Component
public class CustomerService {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public CustomerService(OrderService orderService, PaymentService paymentService) {
       this.orderService = orderService;
        this.paymentService = paymentService;
    }


}
