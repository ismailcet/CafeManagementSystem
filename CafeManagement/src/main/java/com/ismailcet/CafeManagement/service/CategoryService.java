package com.ismailcet.CafeManagement.service;

import com.google.common.base.Strings;
import com.ismailcet.CafeManagement.JWT.JwtFilter;
import com.ismailcet.CafeManagement.constents.CafeConstants;
import com.ismailcet.CafeManagement.entity.Category;
import com.ismailcet.CafeManagement.repository.CategoryRepository;
import com.ismailcet.CafeManagement.utils.CafeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final JwtFilter jwtFilter;


    public CategoryService(CategoryRepository categoryRepository, JwtFilter jwtFilter) {
        this.categoryRepository = categoryRepository;
        this.jwtFilter = jwtFilter;
    }

    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap){
        try{
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap, false)){
                    categoryRepository.save(getCategoryFromMap(requestMap,false));
                    return CafeUtils.getResponseEntity("Category Added Succesfuly",HttpStatus.OK);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if (!validateId){
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd){
        Category category = new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

    public ResponseEntity<List<Category>> getAllCategory(String filterValue){
        try{
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(),HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
