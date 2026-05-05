package com.example.kafka.shipping;


import org.springframework.kafka.core.KafkaTemplate;

public class ShippedOrderIdProducer {


    private KafkaTemplate<String,Long> kafkaTemplate;

    public void produceOrderId(Long orderId){

        kafkaTemplate.send(AppConstants.SHIPPED_ORDER_TOPIC , orderId);
    }

}
