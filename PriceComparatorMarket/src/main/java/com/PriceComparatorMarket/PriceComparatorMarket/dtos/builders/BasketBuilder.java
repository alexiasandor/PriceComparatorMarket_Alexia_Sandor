package com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.BasketDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Basket;

public class BasketBuilder {
    public static Basket fromDtoToEntity(BasketDto basketDto) {
        return Basket.builder()
                .basketId(basketDto.getBasketId())
                .date(basketDto.getDate())
                .productList(basketDto.getProductList())
                .build();
    }

    public static BasketDto fromEntityToDto(Basket basket) {
        return BasketDto.builder()
                .basketId(basket.getBasketId())
                .date(basket.getDate())
                .basketPrice(basket.getBasketPrice())
                .productList(basket.getProductList())
                .build();
    }
}
