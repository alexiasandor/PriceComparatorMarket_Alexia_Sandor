package com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DataPointDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;

public class DataPointBuilder {
    public static DataPointDto fromProductToDto(Product product, Float finalPrice) {
        return DataPointDto.builder()
                .date(product.getDate())
                .price(finalPrice)
                .build();
    }

}
