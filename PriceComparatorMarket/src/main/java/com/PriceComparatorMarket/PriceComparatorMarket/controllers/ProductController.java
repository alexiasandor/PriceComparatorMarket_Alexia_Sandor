package com.PriceComparatorMarket.PriceComparatorMarket.controllers;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.*;
import com.PriceComparatorMarket.PriceComparatorMarket.services.PriceAlertService;
import com.PriceComparatorMarket.PriceComparatorMarket.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/product")

public class ProductController {

    private final ProductService productService;
    private final PriceAlertService priceAlertService;
    @Autowired
    public ProductController(ProductService productService, PriceAlertService priceAlertService) {
        this.productService = productService;
        this.priceAlertService = priceAlertService;
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
    @PostMapping("/pricePerUnit")
    public ResponseEntity<String> getPricePerUnit(@RequestBody PricePerUnitRequest pricePerUnitRequest){
        String result = productService.getPricePerUnit(pricePerUnitRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/updatePrice")
    public ResponseEntity<String> updateProductPrice(@RequestBody ProductPriceUpdateRequest productPriceUpdateRequest){
        String result = productService.updatePrice(productPriceUpdateRequest);
        priceAlertService.priceAlert(productPriceUpdateRequest);
       return ResponseEntity.ok(result);
    }
}
