package com.toqqa.bo;

import com.toqqa.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductBo {

    private String id;

    private String productName;

    private List<ProductCategoryBo> productCategories = new ArrayList<ProductCategoryBo>();

    private List<ProductSubCategoryBo> productSubCategories = new ArrayList<ProductSubCategoryBo>();

    private List<FileBo> images = new ArrayList<>();

    private String description;

    private Long unitsInStock;

    private Double pricePerUnit;

    private Double discount;

    private Integer maximumUnitsInOneOrder;

    private Integer minimumUnitsInOneOrder;

    private Date expiryDate;

    private String countryOfOrigin;

    private String manufacturerName;

    private Boolean isDeleted;

    private Date ManufacturingDate;

    private FileBo banner;

    private Boolean deliveredInSpecifiedRadius;

    private Boolean delieveredOutsideSpecifiedRadius;

    private Boolean isInWishList = false;

    private double averageRating;

    private Integer totalReviews;

    private String productUserId;

    private SmeBo sellerDetails;


    private Integer sequenceNumber;

    public ProductBo(Product product, List<FileBo> images) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.unitsInStock = product.getUnitsInStock();
        this.pricePerUnit = product.getPricePerUnit();
        this.discount = product.getDiscount();
        this.maximumUnitsInOneOrder = product.getMaximumUnitsInOneOrder();
        this.minimumUnitsInOneOrder = product.getMinimumUnitsInOneOrder();
        this.expiryDate = product.getExpiryDate();
        this.countryOfOrigin = product.getCountryOfOrigin();
        this.manufacturerName = product.getManufacturerName();
        this.isDeleted = product.getIsDeleted();
//		this.banner = product.getBanner();
        product.getProductCategories().forEach(pc -> {
            this.productCategories.add(new ProductCategoryBo(pc));
        });
        product.getProductSubCategories().forEach(pc -> {
            this.productSubCategories.add(new ProductSubCategoryBo(pc));
        });
        this.images.addAll(images);
        this.ManufacturingDate = product.getManufacturingDate();
        this.deliveredInSpecifiedRadius = product.getDeliveredInSpecifiedRadius();
        this.delieveredOutsideSpecifiedRadius = product.getDelieveredOutsideSpecifiedRadius();
        this.productUserId = product.getUser().getId();
        this.sequenceNumber = product.getSequenceNumber();
    }

    public ProductBo(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.unitsInStock = product.getUnitsInStock();
        this.pricePerUnit = product.getPricePerUnit();
        this.discount = product.getDiscount();
        this.maximumUnitsInOneOrder = product.getMaximumUnitsInOneOrder();
        this.minimumUnitsInOneOrder = product.getMinimumUnitsInOneOrder();
        this.expiryDate = product.getExpiryDate();
        this.countryOfOrigin = product.getCountryOfOrigin();
        this.manufacturerName = product.getManufacturerName();
        this.isDeleted = product.getIsDeleted();
        this.ManufacturingDate = product.getManufacturingDate();
//		this.banner = product.getBanner();
        product.getProductCategories().forEach(pc -> {
            this.productCategories.add(new ProductCategoryBo(pc));
        });
        product.getProductSubCategories().forEach(pc -> {
            this.productSubCategories.add(new ProductSubCategoryBo(pc));
        });
        this.deliveredInSpecifiedRadius = product.getDeliveredInSpecifiedRadius();
        this.delieveredOutsideSpecifiedRadius = product.getDelieveredOutsideSpecifiedRadius();
        this.productUserId = product.getUser().getId();
        this.sequenceNumber = product.getSequenceNumber();
    }

}
