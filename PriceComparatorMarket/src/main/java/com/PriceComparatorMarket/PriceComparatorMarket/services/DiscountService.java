package com.PriceComparatorMarket.PriceComparatorMarket.services;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DiscountDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders.DiscountBuilder;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public DiscountDto insertDiscount(DiscountDto discountDto){

        Discount discount = DiscountBuilder.fromDtoToEntity(discountDto);
        discountRepository.save(discount);
        return DiscountBuilder.fromEntityToDto(discount);
    }
    public String getBestDiscounts(LocalDate date){
    // find all the active discounts from the current day
    List<Discount> activeDiscountsList = discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(date, date);

       // keep the newer discount
        Map<String, Discount> bestDiscounts = new HashMap<>();

        for (Discount d : activeDiscountsList) {
            String key = d.getProductId();
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

    public String getNewlyAdded(LocalDateTime currentDateTime) {
        //we get the date 24 hours ago the current one
        LocalDateTime daily = currentDateTime.minusHours(24);
        //store all the discounts which fulfill the requirement in a list
        List<Discount> newDiscounts = discountRepository.findByCreationDayBetween(daily, currentDateTime);

        if (newDiscounts.isEmpty()) {
            return " No discounts were added between " + daily + " and " + currentDateTime + ".";
        }

        StringBuilder sb = new StringBuilder("Discounts added in the last 24h before " + currentDateTime + ":\n\n");

        for (Discount d : newDiscounts) {
            sb.append(" ")
                    .append(d.getProductName()).append(" (").append(d.getBrand()).append(") - ")
                    .append(d.getStoreName()).append(": ")
                    .append(d.getPercentageOfDiscount()).append("% off\n");
        }

        return sb.toString();
    }
}
