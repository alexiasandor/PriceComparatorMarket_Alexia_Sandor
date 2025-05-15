package com.PriceComparatorMarket.PriceComparatorMarket.repositories;


import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByStoreName(String storeName);

    List<Product> findByDate(LocalDate date);

    List<Product> findByStoreNameAndDate(String storeName, LocalDate date);
    boolean existsByProductIdAndDate(String productId, LocalDate date);
    Optional<Product> findByProductNameAndBrandAndStoreNameAndDate(String name, String brand, String store, LocalDate date);
    List<Product> findByProductNameAndDate(String productName, LocalDate date);
    boolean existsByProductIdAndDateAndStoreName(String productId, LocalDate date, String storeName);


}
