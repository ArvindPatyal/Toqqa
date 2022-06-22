package com.toqqa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerRating {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private Integer sellerRating;
    private String reviewComment;
    @CreationTimestamp
    private Date dateOfRatingCreation;
    @UpdateTimestamp
    private Date dateOfRatingUpdation;
    @ManyToOne
    private Sme sme;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
