package com.example.DigitalWallet.User;

import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.DigitalWallet.AuthResponse.AuthResponse;
import com.example.DigitalWallet.Jwt.JwtService;
import com.example.DigitalWallet.Util.EmailSender;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    EmailSender email;

    public String userRegistration(User userEntity) {
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return "user registered sucessfully";
    }

    public AuthResponse loginuser(User user) {
        String username = user.getUsername();
        UserDetails userDetails = loadUserByUsername(username);
        boolean isPasswordSame = this.encoder.matches(user.getPassword(), userDetails.getPassword());
        if (isPasswordSame) {
            email.sendEmail(userDetails.getUsername(), "Logged in Successfully", "You have logged in");
            return AuthResponse.builder().token(jwtService.generateToken(username)).build();
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not Found");
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
