package com.ecommerce.Ecom.security.services;

import com.ecommerce.Ecom.model.User;
import com.ecommerce.Ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//custom userDetailsServiceImpl which will help in gettting user beacuse we have custome database and all the other things custome

//so we will need to show that the values are

//this class is utilized by the spring security to get all the data when the user log in
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    //either full or none data action is taken
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1 user is of our model not spring security
        User user=userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User not found with username"+username));

        //2.converting the custome user model to the Userdetails which is used by the spring security
        // getting the userDetails that is the implementation of UserDetails n the UserDetailsImpl class
        return UserDetailsImpl.build(user);
    }
}
