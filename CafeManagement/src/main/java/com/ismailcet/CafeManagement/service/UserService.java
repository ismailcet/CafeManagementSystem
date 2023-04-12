package com.ismailcet.CafeManagement.service;

import com.ismailcet.CafeManagement.constents.CafeConstants;
import com.ismailcet.CafeManagement.entity.User;
import com.ismailcet.CafeManagement.repository.UserRepository;
import com.ismailcet.CafeManagement.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
