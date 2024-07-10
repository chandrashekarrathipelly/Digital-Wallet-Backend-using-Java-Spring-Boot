package com.example.DigitalWallet.UserData;

import com.example.DigitalWallet.Wallet.Wallet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    private String imagePath;

    @Column(nullable = false,unique = true)
    private String panCardNumber;

    @Column(nullable = false,unique = true)
    private String email;

    @OneToOne
    Wallet wallet;
}
