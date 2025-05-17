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
     * @param basketId - id of the basket
     * @return Basket-object or runTimeException message if the basket is not find
     */
    private Basket getBasketById(int basketId) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            throw new RuntimeException("Basket with ID " + basketId + " was not found.");
        }
        return optionalBasket.get();
    }

    /**
     * Helper method to find in which day-category we fit
     * @param date- curent date
     * @return the period in which we fall: before 08-05 or after
     */
    private LocalDate resolveReferenceDate(LocalDate date) {
        return date.isBefore(LocalDate.of(2025, 5, 8))
                ? LocalDate.of(2025, 5, 1)
                : LocalDate.of(2025, 5, 8);
    }

    /**
     * Helper method to find a product by name day it fits in
     * @param productName - name of the product we are searching for
     * @param date - date when the product is available
     * @return list of product which are a good alternative
     */
    private List<Product> getAlternativeForProduct(String productName, LocalDate date) {
        return productRepository.findByProductNameAndDate(productName, date);
    }


    /**
     * Hepler method to create raport : quantity-price
     * @param products - list of products
     * @return the product with the lowest price per unit
     */
    private Product chooseBestProduct(List<Product> products) {
        return products.stream()
                .min(Comparator.comparingDouble(p -> p.getPrice() / p.getPackageQuantity()))
                .orElse(null);
    }

    /**
     * Update basket
     * @param basket - current basket
     * @param optimizedProductList - list with optimized products
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

    /**
     *This method is a helper to format the messages we return
     */
    private String formatBasketMessage(Map<String, List<Product>> grouped, LocalDate date, List<Product> originalListOfProducts, float newBasketPrice, float oldBasketPrice) {
        String result = "Unoptimized basket for:\n";

        for (Product p : originalListOfProducts) {
            result += "- " + p.getStoreName() + ": " +
                    p.getProductName() + " (" + p.getBrand() + "), " +
                    String.format("%.2f", p.getPrice()) + " " + p.getCurrency() + "\n";
        }
        result += "\nUnoptimized basket value : " + String.format("%.2f RON", oldBasketPrice) + "\n";

        result += "\nOptimized basket for date: " + date + ":\n";
        for (Map.Entry<String, List<Product>> entry : grouped.entrySet()) {
            result += "- " + entry.getKey() + ": ";

            String productsLine = entry.getValue().stream()
                    .map(p -> p.getProductName() + " (" + p.getBrand() + "), " +
                            String.format("%.2f", p.getPrice()) + " RON")
                    .collect(Collectors.joining(" | "));

            result += productsLine + "\n";
        }
        result += "Optimized basket value : " + String.format("%.2f RON", newBasketPrice);
        return result;
    }
    private List<Product> findProductsForRequest(BasketCreateDto request, LocalDate referenceDate) {
        List<Product> products = new ArrayList<>();

        for (BasketCreateDto.BasketItemInput item : request.getProductList()) {
            Optional<Product> product = productRepository.findByProductNameAndBrandAndStoreNameAndDate(
                    item.getProductName(),
                    item.getBrand(),
                    item.getStoreName(),
                    referenceDate
            );

            if (product.isEmpty()) {
                System.out.println("Product not found: " + item.getProductName()
                        + " / " + item.getBrand() + " / " + item.getStoreName() + " / " + referenceDate);
            }

            product.ifPresent(products::add);
        }

        return products;
    }

    /**
     * This method calculates basket total price
     * @param products list of products
     * @return price
     */
    private float calculateBasketPrice(List<Product> products) {
        return (float) products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    /**
     * This method create a basket
     * @param request - request containing date and product list
     * @return  the created baske
     */
    public BasketDto createBasket(BasketCreateDto request) {
        LocalDate basketDate = request.getDate();
        LocalDate referenceDate = resolveReferenceDate(basketDate);
        //search products from request and save in a list
        List<Product> products = findProductsForRequest(request, referenceDate);
        //compute the price
        float basketPrice = calculateBasketPrice(products);

        Basket basket = BasketBuilder.buildBasket(basketDate, products, basketPrice);
        basketRepository.save(basket);

        return BasketBuilder.fromEntityToDto(basket);

    }

    /**
     *  Method for first feature - optimize the basket
     * @param basketId - id of the basket we want to ptimize
     * @return - returns a message with the contents of the cart divided into the shopping lists in each store
     */
    public String  optimizeBasket(int basketId) {
        Map<String, List<Product>> storeMap = new HashMap<>();
        List<Product> optimizedProductList = new ArrayList<>();
        // get current basket
        Basket basket = getBasketById(basketId);
        float oldBasketPrice = basket.getBasketPrice();
        System.out.println("old price " + oldBasketPrice);
        // extract date
        LocalDate basketDate = basket.getDate();
        //the period in which we are related to the availability of products
        LocalDate referenceDate = resolveReferenceDate(basketDate);

        // get the list of products from unoptimized basket
        List<Product> originalProducts = new ArrayList<>(basket.getProductList());


        // find the best alternative for every product from list
        for (Product product : basket.getProductList()) {
            // find all alternatives
            List<Product> alternativeProductList = getAlternativeForProduct(product.getProductName(), referenceDate);
            // apply discounts
            List<Product> allVariantsWithDiscounts = new ArrayList<>();
            for (Product p : alternativeProductList) {
                allVariantsWithDiscounts.addAll(applyAllDiscounts(p, basketDate));
            }
            // choose the best product
            Product best = chooseBestProduct(allVariantsWithDiscounts);
            // add to the corresponding store list
            if (best != null) {
                optimizedProductList.add(best);
                storeMap.computeIfAbsent(best.getStoreName(), s -> new ArrayList<>()).add(best);
            }
        }

        // update current basket
        Basket newBasket = updateBasketWithOptimizedProducts(basket, optimizedProductList);
        float basketPrice = newBasket.getBasketPrice();
        // returns a message with the contents of the cart divided into the shopping lists in each store
        return formatBasketMessage(storeMap, basketDate, originalProducts, basketPrice, oldBasketPrice);
    }


}
