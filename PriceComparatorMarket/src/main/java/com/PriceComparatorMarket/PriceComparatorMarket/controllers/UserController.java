package com.PriceComparatorMarket.PriceComparatorMarket.controllers;

import com.PriceComparatorMarket.PriceComparatorMarket.dtos.UserDto;
import com.PriceComparatorMarket.PriceComparatorMarket.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/insert")
    public ResponseEntity<UserDto> insertUser(@RequestBody UserDto userDto){
        UserDto result = userService.insert(userDto);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/getMessage/{id}")
    public ResponseEntity<List<String>> getUserMessage(@PathVariable("id") int userId){
        List<String> message = userService.getUserMessage(userId);
        return ResponseEntity.ok(message);
    }
}
