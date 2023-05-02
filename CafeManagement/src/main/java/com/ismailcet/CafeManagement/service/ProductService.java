package com.ismailcet.CafeManagement.service;

import com.ismailcet.CafeManagement.JWT.JwtFilter;
import com.ismailcet.CafeManagement.constents.CafeConstants;
import com.ismailcet.CafeManagement.entity.Category;
import com.ismailcet.CafeManagement.entity.Product;
import com.ismailcet.CafeManagement.repository.ProductRepository;
import com.ismailcet.CafeManagement.utils.CafeUtils;
import com.ismailcet.CafeManagement.wrapper.ProductWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final JwtFilter jwtFilter;

    public ProductService(ProductRepository productRepository, JwtFilter jwtFilter) {
        this.productRepository = productRepository;
        this.jwtFilter = jwtFilter;
    }

    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {

        try{
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap, false)){
                    productRepository.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Product Added Succesfully",HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }
    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));

        return product;
    }

    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{
            List <Product> products =
                    productRepository.findAll();
             List<ProductWrapper> productWrappers =
                     products.stream().map(product->new ProductWrapper(
                             product.getId(),
                             product.getName(),
                             product.getDescription(),
                             product.getPrice(),
                             product.getStatus(),
                             product.getCategory().getId(),
                             product.getCategory().getName()
                     )).collect(Collectors.toList());

            return new ResponseEntity<>(productWrappers,HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
