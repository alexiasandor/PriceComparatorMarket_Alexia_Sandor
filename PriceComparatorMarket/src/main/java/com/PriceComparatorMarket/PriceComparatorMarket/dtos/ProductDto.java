package com.PriceComparatorMarket.PriceComparatorMarket.dtos;

import com.PriceComparatorMarket.PriceComparatorMarket.entities.Basket;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private String productId;
    private String productName;
    private String productCategory;
    private String brand;
    private float packageQuantity;
    private String packageUnit;
    private float price;
    private String currency;
    private String storeName;
    private LocalDate date;
    List<Basket> baskets;

}
