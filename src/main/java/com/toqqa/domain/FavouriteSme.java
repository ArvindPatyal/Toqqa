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
public class FavouriteSme {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "sme_id")
    private String smeId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sme_id", insertable = false, updatable = false)
    private Sme sme;

    @ManyToOne
    @JoinColumn(name = "favourite_id")
    private Favourite favourite;
}
