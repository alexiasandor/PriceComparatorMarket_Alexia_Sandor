package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayRequestDto {
    private LocalDate date;
}
