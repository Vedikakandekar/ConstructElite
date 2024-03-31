package com.constructElite.config;

import com.constructElite.Entity.User;
import com.constructElite.Services.UserService;
import com.constructElite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=  userService.findByEmail(username);

        if(user==null)
        {
            throw new UsernameNotFoundException("User NOT Found !!");
        }

        return   new CustomUserDetails(user);

    }

}
