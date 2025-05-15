package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BestDiscountPerDayReplyDto {
    private String productName;
    private String brand;
    private String storeName;
    private int percentageOfDiscount;
}
