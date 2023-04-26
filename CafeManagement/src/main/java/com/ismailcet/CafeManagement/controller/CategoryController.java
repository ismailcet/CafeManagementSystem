package com.ismailcet.CafeManagement.controller;

import com.ismailcet.CafeManagement.constents.CafeConstants;
import com.ismailcet.CafeManagement.entity.Category;
import com.ismailcet.CafeManagement.service.CategoryService;
import com.ismailcet.CafeManagement.utils.CafeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewCategory (@RequestBody Map<String, String> requestMap){
        try{
            return categoryService.addNewCategory(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false)
                                                             String filterValue){
        try{
            return categoryService.getAllCategory(filterValue);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
