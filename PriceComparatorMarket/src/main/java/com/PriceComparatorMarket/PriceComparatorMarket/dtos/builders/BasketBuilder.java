package com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.BasketDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Basket;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;

import java.time.LocalDate;
import java.util.List;

public class BasketBuilder {
    public static Basket buildBasket(LocalDate date, List<Product> products, float totalPrice) {
        return Basket.builder()
                .date(date)
                .basketPrice(totalPrice)
                .productList(products)
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
