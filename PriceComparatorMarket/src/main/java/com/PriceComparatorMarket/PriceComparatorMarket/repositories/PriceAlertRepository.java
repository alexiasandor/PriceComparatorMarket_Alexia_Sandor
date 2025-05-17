package com.PriceComparatorMarket.PriceComparatorMarket.repositories;

import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Integer> {
    List<PriceAlert> findByProductNameAndBrand(String productName, String brand);
}
