package com.PriceComparatorMarket.PriceComparatorMarket.services;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.UserDto;
import com.PriceComparatorMarket.PriceComparatorMarket.dtos.builders.UserBuilder;
import com.PriceComparatorMarket.PriceComparatorMarket.entities.User;
import com.PriceComparatorMarket.PriceComparatorMarket.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto insert(UserDto userDto){
        User createdUser = UserBuilder.fromDtoToEntity(userDto);
        createdUser = userRepository.save(createdUser);
        return UserBuilder.fromEntityToDto(createdUser);

    }

    public List<String> getUserMessage(int userId){
        Optional<User> currentUser = userRepository.findById(userId);
        User user = currentUser.get();
        String messages = user.getMessageFromAlerts();
        return Arrays.stream(messages.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
