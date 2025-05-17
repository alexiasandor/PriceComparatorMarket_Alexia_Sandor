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
import java.util.stream.Collectors;

@Service
public class DiscountService {
    private final DiscountRepository discountRepository;
    @Autowired
    public DiscountService(DiscountRepository discountRepository){
        this.discountRepository = discountRepository;
    }

    /**
     * This method creates a new discount
     * @param discountDto - request with appropriate fields to create a new discount
     * @return the new discount
     */
    public DiscountDto insertDiscount(DiscountDto discountDto){

        Discount discount = DiscountBuilder.fromDtoToEntity(discountDto);
        discountRepository.save(discount);
        return DiscountBuilder.fromEntityToDto(discount);
    }

    /**
     * This method returns a list sorted in descending order by discount percentage
     * @param date date for which to retrieve active discounts
     * @return listing with the  best available discounts on the specified date
     */
    public String getBestDiscounts(LocalDate date){
    // find all the active discounts from the current day
    List<Discount> activeDiscountsList = discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(date, date);

       // keep the newer discount for every product
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
        String discountsReport = bestDiscounts.values().stream()
                .sorted(Comparator.comparingInt(Discount::getPercentageOfDiscount).reversed())
                .map(d -> String.format(" %s (%s) - %s → %d%% [from %s to %s]",
                        d.getProductName(),
                        d.getBrand(),
                        d.getStoreName(),
                        d.getPercentageOfDiscount(),
                        d.getFromDate(),
                        d.getToDate()))
                .collect(Collectors.joining("\n"));

        return String.format("Best discounts for: %s\n\n%s", date, discountsReport);

    }

    /**
     * This method returns a list of all discounts that were added in the last 24 hours
     * If no new discounts are found, a message indicating this is returned
     * @param currentDateTime  date and time used as the upper limit
     * @return string listing newly added discounts within the last 24 hours
     */
    public String getNewlyAdded(LocalDateTime currentDateTime) {
        //we get the date 24 hours ago the current one
        LocalDateTime daily = currentDateTime.minusHours(24);
        //store all the discounts which fulfill the requirement in a list
        List<Discount> newDiscounts = discountRepository.findByCreationDayBetween(daily, currentDateTime);

        if (newDiscounts.isEmpty()) {
            return " No discounts were added between " + daily + " and " + currentDateTime + ".";
        }

        String discountDetails = newDiscounts.stream()
                .map(d -> String.format(" %s (%s) - %s: %d%% off",
                        d.getProductName(),
                        d.getBrand(),
                        d.getStoreName(),
                        d.getPercentageOfDiscount()))
                .collect(Collectors.joining("\n"));

        return String.format("Discounts added in the last 24h before %s:\n\n%s", currentDateTime, discountDetails);
    }
}
