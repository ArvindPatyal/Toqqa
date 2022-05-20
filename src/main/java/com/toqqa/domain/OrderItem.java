package com.toqqa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private Integer quantity;
    
    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderInfo orderInfo;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double shippingFee;

}
