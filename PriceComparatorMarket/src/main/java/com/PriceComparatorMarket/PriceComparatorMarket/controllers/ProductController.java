package com.PriceComparatorMarket.PriceComparatorMarket.controllers;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DataPointDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DiscountDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.HistoryGraphsDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.ProductDto;
import com.PriceComparatorMarket.PriceComparatorMarket.services.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/product")

public class ProductController {

    private ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/insertProduct")
    public ResponseEntity<ProductDto>insertProduct(@RequestBody ProductDto request){
        ProductDto product = productService.insertProduct(request);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/historyGraphs")
    public ResponseEntity<List<DataPointDto>> getHistoryGraphs(@RequestBody HistoryGraphsDto historyGraphsDto){
        List<DataPointDto> dataPointDto = productService.getHistoryGraphs(historyGraphsDto);
        return ResponseEntity.ok(dataPointDto);
    }
}
