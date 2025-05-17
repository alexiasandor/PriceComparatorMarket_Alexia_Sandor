package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceAlertRequestDto {
    private String productName;
    private String brand;
    private float targetPrice;
    private int userId;
}
