package com.PriceComparatorMarket.PriceComparatorMarket.controllers;


import com.PriceComparatorMarket.PriceComparatorMarket.dtos.BasketCreateDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.BasketDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.BasketRepository;
import com.PriceComparatorMarket.PriceComparatorMarket.services.BasketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/basket")

public class BasketController {
    private final BasketService basketService;
@Autowired
    public BasketController(BasketService basketService){
    this.basketService = basketService;
}

    @PostMapping("/create")
    public ResponseEntity<BasketDto> createBasket(@RequestBody BasketCreateDto request) {
        BasketDto basket = basketService.createBasket(request);
        return ResponseEntity.ok(basket);
    }

    @GetMapping("/optimize/{id}")
    public ResponseEntity<String> optimizeBasket(@PathVariable int id) {
        String message = basketService.optimizeBasket(id);
        return ResponseEntity.ok(message);
    }


}
