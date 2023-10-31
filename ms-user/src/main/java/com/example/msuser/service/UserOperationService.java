package com.example.msuser.service;

import com.example.msuser.config.client.CardClient;
import com.example.msuser.config.client.PaymentClient;
import com.example.msuser.config.client.ProductClient;
import com.example.msuser.error.UserExistsException;
import com.example.msuser.error.UserNotFoundException;
import com.example.msuser.model.*;
import com.example.msuser.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOperationService {

    private final UserRepository userRepository;
    private final PaymentClient paymentClient;
    private final CardClient cardClient;
    private final ProductClient productClient;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserInfoService userInfoService;
    private final PasswordEncoder passwordEncoder;


    public String register(AuthRequest authRequest) {

        Optional<UserEntity> user = userRepository.findByUsername(authRequest.getUsername());
        if (user.isPresent()) {

            throw new UserExistsException("Username with name " + authRequest.getUsername() + " already exists");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        userRepository.save(userEntity);
        return "User Added Successfully";
    }

    public List<CardDto> getActiveCards(HttpServletRequest request, int pageNumber, int pageSize) {

        return cardClient.getActiveCards(getUsernameFromToken(request), pageNumber, pageSize);
    }


    public List<ProductDto> getAvailableProducts(int pageNumber, int pageSize) {

        return productClient.getAllAvailableProducts(pageNumber, pageSize);
    }


    public TransactionDto buyProduct(PaymentRequest paymentRequest, HttpServletRequest request) {

        String paymentData =  new String(Base64.getEncoder().encode(getUsernameFromToken(request).concat(":")
                .concat(paymentRequest.getCvv()).concat(":").concat(paymentRequest.getExpirationDate()).getBytes()));

        PurchaseRequest purchaseRequest = PurchaseRequest.builder()
                .maskedPan(paymentRequest.getMaskedPan())
                .productName(paymentRequest.getProductName())
                .build();

        return paymentClient.buyProduct(paymentData, purchaseRequest);
    }

    public String generateToken(AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {

            return jwtService.generateToken(authRequest.getUsername());
        } else {

            throw new UserNotFoundException("Username : " + authRequest.getUsername() + " not found");
        }
    }

    private String getUsernameFromToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        return username;
    }
}
