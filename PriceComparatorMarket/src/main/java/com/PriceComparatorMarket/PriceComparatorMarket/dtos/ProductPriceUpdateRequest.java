package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPriceUpdateRequest {
    private String productName;
    private String brand;
    private String storeName;
    private LocalDate date;
    private float newPrice;
}
