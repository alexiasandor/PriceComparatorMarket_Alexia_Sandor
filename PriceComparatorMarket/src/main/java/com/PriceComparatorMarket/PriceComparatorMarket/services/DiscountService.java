package com.PriceComparatorMarket.PriceComparatorMarket.services;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DiscountDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscountService {
    private final DiscountRepository discountRepository;
    @Autowired
    public DiscountService(DiscountRepository discountRepository){
        this.discountRepository = discountRepository;
    }

    public String getBestDiscounts(LocalDate date){
    // find all the active discounts from the current day
    List<Discount> activeDiscountsList = discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(date, date);

       // keep the newer discount
        Map<String, Discount> bestDiscounts = new HashMap<>();

        for (Discount d : activeDiscountsList) {
            String key = d.getProductId(); // doar pe produs, nu pe store
            Discount current = bestDiscounts.get(key);

            if (current == null
                    || d.getPercentageOfDiscount() > current.getPercentageOfDiscount()
                    || (d.getPercentageOfDiscount() == current.getPercentageOfDiscount()
                    && d.getFromDate().isAfter(current.getFromDate()))) {
                bestDiscounts.put(key, d);
            }
        }
         // return as string
        StringBuilder sb = new StringBuilder("Best discounts for: ").append(date).append("\n\n");

        bestDiscounts.values().stream()
                .sorted(Comparator.comparingInt(Discount::getPercentageOfDiscount).reversed())
                .forEach(d -> sb.append(" ")
                        .append(d.getProductName()).append(" (").append(d.getBrand()).append(") - ")
                        .append(d.getStoreName()).append(" → ")
                        .append(d.getPercentageOfDiscount()).append("%\n")
                );

        return sb.toString();
    }
}
