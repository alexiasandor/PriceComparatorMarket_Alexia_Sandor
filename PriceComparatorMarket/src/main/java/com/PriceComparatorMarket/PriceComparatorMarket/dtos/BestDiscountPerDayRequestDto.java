package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BestDiscountPerDayRequestDto {
    private LocalDate date;
}
