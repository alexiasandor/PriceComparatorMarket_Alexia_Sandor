package com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.ProductDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;

public class ProductBuilder {
    public static Product fromDtoToEntity(ProductDto productDto) {
        return Product.builder()
                .productId(productDto.getProductId())
                .productName(productDto.getProductName())
                .productCategory(productDto.getProductCategory())
                .brand(productDto.getBrand())
                .packageQuantity(productDto.getPackageQuantity())
                .packageUnit(productDto.getPackageUnit())
                .price(productDto.getPrice())
                .currency(productDto.getCurrency())
                .storeName(productDto.getStoreName())
                .date(productDto.getDate())
                .build();
    }

    public static ProductDto fromEntityToDto(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productCategory(product.getProductCategory())
                .brand(product.getBrand())
                .packageQuantity(product.getPackageQuantity())
                .packageUnit(product.getPackageUnit())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .storeName(product.getStoreName())
                .date(product.getDate())
                .build();
    }
}
