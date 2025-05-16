package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryGraphsDto {
    private String productName;
    private String brand;
    private String storeName;
    private String productCategory;
}
