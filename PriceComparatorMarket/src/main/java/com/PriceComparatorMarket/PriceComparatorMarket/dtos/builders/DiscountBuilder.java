package com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DiscountDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;

public class DiscountBuilder {
    public static Discount fromDtoToEntity(DiscountDto discountDto) {
        return Discount.builder()
                .id(discountDto.getId())
                .productId(discountDto.getProductId())
                .productName(discountDto.getProductName())
                .brand(discountDto.getBrand())
                .packageQuantity(discountDto.getPackageQuantity())
                .packageUnit(discountDto.getPackageUnit())
                .productCategory(discountDto.getProductCategory())
                .fromDate(discountDto.getFromDate())
                .toDate(discountDto.getToDate())
                .percentageOfDiscount(discountDto.getPercentageOfDiscount())
                .storeName(discountDto.getStoreName())
                .build();
    }

    public static DiscountDto fromEntityToDto(Discount discount) {
        return DiscountDto.builder()
                .id(discount.getId())
                .productId(discount.getProductId())
                .productName(discount.getProductName())
                .brand(discount.getBrand())
                .packageQuantity(discount.getPackageQuantity())
                .packageUnit(discount.getPackageUnit())
                .productCategory(discount.getProductCategory())
                .fromDate(discount.getFromDate())
                .toDate(discount.getToDate())
                .percentageOfDiscount(discount.getPercentageOfDiscount())
                .storeName(discount.getStoreName())
                .build();
    }
}
