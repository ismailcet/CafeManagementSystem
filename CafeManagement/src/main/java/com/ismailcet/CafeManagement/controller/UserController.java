package com.ismailcet.CafeManagement.controller;

import com.ismailcet.CafeManagement.constents.CafeConstants;
import com.ismailcet.CafeManagement.service.UserService;
import com.ismailcet.CafeManagement.utils.CafeUtils;
import com.ismailcet.CafeManagement.wrapper.UserWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService  userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signup")
    public ResponseEntity<String> singUp(@RequestBody Map<String,String> requestMap){
        try{
            return userService.signUp(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap){
        try{
            return userService.login(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();

        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping()
    public ResponseEntity<List<UserWrapper>> getAllUser(){
        try{
            return userService.getAllUser();
        }catch (Exception exc){
            exc.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping()
    public ResponseEntity<String> update(@RequestBody Map<String, String> requestMap){
        try{
            return userService.update(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken(){
        try{
            return userService.checkToken();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap){
        try{
            return userService.changePassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap){
        try{
            return userService.forgotPassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
