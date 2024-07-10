package com.example.DigitalWallet.Transaction;

import com.example.DigitalWallet.UserData.UserData;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;
    private long amount;

    @ManyToOne
    @JoinColumn(name = "transferFrom", referencedColumnName = "userId")
    UserData transferFrom;

    @ManyToOne
    @JoinColumn(name = "transferTo", referencedColumnName = "userId")
    UserData transferTo;
}
