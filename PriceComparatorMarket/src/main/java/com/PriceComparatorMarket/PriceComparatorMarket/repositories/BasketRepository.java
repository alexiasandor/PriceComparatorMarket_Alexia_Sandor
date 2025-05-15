package com.PriceComparatorMarket.PriceComparatorMarket.repositories;

import com.PriceComparatorMarket.PriceComparatorMarket.entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Integer> {
}
