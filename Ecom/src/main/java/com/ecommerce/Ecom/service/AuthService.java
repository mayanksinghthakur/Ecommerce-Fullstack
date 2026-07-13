package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.payload.AuthenticationResult;
import com.ecommerce.Ecom.payload.UserResponse;
import com.ecommerce.Ecom.security.request.LoginRequest;
import com.ecommerce.Ecom.security.request.SignupRequest;
import com.ecommerce.Ecom.security.response.MessageResponse;
import com.ecommerce.Ecom.security.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;



public interface AuthService {

    AuthenticationResult login(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> register(SignupRequest signUpRequest);

    UserInfoResponse getCurrentUserDetails(Authentication authentication);

    ResponseCookie logoutUser();

    UserResponse getAllSellers(Pageable pageable);
}
