package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import com.PriceComparatorMarket.PriceComparatorMarket.entities.PriceAlert;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {
    private int userId;
    private String userName;
    private String messageFromAlerts;
    private List<PriceAlert> listOfAlerts;
}
