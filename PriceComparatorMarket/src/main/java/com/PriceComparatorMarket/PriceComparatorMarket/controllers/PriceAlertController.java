package com.PriceComparatorMarket.PriceComparatorMarket.controllers;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.PriceAlertRequestDto;
import com.PriceComparatorMarket.PriceComparatorMarket.services.PriceAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/priceAlert")
public class PriceAlertController {
    private final PriceAlertService priceAlertService;
    @Autowired
    public PriceAlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @PostMapping("/createPriceAlert")
    public ResponseEntity<String> createPriceAlert(@RequestBody PriceAlertRequestDto priceAlertRequestDto){
        String result = priceAlertService.createPriceAlert(priceAlertRequestDto);
        return ResponseEntity.ok(result);

    }
}
