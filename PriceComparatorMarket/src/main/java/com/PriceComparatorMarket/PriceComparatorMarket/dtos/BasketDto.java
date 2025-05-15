package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import com.PriceComparatorMarket.PriceComparatorMarket.entities.Basket;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketDto {
    private int basketId;
    private LocalDate date;
    private float basketPrice;
    private List<Product> productList;

}
