package com.ismailcet.CafeManagement.service;

import com.ismailcet.CafeManagement.JWT.CustomerUsersDetailsService;
import com.ismailcet.CafeManagement.JWT.JwtFilter;
import com.ismailcet.CafeManagement.JWT.JwtUtil;
import com.ismailcet.CafeManagement.constents.CafeConstants;
import com.ismailcet.CafeManagement.entity.User;
import com.ismailcet.CafeManagement.repository.UserRepository;
import com.ismailcet.CafeManagement.utils.CafeUtils;
import com.ismailcet.CafeManagement.utils.EmailUtils;
import com.ismailcet.CafeManagement.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomerUsersDetailsService customerUsersDetailsService;

    private final JwtUtil jwtUtil;

    private final JwtFilter jwtFilter;

    private final EmailUtils emailUtils;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, CustomerUsersDetailsService customerUsersDetailsService, JwtUtil jwtUtil, JwtFilter jwtFilter, EmailUtils emailUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.customerUsersDetailsService = customerUsersDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.emailUtils = emailUtils;
    }

    public ResponseEntity<String> signUp(Map<String, String> requestMap){
        log.info("Inside signup {}",requestMap);
        try{
            if(validateSignUpMap(requestMap)){
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userRepository.save(setUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Succesfully Registered. ",HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("Email already exits.", HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validateSignUpMap(Map<String, String> requestMap){

       if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
           return true;
       }
       return false;
    }

    private User setUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");

        return user;
    }

    public ResponseEntity<String> login(Map<String, String> requestMap){
        log.info("Inside login");
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                            jwtUtil
                                    .generateToken(customerUsersDetailsService
                                        .getUserDetail()
                                        .getEmail(),customerUsersDetailsService
                                                    .getUserDetail()
                                                    .getRole())
                            +"\"}",HttpStatus.OK);
                }else{
                    return new ResponseEntity<String>("{\"Message\":\""+"Wait for admin approval."+"\"}",HttpStatus.BAD_REQUEST);
                }
            }

        }catch (Exception ex){
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"Message\":\""+"Bad Credentials."+"\"}",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<UserWrapper>> getAllUser(){
        try{
            if(jwtFilter.isAdmin()){
                List<User> users = userRepository.findAll();
                List<UserWrapper> converterUser = users.stream()
                                                .map(user->new UserWrapper(
                                                        user.getId(),
                                                        user.getName(),
                                                        user.getEmail(),
                                                        user.getContactNumber(),
                                                        user.getStatus()
                                                ))
                                                .collect(Collectors.toList());

                return new ResponseEntity<>(converterUser,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap){
        try{
            if(jwtFilter.isAdmin()){
                Optional<User> optional=
                        userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()){
                    userRepository.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userRepository.getAllAdmin());
                    return CafeUtils.getResponseEntity("User status Updated Successfully",HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("User id doesn not exist",HttpStatus.OK);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approved","USER:- "+user+"\n is approved by \nADMIN:-"+ jwtFilter.getCurrentUser(),allAdmin);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Disable","USER:- "+user+"\n is disabled by \nADMIN:-"+ jwtFilter.getCurrentUser(),allAdmin);
        }
    }
}
