package com.PriceComparatorMarket.PriceComparatorMarket.services;

import com.PriceComparatorMarket.PriceComparatorMarket.entities.Product;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.Discount;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.ProductRepository;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.DiscountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class DataLoaderService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    @Autowired
    public DataLoaderService(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    @PostConstruct
    public void loadCsvData() {
        try {
            Path folder = Paths.get("src/main/resources/data");

            if (Files.exists(folder)) {
                Files.list(folder)
                        .filter(path -> path.toString().endsWith(".csv"))
                        .forEach(path -> {
                            String fileName = path.getFileName().toString();

                            if (fileName.contains("discount")) {
                                loadDiscountFile(path);
                            } else {
                                loadProductFile(path);
                            }
                        });
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void loadProductFile(Path filePath) {
        try {
            String fileName = filePath.getFileName().toString();
            String[] nameParts = fileName.replace(".csv", "").split("_");
            String store = nameParts[0];
            LocalDate date = LocalDate.parse(nameParts[1]);

            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                String[] t = lines.get(i).split(";");

                String productId = t[0];
                if (!productRepository.existsByProductIdAndDateAndStoreName(productId, date, store)) {
                    Product p = new Product();
                    p.setProductId(productId);
                    p.setProductName(t[1]);
                    p.setProductCategory(t[2]);
                    p.setBrand(t[3]);
                    p.setPackageQuantity(Float.parseFloat(t[4]));
                    p.setPackageUnit(t[5]);
                    p.setPrice(Float.parseFloat(t[6]));
                    p.setCurrency(t[7]);
                    p.setStoreName(store);
                    p.setDate(date);

                    productRepository.save(p);
                }
            }

            System.out.println("Loaded products from: " + fileName);

        } catch (Exception e) {
            System.err.println("Error loading products from " + filePath + ": " + e.getMessage());
        }
    }

    private void loadDiscountFile(Path filePath) {
        try {
            String fileName = filePath.getFileName().toString();
            String store = fileName.split("_")[0];

            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                String[] t = lines.get(i).split(";");

                String productId = t[0];
                LocalDate fromDate = LocalDate.parse(t[6]);

                if (!discountRepository.existsByProductIdAndFromDate(productId, fromDate)) {
                    Discount d = new Discount();
                    d.setProductId(productId);
                    d.setProductName(t[1]);
                    d.setBrand(t[2]);
                    d.setPackageQuantity(Double.parseDouble(t[3]));
                    d.setPackageUnit(t[4]);
                    d.setProductCategory(t[5]);
                    d.setFromDate(fromDate);
                    d.setToDate(LocalDate.parse(t[7]));
                    d.setPercentageOfDiscount(Integer.parseInt(t[8].trim()));
                    d.setStoreName(store);

                    discountRepository.save(d);
                }
            }

            System.out.println("Loaded discounts from: " + fileName);

        } catch (Exception e) {
            System.err.println("Error loading discounts from " + filePath + ": " + e.getMessage());
        }
    }
}
