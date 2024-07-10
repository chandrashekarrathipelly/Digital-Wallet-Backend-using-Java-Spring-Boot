package com.example.DigitalWallet.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TranasactionRepository extends JpaRepository<Transaction, Long> {
    
}
