package com.PriceComparatorMarket.PriceComparatorMarket.repositories;

import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    boolean existsByProductIdAndFromDate(String productId, LocalDate fromDate);
    List<Discount> findByIdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            int id, LocalDate from, LocalDate to);

}