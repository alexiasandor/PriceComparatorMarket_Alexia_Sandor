package com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.BestDiscountPerDayReplyDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;

public class BestDiscountsPerDayBuilder {
    public static BestDiscountPerDayReplyDto fromDiscountEntityToBestDiscountPerDayDto(Discount discount) {
        return BestDiscountPerDayReplyDto.builder()
                .productName(discount.getProductName())
                .brand(discount.getBrand())
                .storeName(discount.getStoreName())
                .percentageOfDiscount(discount.getPercentageOfDiscount())
                .build();
    }

}
