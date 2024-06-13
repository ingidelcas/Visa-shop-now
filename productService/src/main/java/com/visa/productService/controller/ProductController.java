package com.visa.productService.controller;

import com.visa.lib.DTO.ApiErrorResponse;
import com.visa.productService.dto.ProductDto;
import com.visa.productService.http.header.HeaderGenerator;
import com.visa.productService.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private HeaderGenerator headerGenerator;

    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ProductDto.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> products =  productService.getAllProduct();
        if(!products.isEmpty()) {
            return new ResponseEntity<List<ProductDto>>(
                    products,
                    headerGenerator.getHeadersForSuccessGetMethod(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<List<ProductDto>>(
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }

    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ProductDto.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/byCategory", params = "category")
    public ResponseEntity<List<ProductDto>> getAllProductByCategory(@RequestParam ("category") String category){
        List<ProductDto> products = productService.getAllProductByCategory(category);
        if(!products.isEmpty()) {
            return new ResponseEntity<List<ProductDto>>(
                    products,
                    headerGenerator.getHeadersForSuccessGetMethod(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<List<ProductDto>>(
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }

    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ProductDto.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping (value = "/{id}")
    public ResponseEntity<ProductDto> getOneProductById(@PathVariable ("id") Integer id){
        ProductDto product =  productService.getProductById(id);
        if(product != null) {
            return new ResponseEntity<ProductDto>(
                    product,
                    headerGenerator.getHeadersForSuccessGetMethod(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<ProductDto>(
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ProductDto.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping (value = "/byName", params = "name")
    public ResponseEntity<List<ProductDto>> getAllProductsByName(@RequestParam ("name") String name){
        List<ProductDto> products =  productService.getAllProductsByName(name);
        if(!products.isEmpty()) {
            return new ResponseEntity<List<ProductDto>>(
                    products,
                    headerGenerator.getHeadersForSuccessGetMethod(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<List<ProductDto>>(
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }

}
