package com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.UserDto;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.User;

import java.util.ArrayList;

public class UserBuilder {
    public static User fromDtoToEntity(UserDto userDto){
        return User.builder()
                .userName(userDto.getUserName())
                .messageFromAlerts(userDto.getMessageFromAlerts())
                .listOfAlerts(new ArrayList<>())
                .build();
    }

    public static UserDto fromEntityToDto(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .messageFromAlerts(user.getMessageFromAlerts())
                .build();
    }

}
