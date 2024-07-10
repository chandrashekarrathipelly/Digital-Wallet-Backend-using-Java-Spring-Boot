package com.example.DigitalWallet.UserData;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.example.DigitalWallet.Wallet.Wallet;
import com.example.DigitalWallet.Wallet.WalletRepo;
import com.example.DigitalWallet.Wallet.WalletStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.DigitalWallet.Transaction.Transaction;
import com.example.DigitalWallet.Transaction.TranasactionRepository;
import com.example.DigitalWallet.User.User;
import com.example.DigitalWallet.UserData.dto.TransferDto;
import com.example.DigitalWallet.Util.EmailSender;

@Service
public class UserDataService {
        @Autowired
        private com.example.DigitalWallet.User.UserRepository UserRepository;

        @Autowired
        UserDataRepository userDataRepository;

        @Autowired
        WalletRepo walletRepo;

        @Autowired
        EmailSender email;

        @Autowired
        TranasactionRepository transactionrepo;

        public UserData registerUser(UserData userData, MultipartFile filename, User user) throws IOException {
                String[] extension = Objects.requireNonNull(filename.getOriginalFilename()).split("[.]");
                String path = "C:\\Users\\annedi\\Downloads\\WalletUser\\" + user.getUserId() + "."
                                + extension[extension.length - 1];
                File f = new File(path);
                filename.transferTo(f);
                Wallet wallet = Wallet.builder()
                                .walletAmount(0)
                                .status(WalletStatus.INACTIVE)
                                .build();
                wallet = this.walletRepo.save(wallet);
                email.sendEmail(user.getUserData().getEmail(), "Wallet Created successfully",
                                "You have created wallet whose status is inactive");
                userData.setImagePath(path);
                userData.setWallet(wallet);
                userData = this.userDataRepository.save(userData);
                user.setUserData(userData);
                this.UserRepository.save(user);
                return userData;
        }

        public String updateWalletStatus(WalletStatus walletStatus, User user) {
                Wallet wallet = user.getUserData().getWallet();
                wallet.setStatus(walletStatus);
                wallet = this.walletRepo.save(wallet);
                email.sendEmail(user.getUserData().getEmail(), "Wallet Status Updated",
                                "Your wallet is now" + walletStatus.toString());
                return "Wallet status Updated Successfully";
        }

        public Wallet addMoneyToWallet(long amout, User user) {
                Wallet wallet = user.getUserData().getWallet();
                if (wallet.getStatus().equals(WalletStatus.INACTIVE)) {
                        throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Wallet is in active");
                }
                wallet.setWalletAmount(wallet.getWalletAmount() + amout);
                wallet = this.walletRepo.save(wallet);
                email.sendEmail(user.getUserData().getEmail(), "Money Added",
                                "You have Added money to your wallet total amount: " + wallet.getWalletAmount());
                return wallet;
        }

        public UserData getUser(Long userid) {
                return (UserData) userDataRepository.findById(userid)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
                                                "Inavalid user"));
        }

        public String deleteUser(Long userid) {
                userDataRepository.deleteById(userid);
                return "user deleted";
        }

        public Transaction transfer(TransferDto transferDto, User user) {
                Wallet wallet = user.getUserData().getWallet();
                if (wallet.getStatus().equals(WalletStatus.INACTIVE)) {
                        throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Wallet is in active");
                }
                if (wallet.getWalletAmount() < transferDto.getAmount()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Amount is greater than the current balance");
                }
                UserData transferData = this.userDataRepository.findByPhoneNumber(transferDto.getPhoneNumber())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "User Not found"));
                Wallet transferWallet = transferData.getWallet();
                if (transferWallet.getStatus().equals(WalletStatus.INACTIVE)) {
                        throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Wallet is in active");
                }
                wallet.setWalletAmount(wallet.getWalletAmount() - transferDto.getAmount());
                wallet = this.walletRepo.save(wallet);
                email.sendEmail(user.getUserData().getEmail(), "Money Transferred",
                                "You have transfered money from wallet  amount: " + transferDto.getAmount());
                transferWallet.setWalletAmount(transferWallet.getWalletAmount() + transferDto.getAmount());
                this.walletRepo.save(transferWallet);
                email.sendEmail(user.getUserData().getEmail(), "Money Transferred",
                                "money has transffered to your wallet  amount: " + transferDto.getAmount());
                Transaction t= Transaction.builder()
                .amount(transferDto.getAmount())
                .transferFrom(user.getUserData())
                .transferTo(transferData)
                .build();
                t = this.transactionrepo.save(t);
                return t;
        }

}
