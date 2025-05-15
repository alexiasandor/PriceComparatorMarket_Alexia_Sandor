package com.PriceComparatorMarket.PriceComparatorMarket.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name= "product_id", nullable = false)
    private String productId;
    @Column(name= "product_name", nullable = false)
    private String productName;
    @Column(name= "brand", nullable = false)
    private String brand;
    @Column(name= "package_quantity", nullable = false)
    private double packageQuantity;
    @Column(name= "package_unit", nullable = false)
    private String packageUnit;
    @Column(name= "product_category", nullable = false)
    private String productCategory;
    @Column(name= "fromDate", nullable = false)
    private LocalDate fromDate;
    @Column(name= "toDate", nullable = false)
    private LocalDate toDate;
    @Column(name= "percentage_of_Discount", nullable = false)
    private int percentageOfDiscount;
    @Column(name= "storeName", nullable = false)
    private String storeName;
    @Column(name= "creationDay", nullable = false)
    private LocalDateTime creationDay;
}
