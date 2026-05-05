package com.example.kafka.order;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private double totalCost;

    private String address;

    private boolean shipped;

    public Order(String customerName,double totalCost , String address , boolean shipped){

        this.customerName = customerName;
        this.totalCost = totalCost;
        this.address = address;
        this.shipped = shipped;

    }


}
