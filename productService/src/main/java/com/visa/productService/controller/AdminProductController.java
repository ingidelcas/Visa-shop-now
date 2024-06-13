package com.visa.productService.controller;

import com.visa.productService.dto.ProductDto;
import com.visa.productService.dto.ProductSaveRequestDto;
import com.visa.productService.http.header.HeaderGenerator;
import com.visa.productService.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private HeaderGenerator headerGenerator;

    @PostMapping(value = "/products")
    private ResponseEntity<ProductDto> addProduct(@RequestBody ProductSaveRequestDto productDto, HttpServletRequest request){
    	if(productDto != null) {
    		try {
    		var product	= productService.addProduct(productDto);
    	        return new ResponseEntity<ProductDto>(
    	        		product,
    	        		headerGenerator.getHeadersForSuccessPostMethod(request, product.getProductId()),
    	        		HttpStatus.CREATED);
    		}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<ProductDto>(
						headerGenerator.getHeadersForError(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
    	}
    	return new ResponseEntity<ProductDto>(
    			headerGenerator.getHeadersForError(),
    			HttpStatus.BAD_REQUEST);       
    }
    
    @DeleteMapping(value = "/products/{id}")
    private ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer id){
    	ProductDto product = productService.getProductById(id);
    	if(product != null) {
    		try {
    			productService.deleteProduct(id);
    	        return new ResponseEntity<Void>(
    	        		headerGenerator.getHeadersForSuccessGetMethod(),
    	        		HttpStatus.OK);
    		}catch (Exception e) {
				e.printStackTrace();
    	        return new ResponseEntity<Void>(
    	        		headerGenerator.getHeadersForError(),
    	        		HttpStatus.INTERNAL_SERVER_ERROR);
			}
    	}
    	return new ResponseEntity<Void>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);      
    }
}
