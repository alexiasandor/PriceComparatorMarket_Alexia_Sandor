package com.PriceComparatorMarket.PriceComparatorMarket.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="product_t")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name= "product_id", nullable = false)

    private String productId;
    @Column(name= "product_name", nullable = false)
    private String productName;
    @Column(name= "product_category", nullable = false)
    private String productCategory;
    @Column(name= "brand", nullable = false)
    private String brand;
    @Column(name= "package_quantity", nullable = false)
    private float packageQuantity;
    @Column(name= "package_unit", nullable = false)
    private String packageUnit;
    @Column(name= "price", nullable = false)
    private float price;
    @Column(name= "currency", nullable = false)
    private String currency;
    @Column(name= "storeName", nullable = false)
    private String storeName;
    @Column(name= "date", nullable = false)
    private LocalDate date;

    @ManyToMany(mappedBy = "productList", cascade = CascadeType.ALL)
    @JsonBackReference
    List<Basket> baskets;
}
