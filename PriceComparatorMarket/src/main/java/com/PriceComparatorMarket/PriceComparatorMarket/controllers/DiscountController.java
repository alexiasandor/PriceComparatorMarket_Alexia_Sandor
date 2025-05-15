package com.PriceComparatorMarket.PriceComparatorMarket.controllers;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.BestDiscountPerDayRequestDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DiscountDto;
import com.PriceComparatorMarket.PriceComparatorMarket.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/discount")
public class DiscountController {
    private final DiscountService discountService;
    @Autowired
    public DiscountController(DiscountService discountService){
        this.discountService = discountService;
    }

    @PostMapping("/bestDiscounts")
    public ResponseEntity<String> getBestDiscounts(@RequestBody BestDiscountPerDayRequestDto request) {
        String result = discountService.getBestDiscounts(request.getDate());
        return ResponseEntity.ok(result);
    }
}
