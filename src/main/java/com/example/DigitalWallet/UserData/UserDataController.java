package com.example.DigitalWallet.UserData;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.DigitalWallet.User.User;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
public class UserDataController {
    
    @Autowired 
    UserDataService userDetailsService;

    @PostMapping("/users")
    public UserData createUser(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") UserData userData) throws IOException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsService.registerUser(userData, file, user);
    }

    @GetMapping("users/{userId}")
    public UserData getuser(@PathVariable Long userid) {
        return userDetailsService.getUser(userid);
    }

    @DeleteMapping("users/{userId}")
    public String deleteUser(@PathVariable Long userId){
        return userDetailsService.deleteUser(userId);
    }
    
}
