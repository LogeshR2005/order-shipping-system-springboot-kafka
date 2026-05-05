package com.example.kafka.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/orders")
public class OrderController {


    private final OrderProducer orderProducer;

    @Autowired
    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping(path="/produce")
    public String saveAndProducerOrder(@RequestBody Order order){

        Order orderWithId = orderRepository.save(order);
        System.out.println(orderWithId);

        orderProducer.produceOrder(orderWithId);

        return "Saved order in db and produced to kafka order topic ";
    }
}
