package com.ismailcet.CafeManagement.JWT;

import com.ismailcet.CafeManagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    final UserRepository userRepository;

    public CustomerUsersDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private com.ismailcet.CafeManagement.entity.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userRepository.findByEmailId(username);

        if(!Objects.isNull(userDetail)){
            return new User(userDetail.getEmail(),userDetail.getPassword(), new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User not found .");
        }
    }
    public com.ismailcet.CafeManagement.entity.User getUserDetail(){
        return userDetail;
    }

}
