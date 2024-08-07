package com.ecomerce.ecomerce.service.category;

import com.ecomerce.ecomerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    Page<Category> getAllCategories(Pageable pageable);
    Page<Category> getCategoriesByNameContaining(String name, Pageable pageable);
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);
}
