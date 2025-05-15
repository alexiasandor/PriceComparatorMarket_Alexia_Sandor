package com.PriceComparatorMarket.PriceComparatorMarket.services;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.BasketCreateDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.BasketDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders.BasketBuilder;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Basket;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.BasketRepository;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.DiscountRepository;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    @Autowired
    public BasketService( BasketRepository basketRepository, ProductRepository productRepository, DiscountRepository discountRepository){
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    /**
     * This method return a basket by id
     * @param basketId
     * @return
     */
    private Basket getBasketById(int basketId) {
        return basketRepository.findById(basketId)
                .orElseThrow(() -> new RuntimeException("Basket not found"));
    }

    /**
     * Helper method to find in which day-category we fit
     * @param date
     * @return
     */
    private LocalDate resolveReferenceDate(LocalDate date) {
        return date.isBefore(LocalDate.of(2025, 5, 8))
                ? LocalDate.of(2025, 5, 1)
                : LocalDate.of(2025, 5, 8);
    }

    /**
     * Helper method to find a product by name day it fits in
     * @param productName
     * @param date
     * @return
     */
    private List<Product> getAlternativeForProduct(String productName, LocalDate date) {
        return productRepository.findByProductNameAndDate(productName, date);
    }


    /**
     * Hepler method to create raport : quantity-price
     * @param products
     * @return
     */
    private Product chooseBestProduct(List<Product> products) {
        return products.stream()
                .min(Comparator.comparingDouble(p -> p.getPrice() / p.getPackageQuantity()))
                .orElse(null);
    }

    /**
     * Update basket
     * @param basket
     * @param optimizedProductList
     */
    private Basket updateBasketWithOptimizedProducts(Basket basket, List<Product> optimizedProductList) {
        basket.setProductList(optimizedProductList);
        float totalPrice = (float) optimizedProductList.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        basket.setBasketPrice(totalPrice);
        return basketRepository.save(basket);
    }
    private Product cloneProduct(Product original) {
        Product p = new Product();
        p.setId(original.getId());
        p.setProductId(original.getProductId());
        p.setProductName(original.getProductName());
        p.setBrand(original.getBrand());
        p.setStoreName(original.getStoreName());
        p.setPackageQuantity(original.getPackageQuantity());
        p.setPackageUnit(original.getPackageUnit());
        p.setCurrency(original.getCurrency());
        p.setDate(original.getDate());
        p.setPrice(original.getPrice());
        return p;
    }

    private List<Product> applyAllDiscounts(Product product, LocalDate basketDate) {
        List<Product> results = new ArrayList<>();

        results.add(product); // add original product

        List<Discount> discounts = discountRepository
                .findByIdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                        product.getId(), basketDate, basketDate);


        for (Discount discount : discounts) {
            Product copy = cloneProduct(product);

            double percent = discount.getPercentageOfDiscount();
            double reducedPrice = product.getPrice() * (1 - percent / 100.0);

            reducedPrice = Math.round(reducedPrice * 100.0) / 100.0;

            copy.setPrice((float) reducedPrice);
            results.add(copy);
        }


        return results;
    }

    private String formatBasketMessage(Map<String, List<Product>> grouped, LocalDate date, List<Product> originalListOfProducts, float basketPrice) {
        StringBuilder sb = new StringBuilder("Unoptimized basket for :\n");
        for (Product p : originalListOfProducts) {
            sb.append("- ")
                    .append(p.getStoreName()).append(": ")
                    .append(p.getProductName()).append(" (").append(p.getBrand()).append("), ")
                    .append(p.getPrice()).append(" ")
                    .append(p.getCurrency());
        }

        sb.append("\n Optimized basket for data: ").append(date).append(":\n");
        for (Map.Entry<String, List<Product>> entry : grouped.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(": ");

            String productsLine = entry.getValue().stream()
                    .map(p -> p.getProductName() + " (" + p.getBrand() + "), " +
                            String.format("%.2f", p.getPrice()) + " RON")
                    .collect(Collectors.joining(" | "));

            sb.append(productsLine).append("\n");
        }


        sb.append("\n Basket value : ").append(String.format("%.2f RON", basketPrice));

        return sb.toString();
    }

    /**
     * This method create a basket
     * @param request
     * @return
     */
    public BasketDto createBasket(BasketCreateDto request) {
        LocalDate basketDate = request.getDate();
        LocalDate referenceDate = resolveReferenceDate(basketDate);

        List<Product> products = new ArrayList<>();

        for (BasketCreateDto.BasketItemInput item : request.getProductList()) {
            Optional<Product> p = productRepository.findByProductNameAndBrandAndStoreNameAndDate(
                    item.getProductName(),
                    item.getBrand(),
                    item.getStoreName(),
                    referenceDate
            );
            if (p.isEmpty()) {
                System.out.println("Product not found: " + item.getProductName()
                        + " / " + item.getBrand() + " / " + item.getStoreName() + " / " + referenceDate);
            }

            p.ifPresent(products::add); // if the product is found
        }
        float basketPrice = (float) products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        Basket basket = Basket.builder()
                .date(basketDate)
                .basketPrice(basketPrice)
                .productList(products)
                .build();


        basketRepository.save(basket);

        return BasketBuilder.fromEntityToDto(basket);

    }

    /**
     *  Method for first feature - optimize the basket
     * @param basketId - id of the basket we want to ptimize
     * @return
     */
    public String  optimizeBasket(int basketId) {
        Basket basket = getBasketById(basketId); // get current basket
        LocalDate basketDate = basket.getDate(); // extract date
        LocalDate referenceDate = resolveReferenceDate(basketDate);

        List<Product> originalProducts = new ArrayList<>(basket.getProductList()); // get the list of products from unoptimized basket
        Map<String, List<Product>> storeMap = new HashMap<>();
        List<Product> optimizedProductList = new ArrayList<>();

        for (Product product : basket.getProductList()) {
            List<Product> alternativeProductList = getAlternativeForProduct(product.getProductName(), referenceDate); // find alternative

            List<Product> allVariantsWithDiscounts = new ArrayList<>();
            for (Product p : alternativeProductList) {
                allVariantsWithDiscounts.addAll(applyAllDiscounts(p, basketDate));
            }

            Product best = chooseBestProduct(allVariantsWithDiscounts);

            if (best != null) {
                optimizedProductList.add(best);
                storeMap.computeIfAbsent(best.getStoreName(), s -> new ArrayList<>()).add(best);
            }
        }


        Basket newBasket = updateBasketWithOptimizedProducts(basket, optimizedProductList); // update current basket
        float basketPrice = newBasket.getBasketPrice();
        return formatBasketMessage(storeMap, basketDate, originalProducts, basketPrice);
    }


}
