package com.example.kafka.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/shipping")
public class ShippingController {


    @Autowired
    private ShippingRepository shippingRepository;;


    private final ShippedOrderIdProducer shippedOrderIdProducer;

    public ShippingController(ShippedOrderIdProducer shippedOrderIdProducer) {
        this.shippedOrderIdProducer = shippedOrderIdProducer;
    }


    @DeleteMapping("/shipping/{orderId}")
    public String saveAndProduceOrderId(@PathVariable Long orderId){
        shippedOrderIdProducer.produceOrderId(orderId);
        shippingRepository.deleteById(orderId);

        return "Order is shipped (deleted) from shipping database and kafka ";
    }

    @GetMapping
    public List<Shipping> getOrderToShipping(){
        return shippingRepository.findAll();
    }




    public void saveShipping(Shipping shippingOrder) {
        shippingRepository.save(shippingOrder);

        System.out.println("saved in shipping db");
    }





}
