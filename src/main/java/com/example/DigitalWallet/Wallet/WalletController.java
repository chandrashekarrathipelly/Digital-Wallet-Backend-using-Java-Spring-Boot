package com.example.DigitalWallet.Wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.DigitalWallet.User.User;
import com.example.DigitalWallet.UserData.UserData;

@RequestMapping("wallet")
public class WalletController {
    
    @Autowired
    WalletService walletService;

    @GetMapping("/getwallet/{userId}")
    public Wallet getWallet(@PathVariable Long userId ){
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return walletService.getWallet(user.getUserId());
    }

    @DeleteMapping("deleteWallet/{userId}")
    public String deleteWallet(){
        UserData user=(UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return walletService.deleteWallet(user.getUserId());
    }
}
