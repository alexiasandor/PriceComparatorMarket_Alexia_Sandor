package com.PriceComparatorMarket.PriceComparatorMarket.services;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.DataPointDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.HistoryGraphsDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.PricePerUnitRequest;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.ProductDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders.DataPointBuilder;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders.ProductBuilder;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.DiscountRepository;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;

    }

    public ProductDto insertProduct(ProductDto productDto) {

        Product product = ProductBuilder.fromDtoToEntity(productDto);
        productRepository.save(product);
        return ProductBuilder.fromEntityToDto(product);
    }

    private float roundTwoDecimals(float value) {
        return Math.round(value * 100.0f) / 100.0f;
    }


    /**
     * Helper function to calculate de price with or without discount
     *
     * @param product - the product for which we calculate the price
     * @return
     */
    public float applyDiscount(Product product) {
        // extract the day when the product is available to apply the correct discount
        LocalDate productDay = product.getDate();
        String productId = product.getProductId();

        System.out.println("\n--- Checking discount for product: " + product.getProductName());
        System.out.println("Product ID: " + productId);
        System.out.println("Date: " + productDay);
        //search available discounts
        List<Discount> availableDiscounts = discountRepository.findByProductId(
                product.getProductId());

        if (availableDiscounts.isEmpty()) {
            System.out.println("No discounts found for product ID: " + productId);
            return product.getPrice();
        }
        //keep just available discounts for that date
        List<Discount> validDiscounts = availableDiscounts.stream()
                .filter(d -> !productDay.isBefore(d.getFromDate()) && !productDay.isAfter(d.getToDate()))
                .collect(Collectors.toList());
        System.out.println("Found " + validDiscounts.size() + " valid discounts for " + productDay);

        if (validDiscounts.isEmpty()) {
            return product.getPrice();
        }

        Discount selected = validDiscounts.stream()
                .max(Comparator.comparing(Discount::getFromDate))
                .orElse(null);
        System.out.println("Selected discount: " + selected.getPercentageOfDiscount() + "% from " + selected.getFromDate());

        if (selected == null) {
            return product.getPrice();
        }

        float discountPercent = selected.getPercentageOfDiscount();
        float discountedPrice = roundTwoDecimals(product.getPrice() * (1 - discountPercent / 100f));
        System.out.println("Original price: " + product.getPrice() + " → Discounted price: " + discountedPrice);

        return discountedPrice;
    }

    /**
     * This helper method filters products based on optional request parameters
     *
     * @param historyGraphs - the request which contain filters
     * @param startDate     -start date for OX axis
     * @return a JPA Specification with all applicable filters
     */
    public Specification<Product> helperToGetHistoryGraphs(HistoryGraphsDto historyGraphs, LocalDateTime startDate) {
        return (root, query, criteriaBuilder) -> {
            // list to save all filter conditions- it is like a list of conditions based on parameters from request
            List<Predicate> listOfPredicates = new ArrayList<>();

            if (historyGraphs.getProductName() != null) {
                listOfPredicates.add(criteriaBuilder.equal(root.get("productName"), historyGraphs.getProductName()));
            }
            if (historyGraphs.getBrand() != null) {
                listOfPredicates.add(criteriaBuilder.equal(root.get("brand"), historyGraphs.getBrand()));
            }
            if (historyGraphs.getStoreName() != null) {
                listOfPredicates.add(criteriaBuilder.equal(root.get("storeName"), historyGraphs.getStoreName()));
            }
            if (historyGraphs.getProductCategory() != null) {
                listOfPredicates.add(criteriaBuilder.equal(root.get("productCategory"), historyGraphs.getProductCategory()));
            }

            listOfPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate));
            // sorting result in ascending order by date
            query.orderBy(criteriaBuilder.asc(root.get("date")));
            // combine all predicates(all the posiblities)
            return criteriaBuilder.and(listOfPredicates.toArray(new Predicate[0]));
        };
    }

    /**
     * This method generates price history points for a given product with or withouth filters
     *
     * @param historyGraphsDto - request body
     * @return a list of DataPoints which contains price and data
     */
    public List<DataPointDto> getHistoryGraphs(HistoryGraphsDto historyGraphsDto) {
        // select the time range for ox axis
        LocalDateTime startDate = LocalDateTime.of(2025, 5, 1, 0, 0, 0);
        Specification<Product> filteredProducts = helperToGetHistoryGraphs(historyGraphsDto, startDate);
        List<Product> finalListOfProducts = productRepository.findAll(filteredProducts);
        List<DataPointDto> listOfResults = new ArrayList<>();

        // for every point, calculate price with discount applied and add it to the result list
        for (Product product : finalListOfProducts) {
            float finalPrice = applyDiscount(product);
            DataPointDto point = DataPointBuilder.fromProductToDto(product, finalPrice);
            listOfResults.add(point);
        }

        return listOfResults;
    }

    /**
     * This method
     *
     * @param pricePerUnitRequest request which contain the name of product
     * @return List of products with price per unit
     */
    public String getPricePerUnit(PricePerUnitRequest pricePerUnitRequest) {

        String currentProductName = pricePerUnitRequest.getProductName();
        List<Product> listOfProducts = productRepository.findByProductName(currentProductName);
        if (listOfProducts.isEmpty()) {
            return "No products found with name: " + currentProductName;
        }
        String result = "List of products with price per unit for: " + currentProductName +"\n";
        for (Product product : listOfProducts) {
            float unitValue = product.getPrice() / product.getPackageQuantity();
            result += "- " + product.getProductName() +
                    " from " + product.getStoreName() +
                    ": " + String.format("%.2f RON/%s", unitValue, product.getPackageUnit()) +
                    " (valid from" + product.getDate() + ")\n";

        }
        return result;
    }
}
