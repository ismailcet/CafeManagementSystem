package com.ismailcet.CafeManagement.controller;

import com.ismailcet.CafeManagement.constents.CafeConstants;
import com.ismailcet.CafeManagement.entity.Bill;
import com.ismailcet.CafeManagement.service.BillService;
import com.ismailcet.CafeManagement.utils.CafeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bill")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/generateReport")
    public ResponseEntity<String> generateReport(
            @RequestBody Map<String, Object> requestMap
    ){
        try{
            return billService.generateReport(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getBills")
    public ResponseEntity<List<Bill>> getBills(){
        try{
            return billService.getBills();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/getPdf")
    public ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap){
        try{
            return billService.getPdf(requestMap);
        }catch (Exception ex){

        }
        return null;
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Integer id){
        try{
            return billService.deleteBill(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
