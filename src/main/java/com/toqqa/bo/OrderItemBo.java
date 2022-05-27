package com.toqqa.bo;

import com.toqqa.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemBo {

    private String id;
    private Integer quantity;
    private Double pricePerUnit;
    private Double discount;
    private ProductBo product;

    private Double price;

    public OrderItemBo(OrderItem orderItem, ProductBo productBo) {

        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        this.pricePerUnit = orderItem.getPricePerUnit();
        this.product = productBo;
        this.discount = orderItem.getDiscount();
        this.price = orderItem.getPrice();


    }

}