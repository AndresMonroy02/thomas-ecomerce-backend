package com.ecomerce.ecomerce.service.category;

import com.ecomerce.ecomerce.entity.Category;
import com.ecomerce.ecomerce.exceptions.AlreadyExistsException;
import com.ecomerce.ecomerce.exceptions.ResourceNotFoundException;
import com.ecomerce.ecomerce.repository.CategoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found");
        }
        return category;
    }

    @Override
    public Page<Category> getAllCategories(Pageable pageable) {
    
        Pageable sortedById = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id"));
        return categoryRepository.findAll(sortedById);
    }

    @Override
    public Category addCategory(Category category) {
        return  Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }) .orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
            .ifPresentOrElse(categoryRepository::delete, () -> {
                throw new ResourceNotFoundException("Category not found!");
            });
    }

    @Override
    public Page<Category> getCategoriesByNameContaining(String name, Pageable pageable) {
        Page<Category> categories = categoryRepository.getCategoriesByNameContaining(name, pageable);
        return categories;
    }


}
