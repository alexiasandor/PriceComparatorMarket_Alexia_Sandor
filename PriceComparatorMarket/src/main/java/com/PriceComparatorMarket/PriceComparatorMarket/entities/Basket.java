package com.PriceComparatorMarket.PriceComparatorMarket.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int basketId;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "basket_Price", nullable = false)
    private float basketPrice;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="product",
               joinColumns = @JoinColumn(name="basketId"),
               inverseJoinColumns = @JoinColumn(name="productId"))
    private List<Product> productList;
}
