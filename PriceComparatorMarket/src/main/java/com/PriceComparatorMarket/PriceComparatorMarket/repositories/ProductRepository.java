package com.PriceComparatorMarket.PriceComparatorMarket.repositories;


import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByProductNameAndBrandAndStoreNameAndDate(String name, String brand, String store, LocalDate date);
    List<Product> findByProductNameAndDate(String productName, LocalDate date);
    boolean existsByProductIdAndDateAndStoreName(String productId, LocalDate date, String storeName);
    List<Product> findByProductName(String name);

}
