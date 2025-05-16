package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataPointDto {
    private LocalDate date;
    private float price;
}
