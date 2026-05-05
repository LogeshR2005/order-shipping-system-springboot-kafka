package com.example.kafka.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;


    public void produceOrder(Order order){

        ObjectMapper mapper = new ObjectMapper();

        String orderJsonString = null;

        try{

           orderJsonString = mapper.writeValueAsString(order);

        }catch(Exception e ){
          e.printStackTrace();
        }


        System.out.println("Produced Order -> "+orderJsonString);

        kafkaTemplate.send(AppConstants.ORDER_TOPIC,orderJsonString);
    }
}
