package com.PriceComparatorMarket.PriceComparatorMarket.controllers;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DayRequestDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DayTimeRequestDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DiscountDto;
import com.PriceComparatorMarket.PriceComparatorMarket.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/discount")
public class DiscountController {
    private final DiscountService discountService;
    @Autowired
    public DiscountController(DiscountService discountService){
        this.discountService = discountService;
    }


    @PostMapping("/insertDiscount")
    public ResponseEntity<DiscountDto>insertDiscount(@RequestBody DiscountDto request){
        DiscountDto discount = discountService.insertDiscount(request);
        return ResponseEntity.ok(discount);
    }
    @PostMapping("/bestDiscounts")
    public ResponseEntity<String> getBestDiscounts(@RequestBody DayRequestDto request) {
        String result = discountService.getBestDiscounts(request.getDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/newlyAdded")
    public ResponseEntity<String> getNewlyAdded(@RequestBody DayTimeRequestDto request) {
        String result = discountService.getNewlyAdded(request.getDateTime());
        return ResponseEntity.ok(result);
    }
}
