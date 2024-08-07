package com.ecomerce.ecomerce.controllers;

import com.ecomerce.ecomerce.entity.Category;
import com.ecomerce.ecomerce.service.category.ICategoryService;

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
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<Category>> getAllCategories(
            @RequestParam(defaultValue = "1") int page, // Start page at 1
            @RequestParam(defaultValue = "10") int size) {
        if (page < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
    
        Page<Category> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
        System.out.println(name);
        try {
            Category theCategory = categoryService.addCategory(name);
            return  ResponseEntity.ok(new ApiResponse("Success", theCategory));
        } catch (AlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category theCategory = categoryService.getCategoryById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/findByName/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category theCategory = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Category not found", null));
        } catch (Exception e) {
            // Handle other exceptions (e.g., database errors)
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal server error", null));
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
            categoryService.deleteCategoryById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/findByNameContaining")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<Map<String, Object>>> getCategoriesByNameContaining(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Category> categories = categoryService.getCategoriesByNameContaining(name != null ? name : "", pageable);
            Page<Map<String, Object>> dataPage = categories.map(category -> {
                if (category != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", category.getId());
                    map.put("name", category.getName());
                    return map;
                } else {
                    return null;
                }
            });
            return ResponseEntity.ok(dataPage);
        } catch (Exception e) {
            // Handle potential exceptions
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
