package com.toqqa.domain;


import com.toqqa.constants.OrderConstants;
import com.toqqa.constants.PaymentConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_info")
public class OrderInfo {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @CreationTimestamp
    private Date createdDate;

    @UpdateTimestamp
    private Date modificationDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    private Double amount;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private OrderConstants orderStatus;

    private double shippingFee;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deliveryAddress_id")
    private DeliveryAddress address;

    @Enumerated(EnumType.STRING)
    private PaymentConstants paymentType;

    @ManyToOne
    @JoinColumn(name = "sme_id")
    private Sme sme;

}
