package com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.PriceAlertRequestDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.PriceAlert;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.User;

public class PriceAlertBuilder {
    public static PriceAlert fromDtoToEntity(PriceAlertRequestDto priceAlertRequestDto, User user){
        return PriceAlert.builder()
                .productName(priceAlertRequestDto.getProductName())
                .brand(priceAlertRequestDto.getBrand())
                .targetPrice(priceAlertRequestDto.getTargetPrice())
                .user(user)
                .build();
    }
}
