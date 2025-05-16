package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryGraphRequestDto {
    private String productName;
    private String brand;
    private String storeName;
    private String productCategory;
    private LocalDateTime dateTime;
}
