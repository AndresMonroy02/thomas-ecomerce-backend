package com.ecomerce.ecomerce.service.product;

import com.ecomerce.ecomerce.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface IProductService {

    Product getProductById(Long id);
    Product getProductByName(String name);
    Page<Product> getAllProducts(Pageable pageable);
    Product addProduct(Product product);
    Product updateProduct(Product product, Long id);
    void deleteProductById(Long id);

    // Add additional methods as needed, e.g.,
    // Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);
    // List<Product> getRelatedProducts(Long productId);
}
