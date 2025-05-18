package com.PriceComparatorMarket.PriceComparatorMarket.services;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.PriceAlertRequestDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.ProductPriceUpdateRequest;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders.PriceAlertBuilder;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.PriceAlert;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.User;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.PriceAlertRepository;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PriceAlertService {
    private final UserRepository userRepository;
    private final PriceAlertRepository priceAlertRepository;
@Autowired
    public PriceAlertService(UserRepository userRepository, PriceAlertRepository priceAlertRepository) {
        this.userRepository = userRepository;
        this.priceAlertRepository = priceAlertRepository;
    }

    /**
     * This method creates a new alert
     * @param priceAlertRequestDto alert details used to create a new one
     * @return return a confirmation message
     */
    public String createPriceAlert(PriceAlertRequestDto priceAlertRequestDto){
      //extract user using its id
        int currentUserId = priceAlertRequestDto.getUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PriceAlert newAlert = PriceAlertBuilder.fromDtoToEntity(priceAlertRequestDto, user);
        priceAlertRepository.save(newAlert);
        return "An alert has been created by "+ user.getUserName()
                + " for the product "+ priceAlertRequestDto.getProductName();
    }


    /**
     * This method check the new price (updated grant price any alerts)
     * and send a message to the user who set that alert
     * @param productPriceUpdateRequest - request details
     */
    public void priceAlert(ProductPriceUpdateRequest productPriceUpdateRequest){
       float newPriceForProduct = productPriceUpdateRequest.getNewPrice();
      //search if we find the product in alert list
        List<PriceAlert> listOfAlertsForCurrentProduct = priceAlertRepository.findByProductNameAndBrand(productPriceUpdateRequest.getProductName(), productPriceUpdateRequest.getBrand());
      // check for price condition
       for(PriceAlert alert : listOfAlertsForCurrentProduct){
           if(newPriceForProduct <= alert.getTargetPrice()){
               User currentUser = alert.getUser();
               currentUser.setMessageFromAlerts("\n This product fulfill your price requirement. Buy it now!\n");
               userRepository.save(currentUser);
               System.out.println(currentUser.getMessageFromAlerts());
           }
       }
    }
}
