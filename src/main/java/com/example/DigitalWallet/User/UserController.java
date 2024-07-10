package com.example.DigitalWallet.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.DigitalWallet.AuthResponse.AuthResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;
     @PostMapping("auth/users/register")
    public String userRegistration(@RequestBody User userDetails) {
         return userService.userRegistration(userDetails);
    }
    
    @PostMapping("auth/users/login")
    public AuthResponse userLogin(@RequestBody User user) {
        return userService.loginuser(user);
    }
}
