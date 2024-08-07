package com.ecomerce.ecomerce.service.product;

import com.ecomerce.ecomerce.entity.Category;
import com.ecomerce.ecomerce.entity.Product;
import com.ecomerce.ecomerce.exceptions.AlreadyExistsException;
import com.ecomerce.ecomerce.exceptions.ResourceNotFoundException;
import com.ecomerce.ecomerce.repository.ProductRepository;
import com.ecomerce.ecomerce.repository.CategoryRepository; // Import CategoryRepository

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; 

    @Override
    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public Product getProductByName(String name) {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        return product;
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        Pageable sortedById = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id"));
        return productRepository.findAll(sortedById);
    }
    
    @Override
    public Product addProduct(Product product) {
        // Check if product name already exists
        if (productRepository.existsByName(product.getName())) {
            throw new AlreadyExistsException(product.getName() + " already exists");
        }
    
        // Ensure categories are managed
        Set<Category> managedCategories = new HashSet<>();
        for (Category category : product.getCategories()) {
            Long categoryId = category.getId().longValue(); // Convert to Long
            Category managedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
            managedCategories.add(managedCategory);
        }
        product.setCategories(managedCategories);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product, Long id) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
    
        existingProduct.setImg(product.getImg());
    
        existingProduct.setState(product.isState());
        List<Category> updatedCategoriesList = new ArrayList<>(product.getCategories());
        existingProduct.setCategories(new HashSet<>(updatedCategoriesList));    
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {
                    throw new ResourceNotFoundException("Product not found!");
                });
    }

    @Override
    public Page<Product> customFiltered(BigDecimal minPrice, BigDecimal maxPrice, 
        List<String> categories, String name, String description, Pageable pageable) {
        return productRepository.findCustomFiltered(minPrice, maxPrice, name, description, categories, pageable);
    }

    @Override
    public Page<Product> getProductsByNameContaining(String name, Pageable pageable) {
        Page<Product> product = productRepository.getProductsByNameContaining(name, pageable);
        return product;
    }
}
