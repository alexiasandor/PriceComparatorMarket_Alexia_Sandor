package com.PriceComparatorMarket.PriceComparatorMarket.repositories;

import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    boolean existsByProductIdAndFromDate(String productId, LocalDate fromDate);
    List<Discount> findByIdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            int id, LocalDate from, LocalDate to);

    List<Discount> findByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDate from, LocalDate to);
    List<Discount>findByCreationDayBetween(LocalDateTime daily, LocalDateTime currentDateTime);
    List<Discount> findByProductId(String productId);
}