package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Setter
@Getter
public class BasketCreateDto {
    private LocalDate date;
    private List<BasketItemInput> productList;

    @Getter
    @Setter
    public static class BasketItemInput {
        private String productName;
        private String brand;
        private String storeName;
    }

}
