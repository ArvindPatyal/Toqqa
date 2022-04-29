package com.toqqa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_subcategory")
public class ProductSubCategory {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")

    private String id;

    private String productSubCategory;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private ProductCategory productCategory;

    @ManyToMany(mappedBy = "productSubCategories", fetch = FetchType.LAZY)
    private List<Product> products;

}
