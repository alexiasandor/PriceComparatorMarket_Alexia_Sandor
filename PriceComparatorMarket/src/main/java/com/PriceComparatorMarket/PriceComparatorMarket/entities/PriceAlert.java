package com.PriceComparatorMarket.PriceComparatorMarket.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
public class PriceAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int alertId;
    @Column(name= "productName", nullable = false)
    private String productName;
    @Column(name= "brand", nullable = false)
    private String brand;
    @Column(name= "targetPrice", nullable = false)
    private float targetPrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
