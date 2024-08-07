package com.ecomerce.ecomerce.service.product;

import com.ecomerce.ecomerce.entity.Product;
import com.ecomerce.ecomerce.exceptions.AlreadyExistsException;
import com.ecomerce.ecomerce.exceptions.ResourceNotFoundException;
import com.ecomerce.ecomerce.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired) // Use Autowired instead of RequiredArgsConstructor for ProductService
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    public Product getProductByName(String name) {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        return product;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        Pageable sortedById = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id"));
        return productRepository.findAll(sortedById);
    }

    public Product addProduct(Product product){
        // Check if product name already exists
        if (productRepository.existsByName(product.getName())) {
            throw new AlreadyExistsException(product.getName() + " already exists");
        }

        // Save the product
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, Long id) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImg(product.getImg());
        existingProduct.setState(product.isState());
        return productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {
                    throw new ResourceNotFoundException("Product not found!");
                });
    }

    // You can add additional methods for searching by specific criteria or filtering

}
