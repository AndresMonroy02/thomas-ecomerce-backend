package com.ecomerce.ecomerce.controllers;

import com.ecomerce.ecomerce.entity.Product;
import com.ecomerce.ecomerce.service.product.IProductService;



import com.ecomerce.ecomerce.dto.ApiResponse;
import com.ecomerce.ecomerce.exceptions.AlreadyExistsException;
import com.ecomerce.ecomerce.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
  private final IProductService productService;

  @GetMapping
  public ResponseEntity<Page<Product>> getAllProducts(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    if (page < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    Pageable pageable = PageRequest.of(page - 1, size);
    Page<Product> products = productService.getAllProducts(pageable);
    return ResponseEntity.ok(products);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody Product product) {
    try {
      Product savedProduct = productService.addProduct(product);
      return ResponseEntity.ok(new ApiResponse("Success", savedProduct));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
    try {
      Product product = productService.getProductById(id);
      return ResponseEntity.ok(new ApiResponse("Found", product));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/findByName/{name}")
  public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
    try {
      Product product = productService.getProductByName(name);
      return ResponseEntity.ok(new ApiResponse("Found", product));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal server error", null));
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
    try {
      productService.deleteProductById(id);
      return ResponseEntity.ok(new ApiResponse("Deleted", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null)); // Typo corrected here (gefunden is German for "found")
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody Product product) {
    try {
      Product updatedProduct = productService.updateProduct(product, id);
      return ResponseEntity.ok(new ApiResponse("Update success!", updatedProduct));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  // You can add additional methods for searching by specific criteria or filtering
}
