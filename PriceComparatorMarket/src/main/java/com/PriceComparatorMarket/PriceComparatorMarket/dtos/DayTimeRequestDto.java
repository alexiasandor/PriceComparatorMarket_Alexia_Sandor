package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayTimeRequestDto {
    private LocalDateTime dateTime;

}
